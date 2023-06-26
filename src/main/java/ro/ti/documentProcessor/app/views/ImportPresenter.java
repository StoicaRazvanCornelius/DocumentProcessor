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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
import ro.ti.documentProcessor.MVC.model.ModelXlsProcessor;
import ro.ti.documentProcessor.app.Data;
import ro.ti.documentProcessor.app.RowTablePreview;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

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
            //Data preview
            HashMap file =  DocumentProcessorGluonApplication.getController().readFromFile(data.getPath(),data.getExtension());
            Platform.runLater(() -> setTabs(file,data.getExtension()));
            System.out.println("Done with tables");
            //dataPreviewBox.getChildren().get(1).setVisible(true);
        });


        TableColumn<Data, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
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
                            if (DocumentProcessorGluonApplication.getController().insertNewFile(data.getPath(),data.getName(), data.getExtension(),String.valueOf(data.getLastModified()),data.getClientName())){
                                tvObservableList.remove(getTableView().getItems().get(getIndex()));
                                if (dataPreviewBox.getChildren().size()>=2) {
                                    dataPreviewBox.getChildren().remove(1);
                                }

                            }
                        });
                        //erase btn
                        Button deleteBtn = new Button();
                        deleteBtn.setGraphic(MaterialDesignIcon.DELETE.graphic());
                        deleteBtn.setOnAction((ActionEvent event)->{
                            tabs = null;
                            tvObservableList.remove(getTableView().getItems().get(getIndex()));
                            if (dataPreviewBox.getChildren().size()>=2) {
                                dataPreviewBox.getChildren().remove(1);
                            }
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
                    tvObservableList.add(new Data(file.getName().substring(0,file.getName().indexOf(".")),null,file.getAbsolutePath(),extension,new Timestamp(file.lastModified())));
                    break;
                case "rtf":
                    pathText.setText("Working on the .rtf case");
                    tvObservableList.add(new Data(file.getName().substring(0,file.getName().indexOf(".")),null,file.getAbsolutePath(),extension,new Timestamp(file.lastModified())));
                    break;
                case "pdf":
                    tvObservableList.add(new Data(file.getName().substring(0,file.getName().indexOf(".")),null,file.getAbsolutePath(),extension,new Timestamp(file.lastModified())));
                    pathText.setText("Working on the .pdf case");
                    break;
                default:
                    pathText.setText("File type not supported");
                    break;
            }
        }


    }


    private void setTabs(HashMap<String,Object> file, String extension) {
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

    private void setXlsTabs(HashMap<String, Object> file) {
        tabs = new TabPane();
        ArrayList<String> pages = (ArrayList<String>) file.get("Pages");
        Collections.sort(pages, new ModelXlsProcessor.AlphanumericComparator()); // Sort the pages numerically

        for (String page : pages) {
            int rowSize= 100;
            int columnSize = ((ArrayList<String>) file.get(page + "Indexes")).size();
            TableView<RowTablePreview> tablePreview = new TableView<>();
            TableColumn<RowTablePreview, String>[] columnsPreview = new TableColumn[rowSize];

            for (int i = 0; i < rowSize; i++) {
                final int columnIndex = i;
                TableColumn<RowTablePreview, String> column = new TableColumn<>("Column " + (columnIndex + 1));
                column.setCellValueFactory(cellData -> cellData.getValue().getCells().get(columnIndex).getValue());
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                columnsPreview[i] = column;
            }

            ObservableList<RowTablePreview> rowsPreview = FXCollections.observableArrayList();
            tablePreview.setItems(rowsPreview);
            tablePreview.getColumns().addAll(columnsPreview);
            ArrayList<String> indexes = (ArrayList<String>) file.get(page + "Indexes");
            LinkedHashMap<String, ArrayList<String>> pageData = (LinkedHashMap<String, ArrayList<String>>) file.get(page);


            for (String index : indexes) {
                ArrayList<String> row = pageData.get(index);
                while (row.size()<rowSize) row.add("");
                rowsPreview.add(new RowTablePreview(pageData.get(index)));
            }
            ScrollPane scrollablePage = new ScrollPane();
            scrollablePage.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollablePage.setPrefHeight(Region.USE_COMPUTED_SIZE);
            scrollablePage.setPrefWidth(Region.USE_COMPUTED_SIZE);
            scrollablePage.setMaxHeight(Region.USE_COMPUTED_SIZE);
            scrollablePage.setMaxWidth(Region.USE_COMPUTED_SIZE);
            scrollablePage.setContent(tablePreview);
            tabs.getTabs().add(new Tab(page, scrollablePage));
        }
        if (dataPreviewBox.getChildren().size()>=2) {
            dataPreviewBox.getChildren().remove(1);
        }
        dataPreviewBox.getChildren().add(tabs);

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
        dataPreviewBox.getChildren().get(1).setVisible(false);
        Data dataOld = tvObservableList.filtered(data -> data.getPath().equals(path)).get(0);
        tvObservableList.remove(dataOld);
        Data dataNew = new Data(dataOld);
        dataNew.setLastModified(time);

        tvObservableList.add(dataNew);
    }

    public static class AlphanumericComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            String[] parts1 = s1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            String[] parts2 = s2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            int length = Math.min(parts1.length, parts2.length);
            for (int i = 0; i < length; i++) {
                if (Character.isDigit(parts1[i].charAt(0)) && Character.isDigit(parts2[i].charAt(0))) {
                    int num1 = Integer.parseInt(parts1[i]);
                    int num2 = Integer.parseInt(parts2[i]);
                    int compareResult = Integer.compare(num1, num2);
                    if (compareResult != 0) {
                        return compareResult;
                    }
                } else {
                    int compareResult = parts1[i].compareTo(parts2[i]);
                    if (compareResult != 0) {
                        return compareResult;
                    }
                }
            }

            return Integer.compare(parts1.length, parts2.length);
        }
    }

}
