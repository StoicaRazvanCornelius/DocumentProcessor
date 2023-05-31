package ro.ti.documentProcessor.MVC.controller.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static ro.ti.documentProcessor.DocumentProcessorGluonApplication.properties;

public class Database {

    String url;
    String username;
    String password;
    Connection connection;
    Statement statement;
    ResultSet resultSet;
    String query;

    String retriveAllClients = "SELECT * FROM documentprocessorapp.client";
    String retriveAllFiles = "SELECT * FROM documentprocessorapp.file";
    String retriveFilesWithCorrespondingClientNames = "SELECT file.id, file.name, client.clientName" +
            "FROM documentprocessorapp.file\n" +
            "JOIN documentprocessorapp.client ON file.clientId = client.id";
    String retriveFilesWithSpecificType = "SELECT * FROM documentprocessorapp.file WHERE" +
        "typeId = (SELECT id FROM documentprocessorapp.filetype WHERE typeName = 'your_file_type')";
    String retriveTotalCountOfFilesForEachClient = "SELECT client.clientName, COUNT(file.id) AS fileCount\n" +
          "FROM documentprocessorapp.client\n" +
          "LEFT JOIN documentprocessorapp.file ON client.id = file.clientId\n" +
          "GROUP BY client.id, client.clientName;";


    public Database(Properties properties){
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectTypes(){

        try {
            query = "SELECT * FROM filetype";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String column1Value = resultSet.getString("typeName");
                System.out.println("typeName " + column1Value);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkifConnectionIsValid() {
        try {
            return connection.isValid(10);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap getEntriesFor(String fileName, String clientName, String fileType, String startDateTime, String endDateTime) {
        HashMap<String, ArrayList<String >>results  = new HashMap<>();
        query = "SELECT * FROM file f JOIN client c ON f.clientId = c.id JOIN filetype ft ON f.typeId = ft.id WHERE 1=1";
        if (fileName != null && !fileName.isEmpty()) {
            query +=" AND f.name = '" + fileName+ "'";
        }

        if (clientName != null && !clientName.isEmpty()) {
            query +=" AND c.clientName = '"+clientName+"'";
        }

        if (fileType != null && !fileType.isEmpty()) {
            query +=" AND ft.typeName = '"+fileType+"'";
        }

        if (startDateTime != null && !startDateTime.isEmpty()) {
            query +=" AND f.lastModified >= '"+startDateTime+"'";
        }

        if (endDateTime != null && !endDateTime.isEmpty()) {
            query +=" AND f.lastModified <= '"+endDateTime+"'";
        }

        query +=" ORDER BY f.name ASC, c.clientName ASC, ft.typeName ASC";

        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String file = resultSet.getString("name");
                String client = resultSet.getString("clientName");
                String type = resultSet.getString("typeName");

                results.computeIfAbsent("fileName", key -> new ArrayList<>()).add(file);
                results.computeIfAbsent("clientName", key -> new ArrayList<>()).add(client);
                results.computeIfAbsent("fileType", key -> new ArrayList<>()).add(type);
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}







