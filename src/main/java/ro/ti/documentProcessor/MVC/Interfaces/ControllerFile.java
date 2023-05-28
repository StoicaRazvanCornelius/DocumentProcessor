package ro.ti.documentProcessor.MVC.Interfaces;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public abstract interface ControllerFile {
    public HashMap readFromFile(String path) throws IOException;
    public void writeToFile(String path,String name) throws IOException;

}
