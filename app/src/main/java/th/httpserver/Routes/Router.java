package th.httpserver.Routes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import th.httpserver.annotations.Controller;
import th.httpserver.annotations.RequestMapping;
import th.httpserver.annotations.GetMapping;
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
            
            for(Method method : controllerClass.getMethods()) {
                if(method.isAnnotationPresent(RequestMapping.class) || method.isAnnotationPresent(GetMapping.class)) {
                    String path;
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        path = normalizePath(basePath + getMapping.path());
                        System.out.println("Found GET mapping: " + path);
                    } else {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        path = normalizePath(basePath + requestMapping.path());
                        System.out.println("Found request mapping: " + path);
                    }
                    routes.put(path, method);
                }
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
        if(method != null) {
            try{
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
