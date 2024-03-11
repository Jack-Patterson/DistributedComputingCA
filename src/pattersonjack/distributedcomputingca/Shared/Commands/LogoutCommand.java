package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public class LogoutCommand extends Command {
    public LogoutCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    /**
     * Logs the user off the server.
     *
     * @param sentMessage The message sent by the client.
     * @param thread      The server thread that sent the message.
     * @return A message to be sent back to the client.
     */
    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread) {
        thread.setIsLoggedOff(true);

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponseLogout, "Logging off.");
    }
}
