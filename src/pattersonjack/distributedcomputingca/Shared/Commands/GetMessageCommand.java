package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public class GetMessageCommand extends Command {
    public GetMessageCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

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
                    message = "Message at index " + (index + 1) + ": " + thread.getMessages().get(index);
                else
                    message = "Message at index " + index + ": " + thread.getMessages().get(index - 1);
            } catch (NumberFormatException nfe) {
                return new SMPMessage(SMPMessage.StatusBadRequest, SMPMessage.CommandServerResponse, "Argument was not an integer.");
            } catch (IndexOutOfBoundsException ioobe) {
                return new SMPMessage(SMPMessage.StatusBadRequest, SMPMessage.CommandServerResponse,
                        "There are not enough messages for that index. Please input a lower argument.");
            }
        }

        return new SMPMessage(SMPMessage.StatusOk, SMPMessage.CommandServerResponse, message);
    }
}
