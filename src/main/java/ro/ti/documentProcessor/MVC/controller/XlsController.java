package ro.ti.documentProcessor.MVC.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.ControllerFile;
import ro.ti.documentProcessor.MVC.Interfaces.Model;
import ro.ti.documentProcessor.MVC.Interfaces.View;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;

public class XlsController implements ControllerFile {


    @Override
    public HashMap readFromFile(String path) throws IOException {
        return null;
    }

    @Override
    public void writeToFile(String path, String name) throws IOException {

    }

}
