import myhttphandler.MainPageHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        int port = 8001;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            server.createContext("/", new MainPageHandler());
            server.setExecutor(threadPoolExecutor);
            System.out.printf("Starting server on port %d\n", port);
            server.start();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Error in grabbing server on %d\n", port);
        }

    }
}