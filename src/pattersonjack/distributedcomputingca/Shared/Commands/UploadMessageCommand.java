package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Shared.SMPMessage;
import pattersonjack.distributedcomputingca.Server.SMPServer;
import pattersonjack.distributedcomputingca.Server.SMPServerThread;

public class UploadMessageCommand extends Command {
    public UploadMessageCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread, SMPServer server) {

        server.getMessages().add(sentMessage.message());

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Message saved successfully.");
    }
}
