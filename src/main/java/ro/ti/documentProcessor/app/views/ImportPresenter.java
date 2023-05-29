package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
import ro.ti.documentProcessor.app.Data;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class ImportPresenter {

    @FXML
    private View importView;

    @FXML
    private Button pickFileBtn;

    @FXML
    private TextArea pathText;

    @FXML
    VBox dataPreviewBox;
    @FXML
    VBox fileBox;

    TabPane tabs;

    private final TableView<Data> table = new TableView<>();
    private final ObservableList<Data> tvObservableList = FXCollections.observableArrayList();


    public void initialize() {
        System.gc();
        DocumentProcessorGluonApplication.setImportPresenter(this);
        importView.setShowTransitionFactory(BounceInRightTransition::new);
        importView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = AppManager.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        AppManager.getInstance().getDrawer().open()));
                appBar.setTitleText("Import");
            }
        });

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        table.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.gc();
            Data data = tvObservableList.get(newValue.intValue());
            HashMap file =  DocumentProcessorGluonApplication.getController().readFromFile(data.getPath(),data.getExtension());
            Platform.runLater(() -> setTabs(file,data.getExtension()));
        });

        tvObservableList.addAll(
                new Data("File a","./File a","xls",new Timestamp(System.currentTimeMillis())),
                new Data("File b","./gss","xlsx",new Timestamp(System.currentTimeMillis()))
        );


        TableColumn<Data, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setStyle("-fx-font-family:\"Helvetica neue regular\"");
        TableColumn<Data, String> colExtension = new TableColumn<>("Extension");
        colExtension.setCellValueFactory(new PropertyValueFactory<>("extension"));
        TableColumn<Data, Timestamp> colLastModified = new TableColumn<>("Last modified");
        colLastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        TableColumn<Data, Void> colClientName = new TableColumn<>("Client Name");



        colClientName.setStyle("-fx-text-inner-color:red");
        Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {
                    private final com.gluonhq.charm.glisten.control.TextField clientName = new com.gluonhq.charm.glisten.control.TextField();

                    {
                        clientName.textProperty().addListener(new ChangeListener<String>() {
                            @Override
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                Data data = getTableView().getItems().get(getIndex());
                                data.setClientName(newValue);
                            }
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(clientName);
                        }
                    }
                };
                return cell;
            }
        };
        colClientName.setCellFactory(cellFactory);
        table.getColumns().addAll(colName,colClientName,colExtension,colLastModified );
        TableColumn<Data, Void> colBtn = new TableColumn("Actions");
        cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

                    private final HBox btns = new HBox();

                    {
                        btns.setSpacing(5);
                        btns.setAlignment(Pos.CENTER_LEFT);
                        //edit btn
                        Button editBtn = new Button();
                        editBtn.setGraphic(MaterialDesignIcon.EDIT.graphic());
                        editBtn.setOnAction((ActionEvent event)->{
                                Data data = getTableView().getItems().get(getIndex());
                                DocumentProcessorGluonApplication.getController().openFile(data.getPath());
                        });
                        //save btn
                        Button saveBtn = new Button();
                        saveBtn.setGraphic(MaterialDesignIcon.SAVE.graphic());
                        saveBtn.setOnAction((ActionEvent event)->{
                            //process save
                            Data data = getTableView().getItems().get(getIndex());
                            // erase it from current list
                            tvObservableList.remove(getTableView().getItems().get(getIndex()));
                        });
                        //erase btn
                        Button deleteBtn = new Button();
                        deleteBtn.setGraphic(MaterialDesignIcon.DELETE.graphic());
                        deleteBtn.setOnAction((ActionEvent event)->{
                            tabs = null;
                            tvObservableList.remove(getTableView().getItems().get(getIndex()));
                        });
                        btns.getChildren().addAll(editBtn,saveBtn,deleteBtn);

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btns);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
        table.setItems(tvObservableList);
        fileBox.getChildren().add(table);

        }

    public void pickFile(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Import files");
        FileChooser.ExtensionFilter xlsFilter =new FileChooser.ExtensionFilter("Old excel files (*.xls)", "*.xls");
        FileChooser.ExtensionFilter xlsxFilter =new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter rtfFilter =new FileChooser.ExtensionFilter("Rich Text Format files (*.rtf)", "*.rtf");
        FileChooser.ExtensionFilter pdfFilter =new FileChooser.ExtensionFilter("Pdf files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(xlsxFilter);
        fileChooser.getExtensionFilters().add(xlsFilter);
        fileChooser.getExtensionFilters().add(rtfFilter);
        fileChooser.getExtensionFilters().add(pdfFilter);
        File file;

        if((file= fileChooser.showOpenDialog(theStage))!=null){
            this.pathText.setText(file.getAbsolutePath());
            String extension = file.getName().substring(file.getName().indexOf('.')+1);
            switch (extension){
                case "xls":
                case "xlsx":
                    tvObservableList.add(new Data(file.getName().substring(0,file.getName().indexOf(".")),file.getAbsolutePath(),extension,new Timestamp(file.lastModified())));
                    break;
                case "rtf":
                    pathText.setText("Working on the .rtf case");
                    tvObservableList.add(new Data(file.getName().substring(0,file.getName().indexOf(".")),file.getAbsolutePath(),extension,new Timestamp(file.lastModified())));
                    break;
                case "pdf":
                    tvObservableList.add(new Data(file.getName().substring(0,file.getName().indexOf(".")),file.getAbsolutePath(),extension,new Timestamp(file.lastModified())));
                    pathText.setText("Working on the .pdf case");
                    break;
                default:
                    pathText.setText("File type not supported");
                    break;
            }
        }


    }


    private void setTabs(HashMap<String,ArrayList<String>> file, String extension) {
        switch (extension)
        {
            case "xls":
            case "xlsx":
                setXlsTabs(file);
                break;
            case "rtf":
                break;
            case "pdf":
                break;
            default:
                return;

        }

    }

    private void setXlsTabs(HashMap<String, ArrayList<String>> file) {
        tabs= new TabPane();
        ArrayList<String> pages  = file.get("Pages");
        for (String page:
                pages) {
            int rowCount = file.get(page+"Indexes").size();
            int columnCount = file.get(page+"Columns").size();
            GridBase grid = new GridBase(rowCount, columnCount);
            ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
            for (int row = 0; row < grid.getRowCount(); ++row) {
                final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
                for (int column = 0; column < grid.getColumnCount(); ++column) {
                    ArrayList<String> line  = file.get(page+row);
                    list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,file.get(page+row).get(column)));
                }
                rows.add(list);
            }
            grid.setRows(rows);

            SpreadsheetView spv = new SpreadsheetView(grid);
            ScrollPane scrllablePage = new ScrollPane();
            scrllablePage.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrllablePage.setPrefHeight(Region.USE_COMPUTED_SIZE);
            scrllablePage.setPrefWidth(Region.USE_COMPUTED_SIZE);
            scrllablePage.setMaxHeight(Region.USE_COMPUTED_SIZE);
            scrllablePage.setMaxWidth(Region.USE_COMPUTED_SIZE);
            scrllablePage.setContent(spv);
            tabs.getTabs().add(new Tab(page,spv));
        }
        if (dataPreviewBox.getChildren().get(1) == null) dataPreviewBox.getChildren().add(tabs);
        else {
            dataPreviewBox.getChildren().remove(1);
            dataPreviewBox.getChildren().add(tabs);
        }
    }

    public void clickSound(MouseEvent mouseEvent) {
    }

    public void saveAll(ActionEvent event){
        for (Data data:
                tvObservableList) {
            System.out.println(data.toString());
        }
    }

    public void reloadFile(String path, Timestamp time){
        tabs =  null;
        Data dataOld =tvObservableList.filtered(data -> data.getPath().equals(path)).get(0) ;
        tvObservableList.remove(dataOld);
        Data dataNew =new Data(dataOld);
        dataNew.setLastModified(time);
        tvObservableList.add(dataNew);
    }
}
