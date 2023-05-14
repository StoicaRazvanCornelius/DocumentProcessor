package ro.ti.documentProcessor.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.sun.javafx.scene.control.skin.Utils;
import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImportPresenter {

    @FXML
    private View importView;

    @FXML
    private Button openFileBtn;

    @FXML
    private AutoCompleteTextField pathText;

    Button openExcel;

    @FXML
    private HBox interactableButtons;
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
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open files");
        File file;
        if((file= fileChooser.showOpenDialog(theStage))!=null){

            openExcel = new Button();
            openExcel.setText("Open in Excel");;
            openExcel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    openInExcel(event);
                }
            });
            interactableButtons.getChildren().add(openExcel);
        }
        this.pathText.setText(file.getAbsolutePath());


    }
    // directory save
    // DirectoryChooser directoryChooser = new DirectoryChooser();
    // directoryChooser.setTitle("Save in");

    //file open + path

    public void clickSound(MouseEvent mouseEvent) {
    }

    public void saveFile(ActionEvent actionEvent) {
    }

    public void openInExcel(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().open(new File(pathText.getText()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
    }
}
