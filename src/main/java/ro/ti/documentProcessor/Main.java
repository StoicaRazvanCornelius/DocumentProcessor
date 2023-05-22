package ro.ti.documentProcessor;

import javafx.application.Application;
import ro.ti.documentProcessor.MVC.Interfaces.*;
import ro.ti.documentProcessor.MVC.controller.XlsController;
import ro.ti.documentProcessor.MVC.model.ModelXlsProcessor;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    static volatile Controller controller;
    static volatile View view;
    static volatile Model model;


    public static void main(String[] args) throws InterruptedException {

        controller= new XlsController();
        model = new ModelXlsProcessor();
        DocumentProcessorGluonApplication myApp = new DocumentProcessorGluonApplication();
        controller.setModel(model);
        controller.setView(myApp);
        model.setController(controller);
        myApp.setController(controller);
        myApp.main(new String[]{"E:/JAVAFX/openjfx-20_windows-x64_bin-sdk/javafx-sdk-20/lib"});

    }

}
