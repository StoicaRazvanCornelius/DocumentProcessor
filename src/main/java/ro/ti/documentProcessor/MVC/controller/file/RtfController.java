package ro.ti.documentProcessor.MVC.controller.file;

import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RtfController implements ControllerFile {

    private static volatile RtfController instance;
    private RtfController(){

    }

    public static  RtfController getInstance(){
        return instance != null ? instance : (instance= new RtfController());
    }
    @Override
    public HashMap<String, ArrayList<String>> readFromFile(String path) throws IOException {
        return null;
    }

    @Override
    public void writeToFile(String path, String name) throws IOException {

    }

}
