package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public class LogoutCommand extends Command {
    public LogoutCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread) {
        thread.setIsLoggedOff(true);

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponseLogout, "Logging off.");
    }
}
