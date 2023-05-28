package ro.ti.documentProcessor.MVC.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.Interfaces.View;
import ro.ti.documentProcessor.MVC.controller.database.Database;
import ro.ti.documentProcessor.MVC.controller.utils.FileChecker;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class Controller implements ro.ti.documentProcessor.MVC.Interfaces.Controller {
    Model model;
    View view;
    Database database;

    Properties properties;

    ControllerFile xlsController ;
    ControllerFile rtfController;
    ControllerFile pdfController;

    @Override
    public void openFile(String path)  {
        try {
            Desktop.getDesktop().open(new File(path));
            FileChecker.checkForNewerVersion(this,path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String path, String name,String extension) throws IOException {
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

    @Override
    public HashMap readFromFile(String path, String extension) throws IOException {
        return null;
    }

    @Override
    public void writeToFile(String path, String name, String extension, HashMap content) throws IOException {

    }

    @Override
    public void setView(View view) {
        this.view=view;
    }

    @Override
    public void setModel(Model model) {
        this.model=model;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties=properties;
        database = new Database(properties);
    }

    @Override
    public void testMVC() {
        System.out.println("~MVC~");
        System.out.print("From controller:\n");
        System.out.print("View:");
        view.testView();
        System.out.print("Model:");
        model.testModel();
    }

    @Override
    public void testController() {
        System.out.print("check\n");
    }

    @Override
    public void reloadFile(String path, Timestamp time) {
        view.updateFile(path,time);
        //string sss= model.processFile();
        //view.loadFile(sss);
    }

    @Override
    public void pingDatabase() {
        database.checkifConnectionIsValid();
    }

    protected ControllerFile getFileController(String extension) {
        switch (extension){
            case "xls":
            case "xlsx":
                return xlsController !=null ? xlsController :(xlsController = new XlsController());
            case "rtf":
                return rtfController !=null ? rtfController :(rtfController = new RtfController());
            case "pdf":
                return pdfController !=null ? pdfController :(pdfController = new PdfController());
            default:
                return null;
        }
    }
}

