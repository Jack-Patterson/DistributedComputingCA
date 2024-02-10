package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.SMPSocket;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.Command;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.CommandService;

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
                SMPMessage receivedMessage = socket.receiveMessage();
                Command command = commandService.parseCommand(receivedMessage.getMessage());

                command.execute(server, this);

                server.addMessage(receivedMessage.getMessage());

                socket.sendMessage(receivedMessage);

                System.out.println(receivedMessage);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
