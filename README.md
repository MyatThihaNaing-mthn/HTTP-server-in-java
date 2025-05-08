# Custom HTTP Server in Java

A custom HTTP server implementation built from scratch in Java for educational purposes. This project demonstrates the fundamental concepts of how web servers work, including HTTP protocol implementation, request handling, and server architecture. By building this server, you can gain a deep understanding of:

- HTTP protocol internals and how requests/responses are processed
- Network programming and socket handling
- Concurrent request processing and thread management
- Web server security concepts and implementations
- Middleware architecture and request pipeline
- Static file serving and content type handling

This project serves as a learning resource for developers who want to understand the underlying mechanisms of web servers beyond just using them.

## Features

- **HTTPS Redirection**
  - Automatic redirection from HTTP to HTTPS
  - 301 Moved Permanently response
  - Preserves original request path
  - Secure by default approach
  - Example:
    ```
    HTTP Request to http://localhost:4220/echo
    ↓
    301 Moved Permanently
    Location: https://localhost:4221/echo
    ↓
    HTTPS Request to https://localhost:4221/echo
    ```

- **Security Features**
  - Rate limiting to prevent abuse
  - Authentication middleware (Coming soon)
  - HTTPS support with TLS
  - Static file serving with path traversal protection
  - Secure headers implementation

- **Performance Optimizations**
  - Thread pool for concurrent request handling
  - Keep-alive connection support
  - Efficient file reading with buffering
  - Resource cleanup and management
  - Compression (Coming soon)
  - Chunked transfer encoding (Coming soon)

- **Middleware Architecture**
  - Extensible middleware chain
  - Authentication middleware (Coming soon)
  - Rate limiting middleware
  - Static file serving middleware
  - Custom middleware support
  - Request parsing middleware

- **File Handling**
  - Static file serving
  - Automatic content type detection
  - Secure file access
  - Support for common file types (HTML, CSS, JS, images, etc.)

## Prerequisites

- Java 11 or higher
- Gradle 7.0 or higher
- SSL certificate for HTTPS (included in the project)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MyatThihaNaing-mthn/HTTP-server-in-java
   cd http-server-in-java
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the server:
   ```bash
   ./gradlew run
   ```

## Configuration- Comming soon

The server comes with a pre-configured SSL certificate for development:
- Keystore location: `app/src/main/resources/certs/keystore.jks`
- Default password: "httpServer"
- TLS version: 1.2

## Project Structure

```
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── th/
│   │   │   │       └── httpserver/
│   │   │   │           ├── annotations/    # Custom annotations for routing
│   │   │   │           ├── controllers/    # Request handlers
│   │   │   │           ├── http/          # HTTP protocol implementation
│   │   │   │           ├── middlwares/    # Middleware components
│   │   │   │           ├── routes/        # Route definitions
│   │   │   │           ├── tls/           # SSL/TLS implementation
│   │   │   │           └── utils/         # Utility classes
│   │   │   └── resources/
│   │   │       └── certs/                 # SSL certificates
│   │   └── test/
│   └── build.gradle.kts
└── public/                               # Static files directory
```

## Architecture

The server follows a middleware-based architecture:

1. **Request Processing**
   - Socket connection handling
   - Request parsing
   - Middleware chain execution
   - Response generation

2. **Middleware Chain**
   - HTTP request Parsing
   - Rate Limiting
   - Authentication (Coming soon)
   - Static File Serving

3. **Security Layer**
   - TLS/SSL support
   - Rate limiting
   - Authentication (Coming soon)


### Response Headers

- `Content-Type`: Automatically set based on file type
- `Connection`: Supports keep-alive
- Custom headers can be added through middleware pipeline


## Performance

- Thread pool for concurrent connections
- Keep-alive connection support
- Compression (Coming soon)
- Chunking (Coming soon)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AnyFeature`)
3. Commit your changes (`git commit -m 'Add AnyFeature'`)
4. Push to the branch (`git push origin feature/AnyFeature`)
5. Open a Pull Request

## Contact

Myat Thiha Naing - myatthiha.mthn@gmail.com

