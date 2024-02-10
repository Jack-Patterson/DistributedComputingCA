package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.SMPSocket;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.Command;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.CommandService;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.LoginCommand;

import java.io.IOException;

public class SMPServerThread implements Runnable {

    private final SMPServer server;
    private final SMPSocket socket;
    private final CommandService commandService;
    private boolean isLoggedIn = false;
    boolean loggedOff = false;


    public SMPServerThread(SMPServer server, SMPSocket socket, CommandService commandService) {
        this.server = server;
        this.socket = socket;
        this.commandService = commandService;
    }

    public SMPServer getServer() {
        return server;
    }

    public SMPSocket getSocket() {
        return socket;
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedOff() {
        return loggedOff;
    }

    public void setLoggedOff(boolean loggedOff) {
        this.loggedOff = loggedOff;
    }

    @Override
    public void run() {
        try {
            while (!isLoggedOff()) {
                sendAndReceiveMessage();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void sendAndReceiveMessage() throws IOException, ClassNotFoundException {
        SMPMessage receivedMessage = getSocket().receiveMessage();
        System.out.println("Received Message: " + receivedMessage);

        Command command = getCommandService().parseCommand(receivedMessage);
        SMPMessage response;

        if (isLoggedIn || command instanceof LoginCommand) {
            response = command.execute(receivedMessage, this);
        } else {
            response = SMPMessage.StatusForbiddenMessage;
        }

        getSocket().sendMessage(response);

        System.out.println("Responded With: " + response);
    }
}
