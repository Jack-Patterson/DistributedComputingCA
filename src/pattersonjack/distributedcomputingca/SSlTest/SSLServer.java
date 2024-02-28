package pattersonjack.distributedcomputingca.SSlTest;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

public class SSLServer {
    public static void main(String[] args) {
        int port = 8443; // Example port
        try {
            // Load server private key
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(SSLServer.class.getClassLoader().getResourceAsStream("ssl/dcca.jks"), "123456789".toCharArray());

            // Initialize key manager factory with the server key
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "123456789".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
//            trustManagerFactory.init(trustStore);

            // Set up SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // Get server socket factory from the SSL context
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            try (var serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port)) {
                System.out.println("SSL Server is running on port " + port);
                while (true) {
                    try (SSLSocket clientSocket = (SSLSocket) serverSocket.accept()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

                        String message = reader.readLine();
                        System.out.println("Received from client: " + message);
                        writer.println("Hello " + message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

