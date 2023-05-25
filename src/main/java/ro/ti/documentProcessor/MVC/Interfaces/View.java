package ro.ti.documentProcessor.MVC.Interfaces;

import java.sql.Timestamp;

public interface View {
    public View getView() throws InterruptedException;
    public void testView();

    void updateFile(String path, Timestamp time);
    //public static void setController(Controller controller);
}
