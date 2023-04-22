package ro.ti.documentProcessor.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ImportView {

    public View getView() {
        try {
            View view = FXMLLoader.load(ImportView.class.getResource("import.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
