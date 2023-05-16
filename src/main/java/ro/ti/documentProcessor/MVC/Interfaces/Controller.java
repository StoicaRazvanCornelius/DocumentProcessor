package ro.ti.documentProcessor.MVC.Interfaces;

import java.io.IOException;

public interface Controller {
    public void openFile(String path) throws IOException;
    public void readFromFile(String path) throws IOException;
    public void writeToFile(String path,String name) throws IOException;
}
