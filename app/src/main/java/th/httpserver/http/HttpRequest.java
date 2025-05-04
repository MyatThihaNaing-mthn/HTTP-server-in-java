package th.httpserver.http;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Collections;
import java.util.HashMap;

public class HttpRequest {
    private HttpMethod method;
    private String version;
    private String path;
    private String body;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, List<String>> multiParams;
    private Map<String, String> pathParams;

    public HttpRequest(Builder builder) {
        this.method = Objects.requireNonNull(builder.method, "Method is required");
        this.version = Objects.requireNonNull(builder.version, "Version is required");
        this.path = Objects.requireNonNull(builder.path, "Path is required");
        this.body = builder.body;
        this.headers = Collections
                .unmodifiableMap(new HashMap<>(builder.headers != null ? builder.headers : new HashMap<>()));
        this.queryParams = Collections
                .unmodifiableMap(new HashMap<>(builder.queryParams != null ? builder.queryParams : new HashMap<>()));
        this.multiParams = Collections
                .unmodifiableMap(new HashMap<>(builder.multiParams != null ? builder.multiParams : new HashMap<>()));
        this.pathParams = Collections
                .unmodifiableMap(new HashMap<>(builder.pathParams != null ? builder.pathParams : new HashMap<>()));
    }

    // TODO: Find a better way to mutate the pathParams
    public HttpRequest withPathParams(Map<String, String> pathParams) {
        return new Builder()
                .method(this.method)
                .version(this.version)
                .path(this.path)
                .body(this.body)
                .headers(this.headers)
                .queryParams(this.queryParams)
                .multiParams(this.multiParams)
                .pathParams(pathParams)
                .build();
    }

    public Optional<String> getPathParam(String key) {
        return Optional.ofNullable(pathParams.get(key));
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getBody() {
        return body;
    }

    public Optional<String> getQueryParam(String key) {
        return Optional.ofNullable(queryParams.get(key));
    }

    public List<String> getMultiParam(String key) {
        return multiParams.getOrDefault(key, List.of());
    }

    public String getHeader(String key) {
        if (headers == null)
            return null;
        return headers.getOrDefault(key, null);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public static class Builder {
        private HttpMethod method;
        private String version;
        private String path;
        private String body;
        private Map<String, String> headers;
        private Map<String, String> queryParams;
        private Map<String, List<String>> multiParams;
        private Map<String, String> pathParams;
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder queryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder multiParams(Map<String, List<String>> multiParams) {
            this.multiParams = multiParams;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder pathParams(Map<String, String> pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequest build() {
            if (method == null) {
                throw new IllegalStateException("HTTP method must be specified");
            }
            if (version == null || !version.matches("HTTP/\\d\\.\\d")) {
                throw new IllegalStateException("Invalid HTTP version format");
            }
            if (path == null || path.isEmpty() || !path.startsWith("/")) {
                throw new IllegalStateException("Path must start with /");
            }
            return new HttpRequest(this);
        }
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", version='" + version + '\'' +
                ", path='" + path + '\'' +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", queryParams=" + queryParams +
                ", multiParams=" + multiParams +
                ", pathParams=" + pathParams +
                '}';
    }
}
