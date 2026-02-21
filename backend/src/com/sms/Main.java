package com.sms;

import com.sms.controller.StudentController;
import com.sms.service.StudentService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class Main {
    private static final int PORT = 8080;
    private static final String FRONTEND_DIR = "frontend";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        StudentService studentService = new StudentService();
        StudentController studentController = new StudentController(studentService);

        // API Context
        server.createContext("/api/students", studentController);

        // Static Files Context
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null); // creates a default executor
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    private static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File(FRONTEND_DIR + path);
            if (file.exists() && !file.isDirectory()) {
                String contentType = Files.probeContentType(file.toPath());
                if (contentType == null) {
                    if (path.endsWith(".css")) contentType = "text/css";
                    else if (path.endsWith(".js")) contentType = "application/javascript";
                    else contentType = "text/plain";
                }
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());
                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = exchange.getResponseBody()) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                }
            } else {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }
}
