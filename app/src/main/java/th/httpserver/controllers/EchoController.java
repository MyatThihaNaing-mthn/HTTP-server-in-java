package th.httpserver.controllers;

import java.util.HashMap;
import java.util.Map;
import th.httpserver.annotations.Controller;
import th.httpserver.annotations.GetMapping;
import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpStatus;


@Controller(basePath = "/echo")
public class EchoController {
    
    @GetMapping(path = "")
    public HttpResponse echo(HttpRequest request, HttpResponse response) {
        
        // Set status code and message
        response.setStatus(HttpStatus.OK);
        
        
        String connection = request.getHeader("Connection");
        if (connection != null && connection.equals("close")) {
            response.addHeader("Connection", "close");
        } else {
            response.addHeader("Connection", "keep-alive");
        }
        
        response.addHeader("Content-Type", "text/plain");
        response.setBody("Hello, Echo!".getBytes());
        
        return response;
    }

    @GetMapping(path = "/hello")
    public HttpResponse hello(HttpRequest request, HttpResponse response) {
        
        // Set status code and message
        response.setStatus(HttpStatus.OK);
        
        // Set headers
        response.addHeader("Content-Type", "text/plain");
        
        String connection = request.getHeader("Connection");
        if (connection != null && connection.equals("close")) {
            response.addHeader("Connection", "close");
        } else {
            response.addHeader("Connection", "keep-alive");
        }
        
        response.setBody("Hello, World!".getBytes());

        return response;
    }

    @GetMapping(path = "/hello/{name}")
    public HttpResponse helloName(HttpRequest request, HttpResponse response) {        
        String name = request.getPathParam("name").orElse("Thiha");
        response.setStatus(HttpStatus.OK);
        response.addHeader("Content-Type", "text/plain");

        String connection = request.getHeader("Connection");
        if (connection != null && connection.equals("close")) {
            response.addHeader("Connection", "close");
        } else {
            response.addHeader("Connection", "keep-alive");
        }

        response.setBody(("Hello, " + name + "!").getBytes());

        return response;
    }

}
