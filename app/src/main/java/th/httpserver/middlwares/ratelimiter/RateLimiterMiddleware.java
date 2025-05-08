package th.httpserver.middlwares.ratelimiter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpStatus;
import th.httpserver.http.RequestContext;
import th.httpserver.middlwares.Middleware;
import th.httpserver.middlwares.MiddlewareChain;
public class RateLimiterMiddleware implements Middleware {

    private final Map<String, TokenBucket> tokenBuckets;

    public RateLimiterMiddleware() {
        this.tokenBuckets = new HashMap<>();
    }

    private TokenBucket getTokenBucket(String ipAddress) {
        return tokenBuckets.computeIfAbsent(ipAddress, k -> new TokenBucket(10, 6));
    }

    private boolean isRateLimited(String ipAddress) {
        TokenBucket tokenBucket = getTokenBucket(ipAddress);
        return !tokenBucket.allowRequest();
    }

    @Override
    public void handle(RequestContext ctx, HttpResponse response, MiddlewareChain next) throws IOException {
        System.out.println("Inside rate limiter middleware");
        HttpRequest request = ctx.getRequest();
        String ipAddress = request.getIpAddress();
        if(isRateLimited(ipAddress)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS);
            System.out.println("Rate limited");
            return;
        }
        next.handle(ctx, response);
    }
}
