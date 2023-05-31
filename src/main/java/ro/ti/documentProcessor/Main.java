package ro.ti.documentProcessor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ti.documentProcessor.MVC.Interfaces.*;
import ro.ti.documentProcessor.MVC.controller.rest.Endpoint;
import ro.ti.documentProcessor.MVC.model.ModelXlsProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    static Controller controller;
    View view;
    static Model model;

    static Properties properties;


    public static void main(String[] args) {
        //properties file. Settings remain the same even if you close the app.
        try {
            //String configPropertiesPath = DocumentProcessorGluonApplication.class.getResource("config.properties").getPath();
            InputStream propsInput = DocumentProcessorGluonApplication.class.getResourceAsStream("/ro/ti/documentProcessor/config.properties");
            properties = new Properties();
            properties.load(propsInput);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller=  new ro.ti.documentProcessor.MVC.controller.Controller();
        controller.setProperties(properties);


        model = new ModelXlsProcessor();
        DocumentProcessorGluonApplication.setProperties(properties);
        View view = new DocumentProcessorGluonApplication();
        controller.setModel(model);
        controller.setView(view);
        model.setController(controller);
        view.setController(controller);
        ((DocumentProcessorGluonApplication) view).main(new String[]{"E:/JAVAFX/openjfx-20_windows-x64_bin-sdk/javafx-sdk-20/lib"});

    }

}
