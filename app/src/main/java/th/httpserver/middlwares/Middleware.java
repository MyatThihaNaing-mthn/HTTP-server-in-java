package th.httpserver.middlwares;
import java.io.IOException;

import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;

public interface Middleware {
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain next) throws IOException;
}
