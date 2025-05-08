package th.httpserver.middlwares;

import java.io.IOException;
import java.net.Socket;

import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpParser;
import th.httpserver.http.HttpStatus;
import th.httpserver.http.RequestContext;

public class ParseMiddleware implements Middleware {
    private final Socket clientSocket;

    public ParseMiddleware(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void handle(RequestContext ctx, HttpResponse response, MiddlewareChain next) throws IOException {
        HttpRequest request = HttpParser.parse(clientSocket);
        if(request == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            return;
        }
        ctx.setRequest(request);
        next.handle(ctx, response);
    }   
}
