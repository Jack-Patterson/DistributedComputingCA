package pattersonjack.distributedcomputingca.SSlTest;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class EchoClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 1234;

        // Set system properties for truststore
        System.setProperty("javax.net.ssl.trustStore", "ssl/public.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456789");

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Load truststore
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream trustStoreIS = new FileInputStream("ssl/public.jks")) {
                trustStore.load(trustStoreIS, "123456789".toCharArray());
            }

            // Initialize trust manager factory
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Initialize SSL context
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory factory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket(hostname, port);

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while (true) {
                System.out.println("Enter a message (or an empty line to quit):");
                userInput = stdIn.readLine();

                if (userInput == null || userInput.isEmpty()) {
                    output.println(userInput);
                    break;
                }

                output.println(userInput);
                String response = input.readLine();
                System.out.println("Server response: " + response);
            }

        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException |
                 KeyManagementException ex) {
            System.out.println("I/O error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
