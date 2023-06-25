package ro.ti.documentProcessor.MVC.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.Interfaces.View;
import ro.ti.documentProcessor.MVC.controller.database.Database;
import ro.ti.documentProcessor.MVC.controller.file.PdfController;
import ro.ti.documentProcessor.MVC.controller.file.RtfController;
import ro.ti.documentProcessor.MVC.controller.file.XlsController;
import ro.ti.documentProcessor.MVC.controller.rest.Endpoint;
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

    Endpoint endpoint = new Endpoint();

    Properties properties;
    @Override
    public void openFile(String path)  {
        try {
            Desktop.getDesktop().open(new File(path));
            FileChecker.checkForNewerVersion(this,path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public HashMap readFromFile(String path, String extension) {
        ControllerFile controllerFile = getFileController(extension);
        try {
            HashMap file  =  controllerFile.readFromFile(path);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public HashMap readDatabaseEntries(String fileName, String clientName, String fileType, String startDateTime, String endDateTime) {
        return database.getEntriesFor(fileName, clientName, fileType, startDateTime,  endDateTime);
    }

    @Override
    public boolean insertNewFile(String path,String name, String type, String lastModified, String clientName) {
        path = "C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\Book1.xlsx"; // Replace with the actual file path
        name = "Book1";
        type = "xlsx";
        clientName = "John_Doe"; // Replace space with %20

        return endpoint.uploadFile(path,name,type,lastModified,clientName,lastModified);
    }

    @Override
    public void reloadFile(String path, Timestamp time) {
        view.reloadFile(path,time);
    }

    @Override
    public void pingDatabase() {
        database.checkifConnectionIsValid();
    }

    private ControllerFile getFileController(String extension) {
        switch (extension){
            case "xls":
            case "xlsx":
                return XlsController.getInstance();
            case "rtf":
                return RtfController.getInstance();
            case "pdf":
                return PdfController.getInstance();
            default:
                return null;
        }
    }
}

