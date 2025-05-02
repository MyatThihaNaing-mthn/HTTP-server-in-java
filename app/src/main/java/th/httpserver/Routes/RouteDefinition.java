package th.httpserver.Routes;

import java.lang.reflect.Method;

public class RouteDefinition {
    private final String path;
    private final String method;
    private final Method handler;

    public RouteDefinition(String path, String method, Method handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Method getHandler() {
        return handler;
    }

    @Override
    public String toString() {
        return "Route[" + method + " " + path + "] → " + handler.getName();
    }
}
