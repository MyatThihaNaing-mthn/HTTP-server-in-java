package th.httpserver.middlwares;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import th.httpserver.http.HttpResponse;

public class ResponseSender {
    public static void sendResponse(HttpResponse response, Socket clientSocket) throws IOException {
        StringBuilder responseStr = new StringBuilder();
        byte[] bodyBytes = response.getBody();

        // Status line
        responseStr.append("HTTP/1.1 ");
        responseStr.append(response.getStatusCode());
        responseStr.append(" ");
        responseStr.append(response.getStatusMessageString());
        responseStr.append("\r\n");

        // Compress body if gzip is requested
        if (response.getHeaders().containsKey("Content-Encoding") 
            && "gzip".equals(response.getHeaders().get("Content-Encoding"))) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
                gzipStream.write(bodyBytes);
            }
            bodyBytes = byteStream.toByteArray();
        }

        // Headers
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            responseStr.append(header.getKey());
            responseStr.append(": ");
            responseStr.append(header.getValue());
            responseStr.append("\r\n");
        }

        // Add Content-Length if not present
        if (!response.getHeaders().containsKey("Content-Length") && bodyBytes != null) {
            responseStr.append("Content-Length: ");
            responseStr.append(bodyBytes.length);
            responseStr.append("\r\n");
        }

        // End of headers
        responseStr.append("\r\n");

        // Print response before sending
        System.out.println("Sending response:");
        System.out.println(responseStr.toString());
        if (bodyBytes != null) {
            System.out.println("Body: " + new String(bodyBytes));
        }

        // Send headers
        clientSocket.getOutputStream().write(responseStr.toString().getBytes());
        
        // Send body
        if (bodyBytes != null) {
            clientSocket.getOutputStream().write(bodyBytes);
        }
        
        clientSocket.getOutputStream().flush();
    }

}
