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
import java.util.ArrayList;
import java.util.Base64;

public class MainPageHandler implements HttpHandler {
    @Override    
    public void handle(HttpExchange httpExchange) throws IOException {
        handleResponse(httpExchange, null); 
    }

    private void handleResponse(HttpExchange httpEx, String requestParam) {
        String line;
        ArrayList<Byte> byteLine = new ArrayList<>();
        ArrayList<Byte> response = new ArrayList<>();
        byte[] responseHtml = null;

        // This code needs to be changed to read in images as well. 
        // May be better to use bytes object from get go, and if the line has an image in it, then the according file needs to be found and opened to be read.
        try {
            File pageFile = new File("index.html");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(pageFile)));
            while ((line = bufferedReader.readLine()) != null) {
                byteLine = translateLine(line);
                response.addAll(byteLine);
            }
            bufferedReader.close();
            responseHtml = new byte[response.size()];
            int i = 0;
            for (Byte b : response) {
                responseHtml[i++] = b.byteValue();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error finding file");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading file");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error in class");
        }

        try {
            httpEx.sendResponseHeaders(200, responseHtml.length);
            OutputStream os = httpEx.getResponseBody();
            os.write(responseHtml);
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.printf("Error sending headers.");
            e.printStackTrace();
            java.lang.System.exit(1);
        }
    }
    
    // Convert line in html to bytes, encoding images in base64 prior to byteifying 
    private ArrayList<Byte> translateLine(String line) throws ClassNotFoundException, IOException {
        ArrayList<Byte> byteArray = new ArrayList<>();
        int srcIndex = line.indexOf("src=");
        byte[] byteLine;

        // This means there is an image to encode to base64, need to translate string before and after to bytes
        if (srcIndex != -1) {

            int firstQuote = srcIndex + 5;
            String afterQuote = line.substring(firstQuote);
            int lastQuote = afterQuote.indexOf("\"");

            // Do first part of String
            String firstPart = line.substring(0, firstQuote);
            byteLine = firstPart.getBytes();
            for (final byte val : byteLine) {
                byteArray.add(val);
            }

            // Do image file
            String imageFile = line.substring(firstQuote, firstQuote + lastQuote);
            String imageText = img2Text(imageFile);
            byteLine = imageText.getBytes();
            for (final byte val : byteLine) {
                byteArray.add(val);
            }

            // Do second part of string
            String secondPart = line.substring(firstQuote + lastQuote);
            byteLine = secondPart.getBytes();
            for (final byte val : byteLine) {
                byteArray.add(val);
            }

        } else {
            byte[] wholeLineBytes = StringToByte(line);
            for (final byte val : wholeLineBytes) {
                byteArray.add(val);
            }
        }
        return byteArray;  
    }

    // Convert image to base64 text
    public String img2Text(String path){
        String base64="";
        // Grab file type
        String fileType = path.substring(path.lastIndexOf('.') + 1);
        try{
            File file = new File(path);
            byte[] imageBytes = new byte[(int) file.length()];
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            fis.read(imageBytes);
            fis.close();
            base64 = Base64.getEncoder().encodeToString(imageBytes);
        }catch(Exception e){
            e.printStackTrace(); 
        }
        return "data:image/" + fileType + ";base64," + base64;
    }

    private byte[] StringToByte(String line) {
        byte[] lineBytes = line.getBytes();
        return lineBytes;
    }
}
