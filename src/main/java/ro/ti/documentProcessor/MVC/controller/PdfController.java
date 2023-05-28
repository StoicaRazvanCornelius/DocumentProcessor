package ro.ti.documentProcessor.MVC.controller;

import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.Interfaces.View;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class PdfController implements ControllerFile {


    @Override
    public HashMap readFromFile(String path) throws IOException {
        return null;
    }

    @Override
    public void writeToFile(String path, String name) throws IOException {

    }

}
