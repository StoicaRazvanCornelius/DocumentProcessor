package ro.ti.documentProcessor.MVC.controller.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class Endpoint {

    private static final String BASE_URI = "http://localhost/connectDatabase.php";

/*
    public static void main(String[] args) {
        Endpoint endpoint = new Endpoint();
        String filePath = "C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\Book1.xlsx"; // Replace with the actual file path
        String name = "Book1";
        String extension = "xlsx";
        String path = ".\\files";
        String client = "ss";
        String clientId = "3";
        String lastModified = "2023-05-25 10:30:00";
        //endpoint.uploadFile(filePath, name, extension, lastModified,clientId,client);
        endpoint.downloadFile("C:\\Users\\stoic\\OneDrive\\Desktop\\xlsFiles\\", "5", "tmp","xlsx");
    }

 */
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

    public void downloadFile(String savePath, String clientId, String fileName, String fileExtension) {
        String fileURL = "http://localhost/download.php";
        savePath = savePath + fileName + "." +fileExtension;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(fileURL);

            // Prepare the parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("file_name", fileName));
            params.add(new BasicNameValuePair("file_extension", fileExtension));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();

                try (InputStream inputStream = entity.getContent()) {
                    Files.copy(inputStream, Path.of(savePath), StandardCopyOption.REPLACE_EXISTING);
                }

                System.out.println("File downloaded successfully.");
                System.out.println("Saved file: " + savePath);
            } else {
                System.out.println("Failed to download file. Response code: " + statusCode);
            }
        } catch (IOException e) {
        }
    }
}

