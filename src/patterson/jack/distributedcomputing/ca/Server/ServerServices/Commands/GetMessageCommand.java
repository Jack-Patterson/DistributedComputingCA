package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.Server.SMPServer;
import patterson.jack.distributedcomputing.ca.Server.SMPServerThread;

public class GetMessageCommand extends Command {
    public GetMessageCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread, SMPServer server) {
        String message;

        String argument = sentMessage.message().trim().split(" ")[0];

        if (argument.equals("-1")) {
            message = "Messages: " + server.getMessages();
        } else {
            try {
                int index = Integer.parseInt(argument);

                message = "Message at index " + index + ": " + server.getMessages().get(index);
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
