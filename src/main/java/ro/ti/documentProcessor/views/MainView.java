package ro.ti.documentProcessor.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class MainView {

    public View getView() {
        try {
            View view = FXMLLoader.load(MainView.class.getResource("main.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
