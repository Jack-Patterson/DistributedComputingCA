package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public class GetMessageCommand extends Command {
    public GetMessageCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    /**
     * Returns a message from the server's message list.
     *
     * @param sentMessage The message sent by the client.
     * @param thread      The server thread that received the message.
     * @return A message to be sent back to the client.
     */
    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread) {
        String message;

        String argument = sentMessage.message().trim().split(" ")[0];

        if (argument.equals("-1")) {
            message = "Messages: " + thread.getMessages();
        } else {
            try {
                int index = Integer.parseInt(argument);

                if (index < 0)
                    index = Math.abs(index);

                if (index == 0)
                    message = "Message at position " + (index + 1) + ": " + thread.getMessages().get(index);
                else
                    message = "Message at position " + index + ": " + thread.getMessages().get(index - 1);
            } catch (NumberFormatException nfe) {
                return new SMPMessage(SMPMessage.StatusBadRequest, SMPMessage.CommandServerResponse, "Argument was not an integer. Please try again.");
            } catch (IndexOutOfBoundsException ioobe) {
                return new SMPMessage(SMPMessage.StatusBadRequest, SMPMessage.CommandServerResponse,
                        "There are not enough messages for that position. There are currently only " + thread.getMessages().size() + " messages.");
            }
        }

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, message);
    }
}
