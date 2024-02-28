package pattersonjack.distributedcomputingca.SSlTest;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class EchoServer {
    public static void main(String[] args) {
        int port = 1234; // Port number to listen on

        // Set system properties for keystore
//        System.setProperty("javax.net.ssl.keyStore", "ssl/dcca.jks");
//        System.setProperty("javax.net.ssl.keyStorePassword", "123456789");

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Load keystore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream keyStoreIS = new FileInputStream("ssl/dcca.jks")) {
                keyStore.load(keyStoreIS, "123456789".toCharArray());
            }

            // Initialize key manager factory
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "123456789".toCharArray());

            // Initialize SSL context
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
            System.out.println("SSL Server is listening on port " + port);

            while (true) {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                System.out.println("New client connected");

                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                    String message;
                    while ((message = input.readLine()) != null) {
                        if (message.isEmpty()) {
                            System.out.println("Client sent exit command. Closing connection.");
                            break;
                        }
                        System.out.println("Received from client: " + message);
                        output.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Exception in handling client connection: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    socket.close();
                    System.out.println("Connection with client closed");
                    break;
                }
            }
        } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | CertificateException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
