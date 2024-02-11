package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.SMPSocket;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.Command;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.CommandService;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.LoginCommand;

import java.net.SocketException;

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

    public void setIsLoggedOff(boolean loggedOff) {
        this.loggedOff = loggedOff;
    }

    @Override
    public void run() {
        try {
            while (!isLoggedOff()) {
                SMPMessage receivedMessage = getSocket().receiveMessage();
                System.out.println("Received Message: " + receivedMessage);

                SMPMessage response;
                try {
                    Command command = getCommandService().parseCommand(receivedMessage);

                    response = getResponse(command, receivedMessage);
                } catch (ClassNotFoundException cnfe) {
                    response = SMPMessage.InvalidCommandMessage;
                } catch (IllegalArgumentException iae) {
                    response = new SMPMessage(SMPMessage.StatusBadRequest, SMPMessage.CommandServerResponse, iae.getMessage());
                }

                getSocket().sendMessage(response);
                System.out.println("Responded With: " + response);
            }
        } catch (SocketException se) {
            System.out.println("Connection to socket has been reset. Thread is ending.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private SMPMessage getResponse(Command command, SMPMessage receivedMessage) {
        SMPMessage response;

        if (isLoggedIn() || command instanceof LoginCommand) {
            response = command.execute(receivedMessage, this, getServer());
        } else {
            response = SMPMessage.StatusForbiddenMessage;
        }

        return response;
    }
}
