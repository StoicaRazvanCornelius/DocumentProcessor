package ro.ti.documentProcessor.MVC.Interfaces;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public interface Controller {
    public void openFile(String path) ;
    public HashMap readFromFile(String path,String extension) throws IOException;
    public void writeToFile(String path,String name,String extension,HashMap content) throws IOException;
    public void setView(View view);
    public void setModel(Model model);
    public void setProperties(Properties properties);
    public void testMVC();
    public void testController();
    void reloadFile(String path, Timestamp dateTimeUpdated);
    void pingDatabase();
}
