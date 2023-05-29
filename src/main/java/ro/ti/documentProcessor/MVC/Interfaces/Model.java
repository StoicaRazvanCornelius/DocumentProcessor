package ro.ti.documentProcessor.MVC.Interfaces;

import java.io.File;
import java.util.HashMap;

public abstract interface Model {
    public void setController(Controller controller);
    public Model getModel();
    public HashMap parseFile(File file);
    void testModel();
}

