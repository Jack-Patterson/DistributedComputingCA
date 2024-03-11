package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public class UploadMessageCommand extends Command {
    public UploadMessageCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    /**
     * Uploads a message to the server
     *
     * @param sentMessage The message to upload
     * @param thread      The thread that sent the message
     * @return A message indicating the status of the upload
     */
    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread) {

        thread.getMessages().add(sentMessage.message());

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, "Message saved successfully.");
    }
}
