package ro.ti.documentProcessor.MVC.controller;

import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
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
import java.io.*;
import java.net.URL;
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
        int clientId =database.checkIfClientExist(clientName);
        if (clientId!=-1) {
            boolean uploadCheck =  endpoint.uploadFile(path, name, type, lastModified, String.valueOf(clientId), clientName);
            database.makeNewDocumentEntry(clientName,type,name,lastModified);
            return uploadCheck;
        }
        else return false;

        //database.makeNewDocumentEntry();
    }

    @Override
    public int getClientIdGivenClientName(String name) {
        return database.checkIfClientExist(name);
    }

    @Override
    public void downloadFile(String clientId, String fileName, String fileExtension) {
        endpoint.downloadFile("C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\",clientId,fileName,fileExtension);
    }

    @Override
    public void reloadFile(String path, Timestamp time) {
        view.reloadFile(path,time);
    }

    @Override
    public void pingDatabase() {
        database.checkifConnectionIsValid();
    }

    @Override
    public void storeConfiguration() {
        //System.out.println(DocumentProcessorGluonApplication.class.getResource("config.properties").getPath());
        File outFile = new File(DocumentProcessorGluonApplication.class.getResource("config.properties").getPath());
        try (OutputStream os = new FileOutputStream(outFile)) {
            properties.store(os, "sddd");
        } catch (IOException e) {

        }


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

