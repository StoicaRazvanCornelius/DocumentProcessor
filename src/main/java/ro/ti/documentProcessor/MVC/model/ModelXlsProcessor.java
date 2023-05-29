package ro.ti.documentProcessor.MVC.model;

import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelXlsProcessor implements Model {
    Controller controller;
    @Override
    public void setController(Controller controller) {
        this.controller=controller;
    }

    @Override
    public Model getModel() {
        return this;
    }

    @Override
    public HashMap parseFile(File givenFile) {
        System.out.println("Entered model with file" + givenFile.getName());
        HashMap<String,ArrayList<String>> file= new HashMap<>();
        //Make file
        ArrayList<String> pagesName= new ArrayList<>();
        pagesName.add("0");
        pagesName.add("1");
        pagesName.add("2");
        pagesName.add("3");
        file.put("Pages",pagesName);

        for (String pageName:pagesName){
            ArrayList<String> indexesArray = new ArrayList<>();
            for (int i =0;i<100;i++){
                indexesArray.add(String.valueOf(i));
            }
            file.put(pageName+"Indexes",indexesArray);
            ArrayList<String> columnsName = new ArrayList<>();
            for (int i=0;i<100;i++){
                columnsName.add(String.valueOf(i));
            }
            file.put(pageName+"Columns",columnsName);
            for (int i =0;i<100;i++){
                ArrayList<String> row =new ArrayList();
                for(String column: file.get(pageName+"Columns"))
                {
                    row.add(String.valueOf(i));
                }
                file.put(pageName+i,row);
            }

        }
        return file;
    }

    @Override
    public void testModel() {
        System.out.print("check\n");
        System.out.print("  From model:\n -controller: ");
        controller.testController();
    }
}
