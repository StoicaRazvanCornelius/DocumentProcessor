package ro.ti.documentProcessor.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ImportPresenter {

    @FXML
    private View importView;

    @FXML
    private Button openFileBtn;


    public void initialize() {
        importView.setShowTransitionFactory(BounceInRightTransition::new);

        importView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = AppManager.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        AppManager.getInstance().getDrawer().open()));
                appBar.setTitleText("Import");
            }
        });
    }

    public void openFile(ActionEvent actionEvent){
        openFileBtn.setText("asd");
    }

    public void clickSound(MouseEvent mouseEvent) {
    }

    public void saveFile(ActionEvent actionEvent) {
    }
}
