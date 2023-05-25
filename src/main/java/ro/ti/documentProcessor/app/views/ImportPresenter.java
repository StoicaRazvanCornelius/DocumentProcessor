package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    VBox fileBox;

    volatile TabPane tabs;

    private final TableView<Data> table = new TableView<>();
    private final ObservableList<Data> tvObservableList = FXCollections.observableArrayList();


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

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tvObservableList.addAll(new Data(1, "app1"),
                new Data(2, "app2"),
                new Data(3, "app3"),
                new Data(4, "app4"),
                new Data(5, "app5"));

        TableColumn<Data, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Data, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(colId, colName);

        TableColumn<Data, Void> colBtn = new TableColumn("Button Column");


        Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

                    private final Button btn = new Button("Action");

                    {
                        btn.setGraphic(MaterialDesignIcon.EDIT.graphic());
                        btn.setOnAction((ActionEvent event) -> {
                            Data data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
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

        /*
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

         */
    }

    public void pickFile(ActionEvent actionEvent) throws IOException {
        Node source = (Node) actionEvent.getSource();
        Window theStage = source.getScene().getWindow();
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Import files");
        File file;
        if((file= fileChooser.showOpenDialog(theStage))!=null){
            this.pathText.setText(file.getAbsolutePath());


            //changeInterfaceFilePicked();

            //showExcelFileContent(new HashMap<>());
            //DocumentProcessorGluonApplication.getController().readFromFile(file.getAbsolutePath());
            //HashMap<String, ArrayList<String>> fileFromController = new HashMap<>();


            //showExcelFileContent(fileFromController);
           // Platform.runLater(() -> showExcelFileContent(fileFromController));
        }


    }

    private void showExcelFileContent(HashMap<String, ArrayList<String>> file) {


        //Original
        contentBox.setVisible(true);
        tabs= new TabPane();
        tabs.setSide(Side.BOTTOM);
        tabs.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tabs.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentBox.getChildren().add(tabs);

        System.out.println(new Timestamp(System.currentTimeMillis()));

        int rowCount = 10;
        int columnCount = 180;
        GridBase grid = new GridBase(rowCount, columnCount);

        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < grid.getRowCount(); ++row) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < grid.getColumnCount(); ++column) {
                list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
            }
            rows.add(list);
        }
        grid.setRows(rows);

        SpreadsheetView spv = new SpreadsheetView(grid);

        System.out.println(new Timestamp(System.currentTimeMillis()));

        ScrollPane page = new ScrollPane();
        page.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        page.setPrefHeight(Region.USE_COMPUTED_SIZE);
        page.setPrefWidth(Region.USE_COMPUTED_SIZE);
        page.setMaxHeight(Region.USE_COMPUTED_SIZE);
        page.setMaxWidth(Region.USE_COMPUTED_SIZE);

        page.setContent(spv);
        tabs.getTabs().add(new Tab("pageName",page));

    }

    private void setTabs(HashMap<String,ArrayList<String>> file, ArrayList<String> pagesName) {
        for (String pageName:
             pagesName) {
            int rowCount = 110;
            int columnCount = 180;
            GridBase grid = new GridBase(rowCount, columnCount);

            ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
            for (int row = 0; row < grid.getRowCount(); ++row) {
                final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
                for (int column = 0; column < grid.getColumnCount(); ++column) {
                    list.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"value"));
                }
                rows.add(list);
            }
            grid.setRows(rows);

            SpreadsheetView spv = new SpreadsheetView(grid);

            /*
            TableView<RowData> tableView = new TableView<>();
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);


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
            ArrayList<RowData> s = new ArrayList<>();

            for (String index:
                    rowIndexes) {
                ArrayList<String> rowDataList = file.get(pageName+index);

                //data.add(new RowData(rowDataList));
               /*Thread rowThread = new Thread(new Runnable() {
                   @Override
                   public void run() {
                   }
               });
               rowThread.start();
            }


            tableView.setItems(data);
            tableView.getColumns().addAll(columns);
            */
            ScrollPane page = new ScrollPane();
            page.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            page.setPrefHeight(Region.USE_COMPUTED_SIZE);
            page.setPrefWidth(Region.USE_COMPUTED_SIZE);
            page.setMaxHeight(Region.USE_COMPUTED_SIZE);
            page.setMaxWidth(Region.USE_COMPUTED_SIZE);

            page.setContent(spv);
            tabs.getTabs().add(new Tab(pageName,page));
            //ScrollPane s = new ScrollPane();
            //Tab tab = copyTab(null, page,tableView);
            //s.setContent(tableView);

            System.out.println(new Timestamp(System.currentTimeMillis()));
            //tabs.getTabs().add(tab);
            // tabs.getTabs().add(tab);
            // tabs.getTabs().add(tab);
            //  tabs.getTabs().add(tab);
            //  tabs.getTabs().add(tab);
            //  tabs.getTabs().add(tab);

            System.out.println(new Timestamp(System.currentTimeMillis()));


        }
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
        System.out.println("sddddddddd");
        DocumentProcessorGluonApplication.getController().openFile(pathText.getText());
        DocumentProcessorGluonApplication.getController().testController();
        // directory save
        // DirectoryChooser directoryChooser = new DirectoryChooser();
        // directoryChooser.setTitle("Save in");

    }

    public void openInExcel(ActionEvent actionEvent) {
        System.out.println("asdffff");
        DocumentProcessorGluonApplication.getController().openFile(pathText.getText());
    }
    public void cancel(ActionEvent actionEvent) {
        contentBox.setVisible(false);
        interactableButtons.getChildren().remove(1,interactableButtons.getChildren().size());
    }

    public void setMainRefrence(DocumentProcessorGluonApplication documentProcessorGluonApplication) {
        documentProcessorGluonApplication.getController().testController();
    }

    public Tab copyTab(Tab tab,ScrollPane page,TableView tableView){
        ScrollPane pageNew = new ScrollPane();
        page.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        page.setPrefHeight(Region.USE_COMPUTED_SIZE);
        page.setPrefWidth(Region.USE_COMPUTED_SIZE);
        page.setMaxHeight(Region.USE_COMPUTED_SIZE);
        page.setMaxWidth(Region.USE_COMPUTED_SIZE);

        TableView<Data> tableViewNew = new TableView<>();
        tableViewNew.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        // Create and configure the columns
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            TableColumn<Data, String> column = (TableColumn<Data, String>) tableView.getColumns().get(i);
        }

        page.setContent(tableView);
        return new Tab("22",pageNew);

    }


    public void sss(HashMap<String,ArrayList<String>> file){




        //Make file
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
            for (int i=1;i<100;i++){
                columnsName.add(String.valueOf(i));
            }
            //columnsName.add("A");
            //columnsName.add("B");
            //columnsName.add("C");
            //columnsName.add("D");
            //columnsName.add("E");
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
    }
}
