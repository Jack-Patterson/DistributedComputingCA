package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;
import pattersonjack.distributedcomputingca.Shared.SMPSocket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SMPServer {
    private final CommandService commandService;
    private final int serverPort;

    public SMPServer(CommandService commandService, int serverPort) {
        this.commandService = commandService;
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    /**
     * Starts the server and listens for incoming connections. Then creates a new thread for each connection.
     */
    public void run() {
        SSLServerSocketFactory ssf = getSSLServerSocketFactory();

        try (SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(getServerPort())) {
            System.out.println("SMP Server Ready");

            while (true) {

                System.out.println("Waiting to receive a connection...");
                SMPSocket smpSocket = waitForAndReceiveConnection(serverSocket);
                System.out.println("Connection Accepted.");

                Thread thread = createNewThread(smpSocket);
                thread.start();
                System.out.println("Connection now ready to send and receive messages.");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Waits for a connection to be made and then accepts it.
     *
     * @param serverSocket The server socket to accept the connection from.
     * @return The SMPSocket that represents the connection.
     * @throws IOException If an I/O error occurs when waiting for a connection.
     */
    private SMPSocket waitForAndReceiveConnection(SSLServerSocket serverSocket) throws IOException {

        SSLSocket connection = (SSLSocket) serverSocket.accept();
        return new SMPSocket(connection);
    }

    /**
     * Creates a new thread for the given socket.
     *
     * @param socket The socket to create a new thread for.
     * @return The new thread.
     */
    private Thread createNewThread(SMPSocket socket) {
        SMPServerThread smpServerThread = new SMPServerThread(this, socket, commandService);

        return new Thread(smpServerThread);
    }

    /**
     * Gets the SSLServerSocketFactory for the server.
     *
     * @return The SSLServerSocketFactory for the server.
     */
    private SSLServerSocketFactory getSSLServerSocketFactory() {
        SSLContext sslContext = getSSLContext();
        initializeSSLContext(sslContext);

        return sslContext.getServerSocketFactory();
    }

    /**
     * Gets the SSLContext for the server.
     *
     * @return The SSLContext for the server.
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
     * Initializes the SSLContext for the server with the trust store.
     *
     * @param sslContext The SSLContext to initialize.
     */
    private void initializeSSLContext(SSLContext sslContext) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream trustStoreIS = new FileInputStream("ssl/dcca.jks")) {
                keyStore.load(trustStoreIS, "123456789".toCharArray());
            }

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "123456789".toCharArray());

            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException | IOException |
                 CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }
}
