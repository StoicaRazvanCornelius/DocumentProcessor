package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;

public class MainPresenter {

    @FXML
    private View main;
    @FXML
    private VBox vBoxGauges;

    Clock clock = new Clock();

    @FXML
    private Label label;

    public void initialize() {
        main.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = AppManager.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        AppManager.getInstance().getDrawer().open()));
                appBar.setTitleText("Main");
                appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> 
                        System.out.println("Search")));

            }
        });

        clock=  ClockBuilder.create()
                .skinType(Clock.ClockSkinType.ROUND_LCD)
                .hourColor(Color.rgb(38, 166, 154))
                .minuteColor(Color.rgb(77, 182, 172))
                .secondColor(Color.rgb(128, 203, 196))
                .textColor(Color.rgb(128, 203, 196))
                .dateColor(Color.rgb(128, 203, 196))
                .running(true)
                .build();

        vBoxGauges.getChildren().add(clock);

    }
    
    @FXML
    void buttonClick() {
        //DocumentProcessorGluonApplication.clickSound();
        label.setText("Hello!");
    }

}
