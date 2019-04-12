package jandcode.poi;

import jandcode.commons.test.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

public class Example_Test extends Utils_Test {

    @Test
    public void test_hssf_1() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow(0);

        // Create a cell and put a date value in it.  The first cell is not styled as a date.
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(new Date());

        // we style the second cell as a date (and time).  It is important to create a new cell style from the workbook
        // otherwise you can end up modifying the built in style and effecting not only this cell but other cells.
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        cell = row.createCell(1);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(utils.replaceTestName("temp/#__%.xls"));
        wb.write(fileOut);
        fileOut.close();

        wb.close();
    }

    @Test
    public void test_xssf_1() throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        XSSFRow row = sheet.createRow(0);

        // Create a cell and put a date value in it.  The first cell is not styled as a date.
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(new Date());

        // we style the second cell as a date (and time).  It is important to create a new cell style from the workbook
        // otherwise you can end up modifying the built in style and effecting not only this cell but other cells.
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cell = row.createCell(1);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(utils.replaceTestName("temp/#__%.xlsx"));
        wb.write(fileOut);
        fileOut.close();

        wb.close();
    }


}
