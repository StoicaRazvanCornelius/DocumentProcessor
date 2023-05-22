package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
import ro.ti.documentProcessor.app.Cell;
import ro.ti.documentProcessor.app.RowData;

import java.io.File;
import java.io.IOException;
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

    public void pickFile(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open files");
        File file;
        if((file= fileChooser.showOpenDialog(theStage))!=null){
            changeInterfaceFilePicked();
            //DocumentProcessorGluonApplication.getController().readFromFile(file.getAbsolutePath());
            HashMap<String, ArrayList<String>> fileFromController = new HashMap<>();
            ArrayList<String> pagesName = new ArrayList<>();
            pagesName.add("0");
            pagesName.add("1");
            pagesName.add("2");
            pagesName.add("3");
            pagesName.add("4");
            fileFromController.put("pagesName",pagesName);


            showExcelFileContent(fileFromController);
            this.pathText.setText(file.getAbsolutePath());

        }


    }

    private void showExcelFileContent(HashMap<String, ArrayList<String>> file) {
        contentBox.setVisible(true);
        tabs= new TabPane();
        tabs.setSide(Side.BOTTOM);
        contentBox.getChildren().add(tabs);

        ArrayList<String> pagesName= new ArrayList<>();
        pagesName.add("0");
        pagesName.add("1");
        pagesName.add("2");
        pagesName.add("3");
        file.put("pagesNames",pagesName);

        for (String pageName:pagesName){
            ArrayList<String> indexesArray = new ArrayList<>();
            for (int i =1;i<100;i++){
                indexesArray.add(String.valueOf(i));
            }
            file.put(pageName+"Indexes",indexesArray);
            ArrayList<String> columnsName = new ArrayList<>();
            columnsName.add("A");
            columnsName.add("B");
            columnsName.add("C");
            columnsName.add("D");
            columnsName.add("E");
            file.put(pageName+"Columns",columnsName);
            for (int i =1;i<100;i++){
                ArrayList<String> row =new ArrayList();
                for(String column: file.get(pageName+"Columns"))
                {
                    row.add("0");
                }
                file.put(pageName+i,row);
            }

        }
        setTabs(file,file.get("pagesNames"));

    }

    private void setTabs(HashMap<String,ArrayList<String>> file, ArrayList<String> pagesName) {
        for (String pageName:
             pagesName) {
            TableView<RowData> tableView = new TableView<>();
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            // Create the ArrayList<String> containing your data

            // Create the TableView and its columns
            ArrayList<String> columnNames = file.get(pageName+"Columns");
            TableColumn<RowData, String>[] columns = new TableColumn[columnNames.size()];


            // Create and configure the columns
            for (int i = 0; i < columnNames.size(); i++) {
                final int columnIndex = i;
                TableColumn<RowData, String> column = new TableColumn<>(columnNames.get(columnIndex));
                column.setCellValueFactory(cellData -> cellData.getValue().getCells().get(columnIndex).getValue());
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                columns[i] = column;
            }


            ObservableList<RowData> data=FXCollections.observableArrayList();

            ArrayList<String> rowIndexes = file.get(pageName+"Indexes");
            for (String index:
                 rowIndexes) {
                ArrayList<String> rowDataList = file.get(pageName+index);
                data.add(new RowData(rowDataList));
            }

            tableView.setItems(data);
            tableView.getColumns().addAll(columns);

            ScrollPane page = new ScrollPane();
            page.setContent(tableView);
            tabs.getTabs().add(new Tab(pageName,page));

        }
    }

    private void setColumnValues(String tableName, ArrayList<String> columnsName) {

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

    public void saveFile(ActionEvent actionEvent)
    {
        DocumentProcessorGluonApplication.getController().testController();
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

    public void setMainRefrence(DocumentProcessorGluonApplication documentProcessorGluonApplication) {
        documentProcessorGluonApplication.getController().testController();
    }
}
