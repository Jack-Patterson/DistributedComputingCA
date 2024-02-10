package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.Server.SMPServer;
import patterson.jack.distributedcomputing.ca.Server.SMPServerThread;

public class LogoutCommand extends Command {
    public LogoutCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread, SMPServer server) {
        thread.setIsLoggedOff(true);

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Logging off.");
    }
}
