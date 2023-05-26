package ro.ti.documentProcessor.MVC.controller;

import java.sql.*;
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
}
