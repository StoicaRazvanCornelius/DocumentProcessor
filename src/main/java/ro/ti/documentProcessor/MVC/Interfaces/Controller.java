package ro.ti.documentProcessor.MVC.Interfaces;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public interface Controller {
    public void openFile(String path) ;
    public HashMap<String, ArrayList<String>> readFromFile(String path) throws IOException;
    public void writeToFile(String path,String name) throws IOException;
    public void setView(View view);
    public void setModel(Model model);

    public void testMVC();

    public void testController();
    void reloadFile(String path, Timestamp dateTimeUpdated);
}
