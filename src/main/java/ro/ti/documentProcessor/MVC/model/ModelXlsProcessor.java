package ro.ti.documentProcessor.MVC.model;

import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.Model;

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
    public void testModel() {
        System.out.print("check\n");
        System.out.print("  From model:\n -controller: ");
        controller.testController();
    }
}
