package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.SMPSocket;
import patterson.jack.distributedcomputing.ca.Services.CommandService;

public class SMPServerThread implements Runnable {

    private final SMPServer server;
    private final SMPSocket socket;
    private final CommandService commandService;


    public SMPServerThread(SMPServer server, SMPSocket socket, CommandService commandService) {
        this.server = server;
        this.socket = socket;
        this.commandService = commandService;
    }

    @Override
    public void run() {
        try {
            boolean shouldLogOff = false;
            while (!shouldLogOff) {
                break;
            }

            String httpResponse = "Status: " + 200 + "\n" + "Message: " + "Hello I am Jack";
            System.out.println(httpResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
