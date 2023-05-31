package ro.ti.documentProcessor.MVC.controller.file;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.model.ModelXlsProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class XlsController implements ControllerFile {
    private static volatile XlsController instance;
    private XlsController(){

    }
    public static XlsController getInstance(){
     return instance!= null ? instance: (instance = new XlsController());
    }
    @Override
    public HashMap readFromFile(String path) throws IOException {
        Model xlsModel = new ModelXlsProcessor();
        File file  =  new File(path);
        return xlsModel.parseFile(file);
    }

    @Override
    public void writeToFile(String path, String name) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(20);
        cell.setCellStyle(style);

        path ="E:\\JavaProjects\\DocumentProcessor\\src\\main\\resources\\xlsFiles\\";
        String fileLocation = path.substring(0, path.length()) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

}
