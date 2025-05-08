package th.httpserver.middlwares.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucket {
    private final int capacity;
    private final int refillRatePerSecond;
    private final AtomicInteger tokens;
    private long lastRefill;
    private volatile long lastAccessTime;

    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRate;
        this.tokens = new AtomicInteger(capacity);
        this.lastRefill = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refill();
        if(tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
     }

    private void refill() {
        long now = System.nanoTime();
        lastAccessTime = now;
        long elapsedNanos = now - lastRefill; 
        int tokenToAdd = (int) (elapsedNanos * refillRatePerSecond / 1_000_000_000);

        System.out.println("Total tokens: " + tokens.get());
        if(tokenToAdd > 0) {
            long newTokens = Math.min(capacity, tokens.get() + tokenToAdd);
            tokens.set((int) newTokens);
            lastRefill = now;
         }
     }

     public long getLastAccessTime() {
        return lastAccessTime;
     }

}
