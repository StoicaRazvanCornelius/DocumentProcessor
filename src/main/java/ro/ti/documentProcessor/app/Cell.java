package ro.ti.documentProcessor.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cell {
    StringProperty value;

    public Cell(String value){
        this.value=new SimpleStringProperty(value);
    }
    public StringProperty getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = new SimpleStringProperty(value);
    }
}
