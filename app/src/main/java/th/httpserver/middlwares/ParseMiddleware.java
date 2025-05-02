package th.httpserver.middlwares;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpParser;

public class ParseMiddleware implements Middleware {
    private Socket clientSocket;

    public ParseMiddleware(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain next) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        request = HttpParser.parse(inputStream);
        next.handle(request, response);
    }   
}
