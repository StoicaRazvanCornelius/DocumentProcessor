package ro.ti.documentProcessor.MVC.controller.rest;

import ro.ti.documentProcessor.DocumentProcessorGluonApplication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


public class Endpoint {

    private static final String BASE_URI = "http://localhost/connectDatabase.php";

    public static void main(String[] args) {
        Endpoint endpoint = new Endpoint();
        //endpoint.uploadFile(null);
        //endpoint.downloadFile(null);
    }
    public void uploadFile(String filePath) {
        filePath = "C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\Book1.xlsx";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        try {
            URI uri = URI.create(BASE_URI);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // Set the appropriate headers for file upload
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            // Read the file and write it to the connection's output stream
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                 OutputStream outputStream = connection.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            // Get the response from the server
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("File uploaded successfully.");
            } else {
                System.out.println("Failed to upload file. Response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String savePath) {
        String fileURL = BASE_URI;
        savePath = "C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\Test.txt";

        try {
            URI uri = URI.create(BASE_URI);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Get the content type and file name from response headers
                String contentType = connection.getContentType();
                String fileName = "";

                String disposition = connection.getHeaderField("Content-Disposition");
                if (disposition != null && disposition.contains("filename=")) {
                    int index = disposition.indexOf("filename=");
                    fileName = disposition.substring(index + 9).replace("\"", "");
                } else {
                    // Extract the file name from the URL if not found in headers
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
                }

                // Create the save directory if it doesn't exist
                File directory = new File(savePath).getParentFile();
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Save the file locally
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(savePath);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded successfully.");
                System.out.println("Saved file: " + savePath);
            } else {
                System.out.println("Failed to download file. Response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
