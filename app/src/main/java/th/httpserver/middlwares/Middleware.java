package th.httpserver.middlwares;
import java.io.IOException;

import th.httpserver.http.HttpResponse;
import th.httpserver.http.RequestContext;

public interface Middleware {
    public void handle(RequestContext ctx, HttpResponse response, MiddlewareChain next) throws IOException;
}
