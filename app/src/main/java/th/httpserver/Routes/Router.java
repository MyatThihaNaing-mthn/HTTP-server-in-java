package th.httpserver.Routes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import th.httpserver.annotations.Controller;
import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpStatus;

public class Router {
    private static final Map<String, Method> routes = new HashMap<>();
    private static final Router INSTANCE = new Router();

    public static Router getInstance() {
        return INSTANCE;
    }

    private Router() {
        System.out.println("Router constructor called");
        initRoutes();
    }

    private void initRoutes() {
        System.out.println("Router initRoutes starting");
        Set<Class<?>> controllerClasses = getAllControllerClasses();
        System.out.println("Found " + controllerClasses.size() + " controller classes");

        for (Class<?> controllerClass : controllerClasses) {
            System.out.println("Processing controller: " + controllerClass.getName());
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            String basePath = controllerAnnotation.basePath();
            System.out.println("Base path: " + basePath);

            List<RouteDefinition> routeDefinitions = RouteScanner.extractRoutes(controllerClass, basePath);

            for (RouteDefinition route : routeDefinitions) {
                System.out.println(route);
                routes.put(route.getPath(), route.getHandler());
            }
        }
        System.out.println("Router initRoutes completed. Total routes: " + routes.size());
    }

    private String normalizePath(String path) {

        path = path.replaceAll("/+", "/");
        // Remove trailing slash if not root
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static void handleRequest(HttpRequest request, HttpResponse response) {
        System.out.println("Router handleRequest " + request.toString());
        INSTANCE.instanceHandleRequest(request, response);
    }

    private void instanceHandleRequest(HttpRequest request, HttpResponse response) {
        String path = request.getPath();
        String normalizedPath = normalizePath(path);
        System.out.println("normalizedPath: " + normalizedPath);
        Method method = routes.get(normalizedPath);
        if (method != null) {
            try {
                Object controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                method.invoke(controllerInstance, request, response);
                return; // Return after successful handling
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return; // Return after error
            }
        }
        // Only handle not found if no route was found
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("Not Found".getBytes());

    }

    private Set<Class<?>> getAllControllerClasses() {
        Reflections reflections = new Reflections("th.httpserver.controllers");
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
