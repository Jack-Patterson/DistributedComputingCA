package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.Server.SMPServerThread;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.SecurityService;

public class LoginCommand extends Command {

    public LoginCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    public SMPMessage execute(SMPMessage receivedMessage, SMPServerThread thread) {
        String[] splitMessage = receivedMessage.message().trim().split(" ");

        SecurityService securityService = new SecurityService();
        boolean logInSuccessful = securityService.validateLogin(splitMessage[0], splitMessage[1]);

        SMPMessage response;
        if (logInSuccessful) {
            response = new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Log in successful.");
        } else {
            response = SMPMessage.StatusForbiddenMessage;
        }

        thread.setIsLoggedIn(logInSuccessful);

        return response;
    }
}
