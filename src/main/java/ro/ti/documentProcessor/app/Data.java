package ro.ti.documentProcessor.app;


import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class Data {

    private String name;
    private String path;
    private String extension;
    private Timestamp lastModified;

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    private SimpleStringProperty clientName;

    public Data(String name, String path, String extension, Timestamp lastModified){
        this.name= name;
        this.path= path;
        this.extension=extension;
        this.lastModified=lastModified;
        this.clientName = new SimpleStringProperty();
    }

    public Data(Data data) {
        this.name= data.name;
        this.path = data.getPath();
        this.extension = data.extension;
        this.lastModified = data.lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "[Name:"+name + ", Path:"+ path+", Extension:"+extension +", LastModified:"+lastModified+"]";
    }


}
