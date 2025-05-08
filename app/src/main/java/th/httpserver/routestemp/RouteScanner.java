package th.httpserver.routestemp;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import th.httpserver.annotations.RequestMapping;


public class RouteScanner {

    public static List<RouteDefinition> extractRoutes(Class<?> controllerClass, String basePath) {
        List<RouteDefinition> routes = new ArrayList<>();

        for (Method method : controllerClass.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {
                RequestMapping rm = getRequestMappingFromAnnotation(annotation);

                if (rm != null) {
                    String path = normalizePath(basePath + getPathFromAnnotation(annotation));
                    String httpMethod = rm.method();

                    routes.add(new RouteDefinition(path, httpMethod, method));
                }
            }
        }

        return routes;
    }

    private static RequestMapping getRequestMappingFromAnnotation(Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            return (RequestMapping) annotation;
        }
        return annotation.annotationType().getAnnotation(RequestMapping.class);
    }

    private static String getPathFromAnnotation(Annotation annotation) {
        try {
            Method pathMethod = annotation.annotationType().getMethod("path");
            return (String) pathMethod.invoke(annotation);
        } catch (Exception e) {
            return "/";
        }
    }

    private static String normalizePath(String path) {
        return path.replaceAll("//+", "/");
    }
}
