package th.httpserver.tls;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class TLSServer {

    public static SSLContext createSSLContext() throws Exception {
        // Read password from gradle.properties
        Properties props = new Properties();
        String gradlePropsPath = System.getProperty("user.dir") + "/gradle.properties";
        String keyStoreFilePath = System.getProperty("user.dir") + "/src/main/resources/certs/keystore.jks";
        try {
            props.load(new FileReader(gradlePropsPath));
        } catch (Exception e) {
            System.err.println("Warning: Could not load gradle.properties, using default password");
            System.err.println("Expected path: " + gradlePropsPath);
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
        }
        String keystorePassword = props.getProperty("keystore.password", "httpServer");

        // Load the keystore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try {
            keyStore.load(new FileInputStream(keyStoreFilePath), keystorePassword.toCharArray());
        } catch (Exception e) {
            System.err.println("Error: Could not load keystore");
            System.err.println("Expected path: " + keyStoreFilePath);
            System.err.println("Current working directory: " + System.getProperty("user.dir"));
            throw e;
        }

        // Initialize KeyManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

        // Create and initialize SSLContext with TLS 1.2
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(
            keyManagerFactory.getKeyManagers(),
            null, // No trust managers needed for server
            null
        );

        return sslContext;
    }
}