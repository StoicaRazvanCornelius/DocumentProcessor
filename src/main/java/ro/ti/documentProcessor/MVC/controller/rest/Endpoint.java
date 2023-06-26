package ro.ti.documentProcessor.MVC.controller.rest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Endpoint {

    private static final String BASE_URI = "http://localhost/connectDatabase.php";

    public static void main(String[] args) {
        Endpoint endpoint = new Endpoint();
        String filePath = "C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\Book1.xlsx"; // Replace with the actual file path
        String name = "Book1";
        String extension = "xlsx";
        String path = ".\\files";
        String client = "ss";
        String clientId = "3";
        String lastModified = "2023-05-25 10:30:00";
        endpoint.uploadFile(filePath, name, extension, lastModified,clientId,client);
        //endpoint.downloadFile(null);
    }

    public boolean uploadFile(String filePath, String name, String extension, String lastModified,String clientId, String clientName) {
        try {
            String encodedPath = URLEncoder.encode(".\\files", "UTF-8");
            String encodedUrl = "http://localhost/upload.php";
            URL url = new URL(encodedUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String boundary = "----WebKitFormBoundary" + Long.toHexString(System.currentTimeMillis());
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // Write the file_name parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file_name\"\r\n\r\n");
            writer.append(name).append("\r\n");

            // Write the extension parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"extension\"\r\n\r\n");
            writer.append(extension).append("\r\n");

            // Write the path parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"path\"\r\n\r\n");
            writer.append(encodedPath).append("\r\n");

            // Write the client parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"client\"\r\n\r\n");
            writer.append(clientName).append("\r\n");

            // Write the clientId parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"clientId\"\r\n\r\n");
            writer.append(clientId).append("\r\n");

            // Write the lastModified parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"lastModified\"\r\n\r\n");
            writer.append(lastModified).append("\r\n");

            // Write the file parameter
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(filePath).append("\"\r\n");
            writer.append("Content-Type: application/octet-stream\r\n\r\n");
            writer.flush();

            // Write the file data
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            fileInputStream.close();

            // End of multipart/form-data
            writer.append("\r\n").append("--").append(boundary).append("--").append("\r\n");
            writer.close();


            // Get the response from the server
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    System.out.println("Server Response: " + response.toString());
                }
            } else {
                System.out.println("Failed to upload file. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void downloadFile(String savePath) {
        String fileURL = "http://localhost/download.php";
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
