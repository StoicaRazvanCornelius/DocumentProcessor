package ro.ti.documentProcessor.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ExportView {

    public View getView() {
        try {
            View view = FXMLLoader.load(ExportView.class.getResource("export.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
