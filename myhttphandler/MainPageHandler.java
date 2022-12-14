package myhttphandler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainPageHandler implements HttpHandler {
    @Override    
    public void handle(HttpExchange httpExchange) throws IOException {
        handleResponse(httpExchange, null); 
    }

    private void handleResponse(HttpExchange httpEx, String requestParam) {
        String line;
        StringBuilder response = new StringBuilder();
        String responseHtml = null;

        // This code needs to be changed to read in images as well. 
        // May be better to use bytes object from get go, and if the line has an image in it, then the according file needs to be found and opened to be read.

        try {
            File pageFile = new File("index.html");
            BufferedReader bufferedReader = 
                    new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(pageFile)));
            while ((line = bufferedReader.readLine()) != null) {

                

                response.append(line);
            }
            bufferedReader.close();
            responseHtml = response.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error finding file");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading file");
        }

        try {
            httpEx.sendResponseHeaders(200, responseHtml.length());
            OutputStream os = httpEx.getResponseBody();
            os.write(responseHtml.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.printf("Error sending headers.");
            e.printStackTrace();
            java.lang.System.exit(1);
        }


    }
}