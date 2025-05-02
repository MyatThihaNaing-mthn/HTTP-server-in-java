package th.httpserver.middlwares;

import java.io.IOException;
import th.httpserver.http.HttpRequest;
import th.httpserver.http.HttpResponse;
import th.httpserver.http.HttpStatus;
import th.httpserver.utils.FileReader;

public class StaticFilesMiddleware implements Middleware {

    @Override
    public void handle(HttpRequest request, HttpResponse response, MiddlewareChain next) throws IOException {
        if(request.getPath() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setBody("Bad Request".getBytes());
            return;
        }

        if (checkRootPath(request)) {
            byte[] body = null;
            try {
                body = FileReader.readFile("/index.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (body == null) {
                body = "404 Not Found".getBytes();
            }
            response.setBody(body);
            response.addHeader("Content-Type", "text/html");
            response.setStatus(HttpStatus.OK);
            return;
        }

        if (checkStaticFileRequest(request)) {
            System.out.println("checkStaticFileRequest: " + request.getPath());
            String filePath = request.getPath();
            byte[] body = null;
            try {
                body = FileReader.readFile(filePath);
                System.out.println("body size: " + body.length);
                response.setBody(body);
                
                response.addHeader("Content-Type", getContentType(filePath));
                response.setStatus(HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setBody("Not Found".getBytes());
            }
            return;
        }

        // If we get here, it's not a static file request, so pass it to the next middleware
        next.handle(request, response);
    }

    private Boolean checkRootPath(HttpRequest request) {
        String path = request.getPath();
        System.out.println("checkRootPath: " + path);
        if (path.equals("/")) {
            return true;
        }
        return false;
    }

    private Boolean checkStaticFileRequest(HttpRequest request) {
        String path = request.getPath();
        System.out.println("Checking static file request for path: " + path);
        boolean isStaticFile = path.matches(".*\\.[a-zA-Z0-9]+$");
        System.out.println("Is static file: " + isStaticFile);
        return isStaticFile;
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".js")) return "application/javascript";
        if (filePath.endsWith(".png")) return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
    }
}
