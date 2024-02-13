package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Shared.SMPMessage;
import pattersonjack.distributedcomputingca.Server.SMPServer;
import pattersonjack.distributedcomputingca.Server.SMPServerThread;

public class LogoutCommand extends Command {
    public LogoutCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread, SMPServer server) {
        thread.setIsLoggedOff(true);

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponseLogout, "Logging off.");
    }
}
