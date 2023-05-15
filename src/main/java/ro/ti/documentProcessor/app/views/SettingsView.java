package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.mvc.View;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

public class SettingsView {
    
    public View getView() {
        try {
            View view = FXMLLoader.load(SettingsView.class.getResource("settings.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
