package th.httpserver.http;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    TOO_MANY_REQUESTS(429, "Too Many Requests");

    private int code;
    private String message;

    private HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
   
    public String getMessage() {
        return message;
    }
}
