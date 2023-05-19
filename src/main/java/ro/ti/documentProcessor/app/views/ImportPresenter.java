package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.math3.geometry.partitioning.Side;
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
    private Button saveFileBtn;

    @FXML
    private TextArea pathText;

    @FXML
    HBox interactableButtons;

    Button openExcelBtn;

    Button cancelBtn;

    @FXML
    VBox contentBox;

    @FXML
    TabPane tabs;

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

        openExcelBtn = new Button();
        openExcelBtn.setText("Open in Excel");;
        openExcelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openInExcel(event);
            }
        });
        saveFileBtn = new Button();
        saveFileBtn.setText("Save");;
        openExcelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveFile(event);
            }
        });
        cancelBtn = new Button();
        cancelBtn.setText("Cancel");;
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancel(event);
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
            changeInterfaceFilePicked();
            showExcelFileContent();
            this.pathText.setText(file.getAbsolutePath());

        }


    }

    private void showExcelFileContent() {
        contentBox.setVisible(true);
        tabs= new TabPane();

        //needs to be Side Bottom
        tabs.sideProperty();
        ScrollPane scrollPage = new ScrollPane();
        TreeTableView t= new TreeTableView();
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        t.getColumns().add(new TreeTableColumn<>("C1"));
        scrollPage.setContent(t);
        //tabs.getTabs().add(new Tab("Page 0",new ScrollPane()));
        tabs.getTabs().add(new Tab("Page 0", scrollPage));
        contentBox.getChildren().add(tabs);

    }

    private void changeInterfaceFilePicked() {
        interactableButtons.getChildren().add(openExcelBtn);
        interactableButtons.getChildren().add(saveFileBtn);
        interactableButtons.getChildren().add(cancelBtn);
    }

    public void clickSound(MouseEvent mouseEvent) {
    }

    public void saveFile(ActionEvent actionEvent) {
        // directory save
        // DirectoryChooser directoryChooser = new DirectoryChooser();
        // directoryChooser.setTitle("Save in");

    }

    public void openInExcel(ActionEvent actionEvent) {
        DocumentProcessorGluonApplication.getController().openFile(pathText.getText());
    }
    public void cancel(ActionEvent actionEvent) {
        contentBox.setVisible(false);
        interactableButtons.getChildren().remove(1,interactableButtons.getChildren().size());
    }
}
