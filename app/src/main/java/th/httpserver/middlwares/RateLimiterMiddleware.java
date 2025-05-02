package th.httpserver.middlwares;

import java.io.IOException;

import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;

public class RateLimiterMiddleware implements Middleware {
    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain next) throws IOException {
        //TODO: Implement rate limiter
        next.handle(request, response);
    }
}
