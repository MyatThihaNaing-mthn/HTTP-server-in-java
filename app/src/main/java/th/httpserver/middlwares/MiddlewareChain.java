package th.httpserver.middlwares;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import th.httpserver.Routes.Router;
import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;

public class MiddlewareChain {
    private final Iterator<Middleware> middlewares;

    public MiddlewareChain(List<Middleware> middlewares) {
        // Default middleware is ParseMiddleware
        this.middlewares = middlewares.iterator();
    }

    

    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        if(middlewares.hasNext()) {
            Middleware middleware = middlewares.next();
            middleware.handle(request, response, this);
        }else{
            // Act as final middleware to controllers
            Router.handleRequest(request, response);
        }
        
    }
}