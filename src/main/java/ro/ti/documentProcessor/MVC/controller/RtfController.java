package ro.ti.documentProcessor.MVC.controller;

import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.Interfaces.View;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class RtfController implements Controller {
    @Override
    public void openFile(String path) {

    }

    @Override
    public HashMap<String, ArrayList<String>> readFromFile(String path) throws IOException {
        return null;
    }

    @Override
    public void writeToFile(String path, String name) throws IOException {

    }

    @Override
    public void setView(View view) {

    }

    @Override
    public void setModel(Model model) {

    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public void testMVC() {

    }

    @Override
    public void testController() {

    }

    @Override
    public void reloadFile(String path, Timestamp dateTimeUpdated) {

    }

    @Override
    public void pingDatabase() {

    }
}
