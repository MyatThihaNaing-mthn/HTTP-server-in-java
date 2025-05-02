package th.httpserver.controllers;

import java.io.IOException;
import th.httpserver.annotations.Controller;
import th.httpserver.annotations.GetMapping;
import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpStatus;
import th.httpserver.utils.FileReader;

@Controller(basePath = "/")
public class RootController {
    
    @GetMapping(path = "")
    public HttpResponse getRoot(HttpRequest request, HttpResponse response) {
        // Set status code and message
        response.setStatus(HttpStatus.OK);
        
        // Set headers
        response.addHeader("Content-Type", "text/html");
        
        // Try to read index.html, fallback to default HTML if not found
        byte[] body;
        try {
            body = FileReader.readFile("/index.html");
            if (body == null) {
                throw new IOException("File not found");
            }
        } catch (IOException e) {
            // Fallback to default HTML
            String defaultHtml = "<!DOCTYPE html><html><head><title>HTTP Server</title></head><body><h1>Welcome to HTTP Server</h1></body></html>";
            body = defaultHtml.getBytes();
        }
        
        // Set body and content length
        response.setBody(body);
        
        return response;
    }
}
