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
import java.util.Array;

public class MainPageHandler implements HttpHandler {
    @Override    
    public void handle(HttpExchange httpExchange) throws IOException {
        handleResponse(httpExchange, null); 
    }

    private void handleResponse(HttpExchange httpEx, String requestParam) {
        String line;
        ArrayList<Byte> byteLine = new ArrayList<>();
        ArrayList<Byte> response = new ArrayList<>();
        String responseHtml = null;

        // This code needs to be changed to read in images as well. 
        // May be better to use bytes object from get go, and if the line has an image in it, then the according file needs to be found and opened to be read.

        try {
            File pageFile = new File("../index.html");
            BufferedReader bufferedReader = 
                    new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(pageFile)));
            while ((line = bufferedReader.readLine()) != null) {
                byteLine = translateLine(line);
                response.addAll(byteLine);
            }
            bufferedReader.close();
            responseHtml = response.toArray();
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
    
    public String img2Text(String path){
        String base64="";
        try{
            InputStream iSteamReader = new FileInputStream("featured-700x467.png");
            byte[] imageBytes = IOUtils.toByteArray(iSteamReader);
            base64 = Base64.getEncoder().encodeToString(imageBytes);
            System.out.println(base64);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "data:image/png;base64,"+base64;
    }
    
    private ArrayList<Byte> translateLine(line) {
        ArrayList<Byte> byteArray = new ArrayList<>();
        int srcIndex = line.indexOf("src=");
        // This means there is an image to encode to base64, might need to make size -1
        if (srcIndex != line.size()) {
            int firstQuote = // find first quote 
            int lastQuote = // find last quote
            String imageFile = line.substring(firstQuote+1, lastQuote-1);
            ArrayList<Byte> imageBytes = img2Text(imageFile).getBytes();
            byteArray.addAll(imageBytes);
        }
        
    }
}
