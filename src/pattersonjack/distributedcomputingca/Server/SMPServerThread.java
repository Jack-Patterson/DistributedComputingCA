package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.Commands.Command;
import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;
import pattersonjack.distributedcomputingca.Shared.Commands.LoginCommand;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;
import pattersonjack.distributedcomputingca.Shared.SMPSocket;

import java.net.SocketException;
import java.util.ArrayList;

public class SMPServerThread implements Runnable {

    private final SMPServer server;
    private final SMPSocket socket;
    private final CommandService commandService;
    private final ArrayList<String> messages;
    private boolean isLoggedIn = false;
    boolean loggedOff = false;


    public SMPServerThread(SMPServer server, SMPSocket socket, CommandService commandService) {
        this.messages = new ArrayList<>();
        this.server = server;
        this.socket = socket;
        this.commandService = commandService;
    }

    public ArrayList<String> getMessages() {
        return messages;
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

    /**
     * This method is called when the thread is started.
     * It listens for messages from the client and responds to them.
     */
    @Override
    public void run() {
        try {
            SMPMessage connectionAccepted = new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Connection has been accepted.");
            getSocket().sendMessage(connectionAccepted);

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

            socket.closeConnection();
        } catch (SocketException se) {
            System.out.println("Connection to socket has been reset. Thread is ending.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method is called when the thread is started.
     * It listens for messages from the client and responds to them.
     */
    private SMPMessage getResponse(Command command, SMPMessage receivedMessage) {
        SMPMessage response;

        if (isLoggedIn() || command instanceof LoginCommand) {
            response = command.execute(receivedMessage, this);
        } else {
            response = SMPMessage.StatusForbiddenMessage;
        }

        return response;
    }
}
