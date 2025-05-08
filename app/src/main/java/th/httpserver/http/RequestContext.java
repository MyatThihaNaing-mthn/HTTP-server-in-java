package th.httpserver.http;

import java.net.Socket;

public class RequestContext {
    private HttpRequest request;
    private final Socket clientSocket;

    public RequestContext(Socket clientSocket) {
        this.request = null;
        this.clientSocket = clientSocket;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }
    
    public HttpRequest getRequest() {
        return request;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
    
}
