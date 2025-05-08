package th.httpserver.routestemp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final Map<String, Method> staticRoutes = new HashMap<>();
    private static final List<RouteDefinition> dynamicRoutes = new ArrayList<>();    
    private static final Router INSTANCE = new Router();

    public static Router getInstance() {
        return INSTANCE;
    }

    private Router() {
        System.out.println("Initializing Router...");
        initRoutes();
    }

    private void initRoutes() {        
        Set<Class<?>> controllerClasses = getAllControllerClasses();

        for (Class<?> controllerClass : controllerClasses) {
            System.out.println("Processing controller: " + controllerClass.getName());
            Controller controllerAnnotation = controllerClass.getAnnotation(Controller.class);
            String basePath = controllerAnnotation.basePath();
            System.out.println("Base path: " + basePath);

            List<RouteDefinition> routeDefinitions = RouteScanner.extractRoutes(controllerClass, basePath);

            for (RouteDefinition route : routeDefinitions) {
                if (route.isDynamic()) {
                    dynamicRoutes.add(route);
                } else {
                    staticRoutes.put(route.getPath(), route.getHandler());
                }
            }
        }
        System.out.println("Router initRoutes completed. Total routes: " + (staticRoutes.size() + dynamicRoutes.size()));
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
        INSTANCE.instanceHandleRequest(request, response);
    }

    private void instanceHandleRequest(HttpRequest request, HttpResponse response) {
        String path = request.getPath();
        String normalizedPath = normalizePath(path);
        Method method = staticRoutes.get(normalizedPath); 
        if (method != null) {
            try {
                Object controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                method.invoke(controllerInstance, request, response);
                return; // Return after successful handling
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return;
            }
        }

        for (RouteDefinition route : dynamicRoutes) {
            if (route.getPathPattern().matcher(path).matches()) {
                try {
                    // create new httprequest with path params 
                    request = request.withPathParams(getPathParams(route, path));
                    Object controllerInstance = route.getHandler().getDeclaringClass().getDeclaredConstructor().newInstance();
                    route.getHandler().invoke(controllerInstance, request, response);
                    return; // Return after successful handling
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    return; // Return after error
                }
            }
        }

        // Only handle not found if no route was found
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("Not Found".getBytes());

    }
  

    //TODO: Need to check if the path is a match but expensive operation
    private Map<String, String> getPathParams(RouteDefinition routeDefinition, String acutalPath) { 
        if (!routeDefinition.isDynamic()) { return Collections.emptyMap(); } 
        Map<String, String> pathParams = new HashMap<>();
        String[] pathParts = acutalPath.split("/");
        String[] patternParts = routeDefinition.getPath().split("/");

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                pathParams.put(patternParts[i].substring(1, patternParts[i].length() - 1), pathParts[i]);
                System.out.println("Path param: " + patternParts[i].substring(1, patternParts[i].length() - 1) + " = " + pathParts[i]);
            }
        }
        return pathParams;
    }

    private Set<Class<?>> getAllControllerClasses() {
        Reflections reflections = new Reflections("th.httpserver.controllers");
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
