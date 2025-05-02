package th.httpserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
public class HttpResponse {
    private String version;
    private HttpStatus status;
    private byte[] body;
    private Map<String, String> headers;

    public HttpResponse() {
        //Default headers for empty response
        this.headers = new HashMap<>();
        this.body = new byte[0];
        this.addHeader("Content-Type", "text/plain");
        this.addHeader("Connection", "keep-alive");
        this.addHeader("Server", "Java HTTP Server");

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }


    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
        setContentLength();
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    private void setContentLength() {
        if(body != null) {
            this.headers.put("Content-Length", String.valueOf(body.length));
        } else {
            this.headers.put("Content-Length", "0");
        }
    }


    public void addHeader(String key, String value) {
        if ("Content-Length".equalsIgnoreCase(key)) {
            throw new UnsupportedOperationException(
                "Content-Length is automatically calculated. Use setBody() instead.");
        }
        this.headers.put(key, value);
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public String getStatusMessageString() {
        return status.getMessage();
    }

    @Override
    public String toString() {
        return "HTTP/1.1 " + getStatusCode() + " " + getStatusMessageString() + "\r\n" +
               "Content-Type: " + headers.get("Content-Type") + "\r\n" +
               "Content-Length: " + headers.get("Content-Length") + "\r\n" +
               "Connection: " + headers.get("Connection") + "\r\n" +
               "Body: " + new String(body) + "\r\n";
    }
}
