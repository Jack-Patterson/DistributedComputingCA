package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;
import pattersonjack.distributedcomputingca.Shared.SMPSocket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    public void run() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        String ksName = "ssl/dcca.jks";
        char ksPass[] = "t00217640".toCharArray();
        char ctPass[] = "t00217640".toCharArray();

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ksName), ksPass);

        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ctPass);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();


        try (SSLServerSocket serverSocket
                     = (SSLServerSocket) ssf.createServerSocket(8888);) {
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

    private SMPSocket waitForAndReceiveConnection(SSLServerSocket serverSocket) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        SSLSocket c = (SSLSocket) serverSocket.accept();
        return new SMPSocket(c);
    }

    private Thread createNewThread(SMPSocket socket) {
        SMPServerThread smpServerThread = new SMPServerThread(this, socket, commandService);

        return new Thread(smpServerThread);
    }
}
