package ru.tusur.gazpromedatomsk.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import ru.tusur.gazpromedatomsk.model.Description;
import ru.tusur.gazpromedatomsk.model.Dish;
import ru.tusur.gazpromedatomsk.model.NotificationEmail;
import ru.tusur.gazpromedatomsk.model.Order;
import ru.tusur.gazpromedatomsk.model.User;
import ru.tusur.gazpromedatomsk.service.FileService;
import ru.tusur.gazpromedatomsk.service.MenuService;
import ru.tusur.gazpromedatomsk.service.UserService;
import ru.tusur.gazpromedatomsk.service.impl.utils.StyleExcel;
import ru.tusur.gazpromedatomsk.service.mail.MailService;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

  private final UserService userService;
  private final MailService mailService;
  private final MenuService menuService;
  private final StyleExcel styleExcel;

  private final String EMAIL_DELIVERY = "eda@gmail.com";

  private HSSFWorkbook workbook;
  private HSSFSheet sheet;
  private int finalSum;

  public FileServiceImpl(UserService userService,
      MailService mailService, MenuService menuService,
      StyleExcel styleExcel) {
    this.userService = userService;
    this.mailService = mailService;
    this.menuService = menuService;
    this.styleExcel = styleExcel;
  }

  @Override
  public void sendEmailToAdmin() {
    userService.getAllUser(true).forEach(
        (user) -> mailService.sendEmailWithAttachment(new NotificationEmail("Отчет по заказу",
            user.getEmail(),
            "Это автоматическая рассылка, отвечать на нее не нужно.\n"
                + "В случае возникновения вопросов обращайтесь в техподдержку.",
            createFileExcelForAdmin()
        )));
  }

  @Override
  public void sendEmailToDelivery() {
    mailService.sendEmailWithAttachment(new NotificationEmail("Заказ от Газпром",
        EMAIL_DELIVERY,
        "Это автоматическая рассылка, отвечать на нее не нужно.\n"
            + "В случае возникновения вопросов обращайтесь к главному по заказу еды.",
        createFileExcelForDelivery()
    ));
  }

  @Override
  public String createFileExcelForAdmin() {
    String fileName = createFileExcel("report");

    finalSum = 0;

    setFile(true);

    try (FileOutputStream out = new FileOutputStream(fileName)) {
      workbook.write(out);
    } catch (IOException e) {
      e.printStackTrace();
    }

    log.debug("Excel file success created!");

    return fileName;
  }


  @Override
  public String createFileExcelForDelivery() {
    String fileName = createFileExcel("order");

    finalSum = 0;

    setFile(false);

    try (FileOutputStream out = new FileOutputStream(fileName)) {
      workbook.write(out);
    } catch (IOException e) {
      e.printStackTrace();
    }

    log.debug("Excel file success created!");

    return fileName;
  }

  private String createFileExcel(String fileName) {
    workbook = new HSSFWorkbook();
    sheet = workbook.createSheet("Заказ - Доставка от Александры");

    Date nowDate = new Date();
    String title = "EEEE (d MMMM) - Доставка от Александры";

    if (fileName.equals("report")){
      title = "Заказ еды от d MMMM";
    }

    SimpleDateFormat strDate = new SimpleDateFormat(title);
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");

    fileName += "_" + formatForDateNow.format(nowDate) + ".xls";

    Row row = sheet.createRow(0);
    row.createCell(0).setCellValue(strDate.format(nowDate));
    row.getCell(0).setCellStyle(styleExcel.getStyleUniversal(-1, workbook));

    sheet.setColumnWidth(0, 250 * 59);

    return fileName;
  }

  private void setFile(Boolean forAdmin) {
    String tempType = "";
    int rowNum = 1;

    List<User> users = userService.getAllUser(false);

    for (Dish item : menuService.getAllToday().getDishes()) {
      Row row = sheet.createRow(rowNum);

      if (!item.getType().equals(tempType)) {
        tempType = item.getType();

        setCell(row, 0, tempType, styleExcel.getStyleForType(workbook));

        for (int i = 0; i < users.size(); i++) {
          if (forAdmin) {
            String userName = users.get(i).getName() + " " + users.get(i).getLastName();
            sheet.setColumnWidth(i + 2, 250 * userName.length() * 2);
            setCell(row, i + 2, userName, styleExcel.getStyleBorder(-1, workbook));

          } else {
            setCell(row, i + 2, transformNumberToRomanNumeral(i + 1),
                styleExcel.getStyleBorder(-1, workbook));
          }
        }

        if (rowNum == 1) {
          setCell(row, 1, "Цена", styleExcel.getStyleUniversal(-1, workbook));
          setCell(row, users.size() + 2, "Сумма", styleExcel.getStyleUniversal(-1, workbook));
        }
        rowNum++;

        row = sheet.createRow(rowNum);
      }

      setValueOfDish(item, row, rowNum);

      setValueOfUser(item, row, rowNum, users);

      rowNum++;
    }

    Row row = sheet.createRow(rowNum + 1);
    int column = 2;

    if (forAdmin) {
      setCell(row, 1, "Итого", styleExcel.getStyleBorder(-1, workbook));
    }

    for (User user : users) {
      int sum = user.getOrders().stream().mapToInt(Order::getCount).sum();

      setCell(row, column, sum, styleExcel.getStyleBorder(-1, workbook));
      column++;
    }

    setCell(row, column, finalSum, styleExcel.getStyleBorder(-1, workbook));
  }

  private void setValueOfDish(Dish item, Row row, int rowNum) {
    String cellDish;
    if (!item.getDescription().get(0).getContent().equals("")) {
      cellDish = String.format("{%s} %s (%s), %s",
          item.getDishId(),
          item.getTitle(),
          item.getDescription().stream().map(Description::getContent)
              .collect(Collectors.joining()),
          item.getWeight());
    } else {
      cellDish = String.format("{%s} %s, %s",
          item.getDishId(),
          item.getTitle(),
          item.getWeight());
    }

    if (rowNum % 2 == 0) {
      setCell(row, 0, cellDish, styleExcel.getStyleBorder(0, workbook));
      setCell(row, 1, item.getPrice(), styleExcel.getStyleBorder(1, workbook));
    } else {
      setCell(row, 0, cellDish, styleExcel.getStyleGreen(0, workbook));
      setCell(row, 1, item.getPrice(), styleExcel.getStyleGreen(1, workbook));
    }
  }

  private void setValueOfUser(Dish item, Row row, int rowNum,
      List<User> users) {
    int sum = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date today = new Date();

    try {
      today = sdf.parse(sdf.format(new Date()));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < users.size(); i++) {
      Date finalToday = today;
      int value = users.get(i).getOrders().stream()
          .filter(order -> item.getDishId().equals(order.getDishId()))
          .filter(order -> finalToday.equals(order.getCreatedAt()))
          .mapToInt(Order::getCount)
          .sum();

      sum += value * item.getPrice();

      if (rowNum % 2 == 0) {
        setCell(row, i + 2, value, styleExcel.getStyleBorderForUser(workbook));
      } else {
        setCell(row, i + 2, value, styleExcel.getStyleGreenForUser(workbook));
      }
    }

    finalSum += sum;

    if (rowNum % 2 == 0) {
      setCell(row, users.size() + 2, sum, styleExcel.getStyleBorder(-1, workbook));
    } else {
      setCell(row, users.size() + 2, sum, styleExcel.getStyleGreen(-1, workbook));
    }
  }

  private void setCell(Row row, int columnNum, String value, CellStyle cellStyle) {
    row.createCell(columnNum).setCellValue(value);
    row.getCell(columnNum).setCellStyle(cellStyle);
  }

  private void setCell(Row row, int columnNum, int value, CellStyle cellStyle) {
    row.createCell(columnNum).setCellValue(value);
    row.getCell(columnNum).setCellStyle(cellStyle);
  }

  private int getCountDish(List<Order> orders, String dishId) {
    int count = 0;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    for (Order order : orders) {
      try {
        if (order.getDishId().equals(dishId) && order.getCreatedAt()
            .equals(sdf.parse(sdf.format(new Date())))) {
          count = order.getCount();
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return count;
  }


  private String transformNumberToRomanNumeral(int number) {
    int[] roman_value_list = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    String[] roman_char_list = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX",
        "V", "IV", "I"};
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < roman_value_list.length; i += 1) {
      while (number >= roman_value_list[i]) {
        number -= roman_value_list[i];
        res.append(roman_char_list[i]);
      }
    }
    return res.toString();
  }
}
