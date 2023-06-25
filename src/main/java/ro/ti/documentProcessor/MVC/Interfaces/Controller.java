package ro.ti.documentProcessor.MVC.Interfaces;

import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public interface Controller {
    public void openFile(String path) ;
    public HashMap readFromFile(String path,String extension);
    public void writeToFile(String path,String name,String extension,HashMap content) throws IOException;
    public void setView(View view);
    public void setModel(Model model);
    public void setProperties(Properties properties);
    public void testMVC();
    public void testController();
    public  HashMap readDatabaseEntries(String fileName, String clientName, String fileType, String startDateTime, String endDateTime);
    public  boolean insertNewFile(String path,String name, String type, String lastModified, String clientName);
    void reloadFile(String path, Timestamp dateTimeUpdated);
    void pingDatabase();

}
