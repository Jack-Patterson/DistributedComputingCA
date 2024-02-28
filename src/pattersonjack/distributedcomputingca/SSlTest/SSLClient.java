package pattersonjack.distributedcomputingca.SSlTest;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
public class SSLClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8443; // Must match server's port
        try {
            // Get the default SSL socket factory
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port)) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

                String message = "World";
                writer.println(message);
                System.out.println("Sent to server: " + message);

                String response = reader.readLine();
                System.out.println("Received from server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}