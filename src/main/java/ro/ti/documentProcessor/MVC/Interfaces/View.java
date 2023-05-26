package ro.ti.documentProcessor.MVC.Interfaces;

import java.sql.Timestamp;
import java.util.Properties;

public abstract interface View {
    public View getView() throws InterruptedException;
    public void testView();

    static void setProperties(Properties properties) {

    }

    void updateFile(String path, Timestamp time);

    void setController(Controller controller);
    //public static void setController(Controller controller);
}
