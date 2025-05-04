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
        props.load(new FileReader(gradlePropsPath));
        String keystorePassword = props.getProperty("keystore.password", "httpServer");

        // Load the keystore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        String keystorePath = System.getProperty("user.dir") + "/app/src/main/resources/certs/keystore.jks";
        keyStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());

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