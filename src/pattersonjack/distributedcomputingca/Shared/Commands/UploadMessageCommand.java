package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public class UploadMessageCommand extends Command {
    public UploadMessageCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread) {

        thread.getMessages().add(sentMessage.message());

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Message saved successfully.");
    }
}
