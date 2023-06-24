package ro.ti.documentProcessor.MVC.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ModelXlsProcessor implements Model {
    Controller controller;
    @Override
    public void setController(Controller controller) {
        this.controller=controller;
    }

    @Override
    public Model getModel() {
        return this;
    }

    @Override
    public HashMap parseFile(File givenFile) {
        try {
            System.out.println("Entered model with file " + givenFile.getName());
            HashMap<String, LinkedHashMap<String, ArrayList<String>>> file = new HashMap<>();
            //Make file
            FileInputStream fis = new FileInputStream(givenFile);
            Workbook workbook = new XSSFWorkbook(fis);

            int numSheets = workbook.getNumberOfSheets();
            HashMap<String, Object> data = new HashMap<>();
            ArrayList<String> pages = new ArrayList<>();

            for (int sheetIndex = 0; sheetIndex < numSheets; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String sheetName = sheet.getSheetName();
                LinkedHashMap<String, ArrayList<String>> pageData = new LinkedHashMap<>();
                ArrayList<String> indexes = new ArrayList<>();
                ArrayList<String> columns = new ArrayList<>();

                int numRows = sheet.getLastRowNum() + 1;

                for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    ArrayList<String> rowData = new ArrayList<>();

                    if (row != null) {
                        int numCols = row.getLastCellNum();

                        for (int cellIndex = 0; cellIndex < numCols; cellIndex++) {
                            Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            String cellValue = cell.toString();
                            rowData.add(cellValue);
                        }

                        if (!rowData.isEmpty()) {
                            indexes.add(String.valueOf(rowIndex));
                            columns.add(String.valueOf(rowData.size()));
                            pageData.put(String.valueOf(rowIndex), rowData);
                        }
                    }
                }

                data.put(sheetName + "Indexes", indexes);
                data.put(sheetName + "Columns", columns);
                pages.add(sheetName);
                data.put(sheetName, pageData);
            }

            data.put("Pages", pages);

            workbook.close();
            fis.close();

            return data;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void testModel() {
        System.out.print("check\n");
        System.out.print("  From model:\n -controller: ");
        controller.testController();
    }

    public static void main(String[] args) {
        ModelXlsProcessor m = new ModelXlsProcessor();
        String filePath = "C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\Book1.xlsx";
        HashMap<String, Object> data = m.parseFile(new File(filePath)); // Parse the file and store the data in the HashMap

        ArrayList<String> pages = (ArrayList<String>) data.get("Pages");
        Collections.sort(pages, new AlphanumericComparator()); // Sort the pages numerically

        for (String page : pages) {
            System.out.println("Page: " + page);

            ArrayList<String> indexes = (ArrayList<String>) data.get(page + "Indexes");
            Collections.sort(indexes, new AlphanumericComparator()); // Sort the indexes numerically

            LinkedHashMap<String, ArrayList<String>> pageData = (LinkedHashMap<String, ArrayList<String>>) data.get(page);
            for (String index : indexes) {
                ArrayList<String> rowData = pageData.get(index);
                System.out.println("Row " + index + ": " + rowData);
            }
            System.out.println();
        }
    }


    public static class AlphanumericComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            String[] parts1 = s1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            String[] parts2 = s2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            int length = Math.min(parts1.length, parts2.length);
            for (int i = 0; i < length; i++) {
                if (Character.isDigit(parts1[i].charAt(0)) && Character.isDigit(parts2[i].charAt(0))) {
                    int num1 = Integer.parseInt(parts1[i]);
                    int num2 = Integer.parseInt(parts2[i]);
                    int compareResult = Integer.compare(num1, num2);
                    if (compareResult != 0) {
                        return compareResult;
                    }
                } else {
                    int compareResult = parts1[i].compareTo(parts2[i]);
                    if (compareResult != 0) {
                        return compareResult;
                    }
                }
            }

            return Integer.compare(parts1.length, parts2.length);
        }
    }


}
