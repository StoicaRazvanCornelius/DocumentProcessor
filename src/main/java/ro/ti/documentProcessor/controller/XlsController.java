package ro.ti.documentProcessor.controller;

public class XlsController {
    private static XlsController controller;
    public static XlsController getController(){
        return (controller == null) ? (new XlsController()):(controller);
    }
}
