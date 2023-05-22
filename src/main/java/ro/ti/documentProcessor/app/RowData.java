package ro.ti.documentProcessor.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class RowData {
    private ObservableList<Cell> cells = FXCollections.observableArrayList();

    public RowData(ArrayList<String> cellDataList) {
        for (String cellData : cellDataList) {
            cells.add(new Cell(cellData));
        }
    }

    public ObservableList<Cell> getCells() {
        return cells;
    }
}
