package th.httpserver.middlwares;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import th.httpserver.http.HttpResponse;
import th.httpserver.http.RequestContext;
import th.httpserver.routes.Router;

public class MiddlewareChain {
    private final Iterator<Middleware> middlewares;

    public MiddlewareChain(List<Middleware> middlewares) {
        this.middlewares = middlewares.iterator();
    }

    

    public void handle(RequestContext ctx, HttpResponse response) throws IOException {
        if(middlewares.hasNext()) {
            Middleware middleware = middlewares.next();
            middleware.handle(ctx, response, this);
        }else{
            // Act as final middleware to controllers
            Router.handleRequest(ctx.getRequest(), response);
        }
        
    }
}