package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.SMPSocket;
import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SMPServer {
    private final ArrayList<String> messages;
    private final CommandService commandService;
    private final int serverPort;

    public SMPServer(CommandService commandService, int serverPort) {
        this.messages = new ArrayList<>();
        this.commandService = commandService;
        this.serverPort = serverPort;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
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

    private SMPSocket waitForAndReceiveConnection(ServerSocket serverSocket) throws IOException {
        Socket connection = serverSocket.accept();
        return new SMPSocket(connection);
    }

    private Thread createNewThread(SMPSocket socket) {
        SMPServerThread smpServerThread = new SMPServerThread(this, socket, commandService);

        return new Thread(smpServerThread);
    }
}
