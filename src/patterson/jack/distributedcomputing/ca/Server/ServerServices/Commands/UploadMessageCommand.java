package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.Server.SMPServer;
import patterson.jack.distributedcomputing.ca.Server.SMPServerThread;

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
