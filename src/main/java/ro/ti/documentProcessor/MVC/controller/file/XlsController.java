package ro.ti.documentProcessor.MVC.controller.file;

import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.model.ModelXlsProcessor;

import java.io.File;
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

    }

}
