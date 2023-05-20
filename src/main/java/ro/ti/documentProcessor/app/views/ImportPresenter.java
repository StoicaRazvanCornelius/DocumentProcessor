package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
import ro.ti.documentProcessor.MVC.Interfaces.Cell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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

    TabPane tabs;
    HashMap<String,TableView> pages;

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
        pages = new HashMap<>();
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
        tabs.setSide(Side.BOTTOM);
        ArrayList<String> names= new ArrayList<>();
        names.add("0");
        names.add("1");
        names.add("2");
        names.add("3");
        setTabs(names);
        contentBox.getChildren().add(tabs);

    }

    private void setTabs(ArrayList<String> pagesName) {
        for (String name :
                pagesName) {
            ScrollPane page = new ScrollPane();
            TableView tableView= new TableView<String>();
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            page.setContent(tableView);
            tabs.getTabs().add(new Tab(name,page));
            pages.put(name,tableView);

            ArrayList<String> columnsName= new ArrayList<>();
            columnsName.add("A");
            columnsName.add("B");
            columnsName.add("C");
            columnsName.add("D");
            columnsName.add("E");
            String tableName = name;
            setColumnValues(tableName, columnsName);
        }
    }

    private void setColumnValues(String tableName, ArrayList<String> columnsName) {
        for (String columnName:
             columnsName) {
            pages.get(tableName).getColumns().add(new TableColumn<>(columnName));
        }
    }

    private void setCellValues() {

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
