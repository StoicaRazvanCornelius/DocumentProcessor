package ro.ti.documentProcessor;

import ro.ti.documentProcessor.MVC.Interfaces.*;
import ro.ti.documentProcessor.MVC.controller.XlsController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            String configPropertiesPath = DocumentProcessorGluonApplication.class.getResource("config.properties").getPath();
            FileInputStream propsInput = new FileInputStream(configPropertiesPath);
            properties = new Properties();
            properties.load(propsInput);
        }catch (FileNotFoundException e){
            //run default interaface and create new config file
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }

        controller= new XlsController();
        controller.setProperties(properties);
        controller.pingDatabase();
        /*
        model = new ModelXlsProcessor();
        DocumentProcessorGluonApplication.setProperties(properties);
        View view = new DocumentProcessorGluonApplication();
        controller.setModel(model);
        controller.setView(view);
        model.setController(controller);
        view.setController(controller);
        ((DocumentProcessorGluonApplication) view).main(new String[]{"E:/JAVAFX/openjfx-20_windows-x64_bin-sdk/javafx-sdk-20/lib"});
        */
    }

}
