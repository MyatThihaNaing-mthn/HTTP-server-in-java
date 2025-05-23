package th.httpserver.middlwares;

import java.io.IOException;

import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.RequestContext;
public class AuthMiddleware implements Middleware {
    @Override
    public void handle(RequestContext ctx, HttpResponse response, MiddlewareChain next) throws IOException {
        HttpRequest request = ctx.getRequest();
        String authHeader = request.getHeader("Authorization");
        // if(authHeader == null) {
        //     response.setStatus(HttpStatus.UNAUTHORIZED);
        //     return;
        // }
        System.out.println("AuthMiddleware Before router");
        next.handle(ctx, response);
        System.out.println("AuthMiddleware After router");
    }
}
