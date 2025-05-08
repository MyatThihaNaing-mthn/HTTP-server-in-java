package th.httpserver.routestemp;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class RouteDefinition {
    private static final Pattern DYNAMIC_PATH_PATTERN = Pattern.compile("\\{\\s*\\w+\\s*\\}");
    private final String path;
    private final Pattern pathPattern;
    private final String method;
    private final Method handler;
    private final boolean isDynamic;

    public RouteDefinition(String path, String method, Method handler) {
        this.path = path;
        this.method = method;
        this.handler = handler;
        this.isDynamic = DYNAMIC_PATH_PATTERN.matcher(path).find();
        this.pathPattern = isDynamic ? Pattern.compile(getRegexPath(path)) : null;

        System.out.println("Creating RouteDef " + this.isDynamic + " " + this.path + " " + this.pathPattern);
    }

    public String getPath() {
        return path;
    }

    public Pattern getPathPattern() {
        return pathPattern;
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public String getMethod() {
        return method;
    }

    public Method getHandler() {
        return handler;
    }

    private String getRegexPath(String path) {
        return "^" + path.replaceAll("\\{\\s*\\w+\\s*\\}", "([^/]+)") + "$";
    }

    @Override
    public String toString() {
        return "Route[" + method + " " + path + "] → " + handler.getName();
    }
}
