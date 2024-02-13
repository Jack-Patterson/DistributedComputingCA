package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Shared.SMPMessage;
import pattersonjack.distributedcomputingca.Server.SMPServer;
import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Server.ServerServices.SecurityService;

public class LoginCommand extends Command {

    public LoginCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    public SMPMessage execute(SMPMessage receivedMessage, SMPServerThread thread, SMPServer server) {
        if (!getPrefix().equals(receivedMessage.command())) {
            return SMPMessage.InvalidCommandMessage;
        }
        else if (thread.isLoggedIn()) {
            return new SMPMessage(SMPMessage.StatusBadRequest, SMPMessage.CommandServerResponse, "Client is already logged in.");
        }

        String[] splitMessage = receivedMessage.message().trim().split(" ");

        if (splitMessage.length < 2) {
            return new SMPMessage(SMPMessage.StatusForbidden, SMPMessage.CommandServerResponse,
                    "Not enough log in credentials provided. Please ensure that both a username and password were provided.");
        }

        SecurityService securityService = new SecurityService();
        boolean logInSuccessful = securityService.validateLogin(splitMessage[0], splitMessage[1]);

        SMPMessage response;
        if (logInSuccessful) {
            response = new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Log in successful.");
        } else {
            return SMPMessage.StatusForbiddenMessage;
        }

        thread.setIsLoggedIn(logInSuccessful);

        return response;
    }
}
