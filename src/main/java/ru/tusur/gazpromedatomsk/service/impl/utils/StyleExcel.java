package ru.tusur.gazpromedatomsk.service.impl.utils;

import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StyleExcel {

  public CellStyle getStyleUniversal(int column, HSSFWorkbook workbook) {
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

  public HSSFFont getFont(HSSFWorkbook workbook) {
    HSSFFont font = workbook.createFont();
    font.setFontName("Calibri");
    font.setFontHeightInPoints((short) 11);

    return font;
  }

  public CellStyle getStyleBorder(int column, HSSFWorkbook workbook) {
    CellStyle style = getStyleUniversal(column, workbook);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
  }

  public CellStyle getStyleGreen(int column, HSSFWorkbook workbook) {
    HSSFPalette palette = workbook.getCustomPalette();
    palette.setColorAtIndex((short) 10,
        (byte) 229,
        (byte) 255,
        (byte) 230
    );

    HSSFColor greenColor = palette.getColor(10);

    CellStyle style = getStyleBorder(column, workbook);

    style.setFillBackgroundColor(greenColor.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFillForegroundColor(greenColor.getIndex());
    return style;
  }

  public CellStyle getStyleBorderForUser(HSSFWorkbook workbook) {
    HSSFFont font = getFont(workbook);
    font.setFontHeightInPoints((short) 15);
    font.setBold(true);

    CellStyle style = getStyleBorder(-1, workbook);
    style.setFont(font);

    return style;
  }

  public CellStyle getStyleGreenForUser(HSSFWorkbook workbook) {
    HSSFFont font = getFont(workbook);
    font.setFontHeightInPoints((short) 15);
    font.setBold(true);

    CellStyle style = getStyleGreen(-1, workbook);
    style.setFont(font);

    return style;
  }

  public CellStyle getStyleForType(HSSFWorkbook workbook) {
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
}
