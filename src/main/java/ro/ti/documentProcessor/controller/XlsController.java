package ro.ti.documentProcessor.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsController {
    private static XlsController controller;
    public static XlsController getController(){
        return (controller == null) ? (new XlsController()):(controller);
    }

    public void openFile(String path) throws IOException {
        Desktop.getDesktop().open(new File(path));
    }
    public void readFromFile(File file) throws IOException {
        FileInputStream fileStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileStream);
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i =0;
        for (Row row: sheet){
            data.put(i, new ArrayList<>());
            for (Cell cell:
                    row) {
                switch (cell.getCellType()){
                    case STRING:
                        data.get(i).add(cell.getRichStringCellValue().toString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell))
                            data.get(i).add(cell.getDateCellValue()+"");
                        else data.get(i).add(cell.getNumericCellValue()+"");
                        break;
                    case BOOLEAN: data.get(i).add(cell.getBooleanCellValue()+"");
                        break;
                    case FORMULA: data.get(i).add(cell.getCellFormula()+"");
                        break;
                    default: data.get(i).add(" ");
                }
            }
            i++;
        }


        for (int j : data.keySet()){
            for (String s:
                    data.get(j)) {
                System.out.print(" " + s);
            }
            System.out.println();
        }
    }

    public void writeToFile() throws IOException {
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

        String path ="E:\\JavaProjects\\DocumentProcessor\\src\\main\\resources\\xlsFiles\\";
        String fileLocation = path.substring(0, path.length()) + "temp.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }
}
