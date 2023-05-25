package ro.ti.documentProcessor.app;


public class Data {

    private int id;
    private String name;

    public Data(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String nme) {
        this.name = nme;
    }

    @Override
    public String toString() {
        return "id: " + id + " - " + "name: " + name;
    }

}
