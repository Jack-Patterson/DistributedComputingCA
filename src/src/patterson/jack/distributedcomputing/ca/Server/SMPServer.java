package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.SMPSocket;
import patterson.jack.distributedcomputing.ca.Services.CommandService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void addMessage(String message) {
        messages.add(message);
    }

    public int getServerPort() {
        return serverPort;
    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("SMP Server Ready");

            while (true) {

                System.out.println("Waiting to receive a connection...");
                SMPSocket smpSocket = waitForAndReceiveConnection(serverSocket);
                System.out.println("Connection Accepted.");

                createNewThread(smpSocket);
                System.out.println("Connection now ready to send and receive messages.");
            }
        } catch (IOException ioException) {
            System.err.println("Error opening socket. " + Arrays.toString(ioException.getStackTrace()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private SMPSocket waitForAndReceiveConnection(ServerSocket serverSocket) throws IOException {
        Socket connection = serverSocket.accept();
        return new SMPSocket(connection);
    }

    private void createNewThread(SMPSocket socket) {
        SMPServerThread smpServerThread = new SMPServerThread(this, socket, commandService);
        Thread thread = new Thread(smpServerThread);
        thread.start();
    }
}
