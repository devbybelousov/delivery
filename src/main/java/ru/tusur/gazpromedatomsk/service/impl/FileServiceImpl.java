package ru.tusur.gazpromedatomsk.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;
import ru.tusur.gazpromedatomsk.model.Description;
import ru.tusur.gazpromedatomsk.model.Dish;
import ru.tusur.gazpromedatomsk.model.NotificationEmail;
import ru.tusur.gazpromedatomsk.model.Order;
import ru.tusur.gazpromedatomsk.model.User;
import ru.tusur.gazpromedatomsk.service.FileService;
import ru.tusur.gazpromedatomsk.service.MenuService;
import ru.tusur.gazpromedatomsk.service.UserService;
import ru.tusur.gazpromedatomsk.service.mail.MailService;

@Service
@Slf4j
@AllArgsConstructor
public class FileServiceImpl implements FileService {

  private final UserService userService;
  private final MailService mailService;
  private final MenuService menuService;

  private final String EMAIL_DELIVERY = "eda@gmail.com";

  @Override
  public void sendEmailToAdmin() {
    /*userRepository.findAllByIsAdmin(true).stream().map(
        user -> {
          mailService.sendMail(new NotificationEmail("Отчет по заказу",
              user.getEmail(),
              "Чтобы посмотреть меню нажмите на <a href=\"http://localhost:8080/api/menu\">ссылку</a>"
          ));
          return user;
        }
    );*/
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
    return null;
  }

  @Override
  public String createFileExcelForDelivery() {

    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Заказ - Доставка от Александры");
    int rowNum = 0;

    Date nowDate = new Date();
    SimpleDateFormat strDate = new SimpleDateFormat("EEEE (d MMMM) - Доставка от Александры");
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");

    String fileName = "order_" + formatForDateNow.format(nowDate) + ".xls";

    sheet.setColumnWidth(0, 250 * 59);

    Row row = sheet.createRow(rowNum);
    row.createCell(0).setCellValue(strDate.format(nowDate));
    row.getCell(0).setCellStyle(getStyleUniversal(workbook, -1));
    rowNum++;

    setMenu(rowNum, sheet, workbook);

    try (FileOutputStream out = new FileOutputStream(fileName)) {
      workbook.write(out);
    } catch (IOException e) {
      e.printStackTrace();
    }

    log.debug("Excel file success created!");

    return fileName;
  }

  private void setMenu(int rowNum, HSSFSheet sheet, HSSFWorkbook workbook) {
    String tempType = "";

    List<User> users = userService.getAllUser();

    for (Dish item : menuService.getAllToday().getDishes()) {
      Row row = sheet.createRow(rowNum);

      if (!item.getType().equals(tempType)) {
        tempType = item.getType();
        row.createCell(0).setCellValue(tempType);
        row.getCell(0).setCellStyle(getStyleForType(workbook));

        for (int i = 0; i < users.size(); i++) {
          row.createCell(i + 2).setCellValue(transformNumberToRomanNumeral(i + 1));
          row.getCell(i + 2).setCellStyle(getStyleBorder(workbook, -1));
        }

        if (rowNum == 1) {
          row.createCell(1).setCellValue("Цена");
          row.getCell(1).setCellStyle(getStyleUniversal(workbook, -1));

          row.createCell(users.size() + 2).setCellValue("Сумма");
          row.getCell(users.size() + 2).setCellStyle(getStyleUniversal(workbook, -1));
        }
        rowNum++;

        row = sheet.createRow(rowNum);
      }

      setValueOfDish(item, row, rowNum, workbook);

      setValueOfUser(item, row, rowNum, workbook, users);

      rowNum++;
    }
  }

  private void setValueOfDish(Dish item, Row row, int rowNum, HSSFWorkbook workbook) {
    String cellDish;
    if (!item.getDescription().get(0).getContent().equals("")) {
      cellDish = String.format("{%s} %s (%s), %s",
          item.getId(),
          item.getTitle(),
          item.getDescription().stream().map(Description::getContent)
              .collect(Collectors.joining()),
          item.getWeight());
    } else {
      cellDish = String.format("{%s} %s, %s",
          item.getId(),
          item.getTitle(),
          item.getWeight());
    }
    row.createCell(0).setCellValue(cellDish);
    row.createCell(1).setCellValue(item.getPrice());

    if (rowNum % 2 == 0) {
      row.getCell(0).setCellStyle(getStyleBorder(workbook, 0));
      row.getCell(1).setCellStyle(getStyleBorder(workbook, 1));
    } else {
      row.getCell(0).setCellStyle(getStyleGreen(workbook, 0));
      row.getCell(1).setCellStyle(getStyleGreen(workbook, 1));
    }
  }

  private void setValueOfUser(Dish item, Row row, int rowNum, HSSFWorkbook workbook,
      List<User> users) {
    double sum = 0.0;

    for (int i = 0; i < users.size(); i++) {
      int value = getCountDish(users.get(i).getOrders(), item.getId());
      log.error(String.valueOf(value));
      row.createCell(i + 2).setCellValue(value);

      sum += value;

      if (rowNum % 2 == 0) {
        row.getCell(i + 2).setCellStyle(getStyleBorderForUser(workbook));
      } else {
        row.getCell(i + 2).setCellStyle(getStyleGreenForUser(workbook));
      }
    }

    row.createCell(users.size() + 2).setCellValue(sum);

    if (rowNum % 2 == 0) {
      row.getCell(users.size() + 2).setCellStyle(getStyleBorder(workbook, -1));
    } else {
      row.getCell(users.size() + 2).setCellStyle(getStyleGreen(workbook, -1));
    }
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


  private CellStyle getStyleUniversal(HSSFWorkbook workbook, int column) {
    CellStyle style = workbook.createCellStyle();
    style.setFont(getFont(workbook));
    style.setWrapText(true);

    if (column == 1) {
      DataFormat format = workbook.createDataFormat();
      style.setDataFormat(format.getFormat("#.0"));
      style.setVerticalAlignment(VerticalAlignment.BOTTOM);
      style.setAlignment(HorizontalAlignment.CENTER);
    } else if (column == -1) {
      style.setVerticalAlignment(VerticalAlignment.BOTTOM);
      style.setAlignment(HorizontalAlignment.CENTER);
    }

    return style;
  }

  private HSSFFont getFont(HSSFWorkbook workbook) {
    HSSFFont font = workbook.createFont();
    font.setFontName("Calibri");
    font.setFontHeightInPoints((short) 11);

    return font;
  }

  private CellStyle getStyleBorder(HSSFWorkbook workbook, int column) {
    CellStyle style = getStyleUniversal(workbook, column);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
  }

  private CellStyle getStyleGreen(HSSFWorkbook workbook, int column) {
    HSSFPalette palette = workbook.getCustomPalette();
    palette.setColorAtIndex((short) 10,
        (byte) 229,
        (byte) 255,
        (byte) 230
    );

    HSSFColor greenColor = palette.getColor(10);

    CellStyle style = getStyleBorder(workbook, column);

    style.setFillBackgroundColor(greenColor.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFillForegroundColor(greenColor.getIndex());
    return style;
  }

  private CellStyle getStyleBorderForUser(HSSFWorkbook workbook) {
    HSSFFont font = getFont(workbook);
    font.setFontHeightInPoints((short) 15);
    font.setBold(true);

    CellStyle style = getStyleBorder(workbook, -1);
    style.setFont(font);

    return style;
  }

  private CellStyle getStyleGreenForUser(HSSFWorkbook workbook) {
    HSSFFont font = getFont(workbook);
    font.setFontHeightInPoints((short) 15);
    font.setBold(true);

    CellStyle style = getStyleGreen(workbook, -1);
    style.setFont(font);

    return style;
  }

  private CellStyle getStyleForType(HSSFWorkbook workbook) {
    HSSFPalette palette = workbook.getCustomPalette();
    palette.setColorAtIndex((short) 11,
        (byte) 50,
        (byte) 205,
        (byte) 50
    );

    HSSFColor greenColor = palette.getColor(11);

    HSSFFont greenFont = getFont(workbook);
    greenFont.setColor(greenColor.getIndex());
    greenFont.setBold(true);

    CellStyle style = workbook.createCellStyle();
    style.setFont(greenFont);
    style.setAlignment(HorizontalAlignment.CENTER);
    return style;
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
