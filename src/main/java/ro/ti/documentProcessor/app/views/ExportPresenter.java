package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.DatePicker;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;
import ro.ti.documentProcessor.app.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExportPresenter {

    @FXML
    private View exportView;
    @FXML
    DatePicker startDateFld;
    @FXML
    DatePicker endDateFld;
    @FXML
    TextField fileTypeFld;
    @FXML
    TextField clientNameFld;
    @FXML
    TextField fileNameFld;

    @FXML
    private TableView<Data> dataView;

    @FXML
    private TableColumn<Data, String> fileName;

    @FXML
    private TableColumn<Data, String> clientName;

    @FXML
    private TableColumn<Data,Void> actions;
    ObservableList<Data> entries;
    public void initialize() {
        exportView.setShowTransitionFactory(BounceInRightTransition::new);

        exportView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = AppManager.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        AppManager.getInstance().getDrawer().open()));
                appBar.setTitleText("Export");
            }
        });

        fileName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {
                    private final HBox btns = new HBox();
                    {
                        btns.setSpacing(5);
                        btns.setAlignment(Pos.CENTER_LEFT);
                        Button downloadBtn = new Button();
                        downloadBtn.setGraphic(MaterialDesignIcon.FILE_DOWNLOAD.graphic());
                        downloadBtn.setOnAction((ActionEvent event)->{
                            Data data = getTableView().getItems().get(getIndex());
                            String clientId = String.valueOf(DocumentProcessorGluonApplication.getController().getClientIdGivenClientName(data.getClientName()));
                            DocumentProcessorGluonApplication.getController().downloadFile(clientId, data.getName(), data.getExtension());
                        });

                        btns.getChildren().addAll(downloadBtn);

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

        actions.setCellFactory(cellFactory);
    }

    public void searchAll(ActionEvent event){
        entries = FXCollections.observableArrayList();
        HashMap<String, ArrayList<String>> databaseEntries =  DocumentProcessorGluonApplication.getController().readDatabaseEntries(fileNameFld.getText(), clientNameFld.getText(), fileTypeFld.getText(), null,null);
        ArrayList<String> fileNames = databaseEntries.get("fileName");
        ArrayList<String> clientNames = databaseEntries.get("clientName");
        ArrayList<String> fileTypes = databaseEntries.get("fileType");
        ArrayList<String> lastModifiedDates = databaseEntries.get("lastModified");


        for (int i = 0; i < fileNames.size(); i++) {
            entries.add(new Data(fileNames.get(i),clientNames.get(i),null,fileTypes.get(i),null));
        }
        dataView.setItems(entries);
    }
    
}
