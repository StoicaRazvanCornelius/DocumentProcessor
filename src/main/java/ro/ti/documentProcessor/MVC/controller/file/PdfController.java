package ro.ti.documentProcessor.MVC.controller.file;

import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;

import java.io.IOException;
import java.util.HashMap;

public class PdfController implements ControllerFile {
    private static volatile PdfController instance;

    private PdfController(){

    }

    public static PdfController getInstance(){
        return instance != null ? instance :(instance = new PdfController());
    }
    @Override
    public HashMap readFromFile(String path) throws IOException {
        return null;
    }

    @Override
    public void writeToFile(String path, String name) throws IOException {

    }

}
