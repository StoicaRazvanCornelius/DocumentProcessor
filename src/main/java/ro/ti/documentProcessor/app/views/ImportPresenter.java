package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
import ro.ti.documentProcessor.MVC.Interfaces.Controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImportPresenter {

    @FXML
    private View importView;

    @FXML
    private Button pickFileBtn;

    @FXML
    private TextArea pathText;

    Button openExcelBtn;
    Button cancelBtn;

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

    public void pickFile(ActionEvent actionEvent){
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open files");
        File file;
        if((file= fileChooser.showOpenDialog(theStage))!=null){
            changeInterface();



        }
        this.pathText.setText(file.getAbsolutePath());


    }

    private void changeInterface() {
        openExcelBtn = new Button();
        openExcelBtn.setText("Open in Excel");;
        openExcelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openInExcel(event);
            }
        });
        interactableButtons.getChildren().add(openExcelBtn);

        cancelBtn = new Button();
        cancelBtn.setText("Cancel");;
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancel(event);
            }
        });
        interactableButtons.getChildren().add(cancelBtn);
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
        //org.apache.commons.io.FileUtils.isFileNewer();
        try {
            DocumentProcessorGluonApplication.getController().openFile(pathText.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void cancel(ActionEvent actionEvent) {

    }
}
