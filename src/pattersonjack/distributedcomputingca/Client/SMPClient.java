package pattersonjack.distributedcomputingca.Client;

import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;
import pattersonjack.distributedcomputingca.Shared.HostData;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;
import pattersonjack.distributedcomputingca.Shared.SMPSocket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SMPClient {

    private final SMPSocket socket;
    private final CommandService commandService;

    public SMPClient(HostData hostData) throws IOException {
        socket = new SMPSocket(getSSLSocket(hostData));
        commandService = new CommandService();

        System.out.println(socket.receiveMessage().message());
    }

    /**
     * Sends a message to the server.
     *
     * @param messageAsText The message to send to the server.
     * @throws ClassNotFoundException If the command in the message cannot be parsed.
     */
    public void sendMessage(String messageAsText) throws ClassNotFoundException {

        SMPMessage message = commandService.parseText(messageAsText);

        socket.sendMessage(message);
    }

    /**
     * Receives a message from the server.
     *
     * @return The message received from the server.
     * @throws IOException If an I/O error occurs when receiving the message.
     */
    public SMPMessage receiveMessage() throws IOException {
        SMPMessage message = socket.receiveMessage();

        if (message.command().equals(SMPMessage.CommandServerResponseLogout)) {
            try {
                socket.closeConnection();
            } catch (Exception ignored) {
            }
        }

        return message;
    }

    /**
     * Gets an SSL Socket.
     *
     * @param hostData The hostname and port number.
     * @return The SSL Socket.
     */
    private SSLSocket getSSLSocket(HostData hostData) {
        SSLContext sslContext = getSSLContext();
        initializeSSLContext(sslContext);

        SSLSocket sslSocket = null;
        try {
            SSLSocketFactory factory = sslContext.getSocketFactory();
            sslSocket = (SSLSocket) factory.createSocket(hostData.hostname(), hostData.port());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sslSocket;
    }

    /**
     * Retrieves the SSL Context.
     *
     * @return The SSL Context.
     */
    private SSLContext getSSLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext;
    }

    /**
     * Initializes the SSL Context with the trust store.
     *
     * @param sslContext The SSL Context to initialize.
     */
    private void initializeSSLContext(SSLContext sslContext) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream trustStoreIS = new FileInputStream("ssl/public.jks")) {
                trustStore.load(trustStoreIS, "123456789".toCharArray());
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException | IOException |
                 CertificateException e) {
            e.printStackTrace();
        }
    }
}
