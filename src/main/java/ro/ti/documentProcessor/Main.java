package ro.ti.documentProcessor;

import javafx.application.Application;
import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.View;
import ro.ti.documentProcessor.MVC.controller.XlsController;

public class Main {
    public static void main(String[] args) throws InterruptedException {
       Controller controller= XlsController.getController();
       DocumentProcessorGluonApplication.setController(controller);
       Application.launch(DocumentProcessorGluonApplication.class,new String[]{"E:/JAVAFX/openjfx-20_windows-x64_bin-sdk/javafx-sdk-20/lib"});
    }
}
