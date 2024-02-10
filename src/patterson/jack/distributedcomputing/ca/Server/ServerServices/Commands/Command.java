package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.Server.SMPServer;
import patterson.jack.distributedcomputing.ca.Server.SMPServerThread;

public abstract class Command {
    private final String prefix;
    private final int argumentsCount;

    public static String loginPrefix = "login";
    public static String logoutPrefix = "logout";
    public static String uploadMessagePrefix = "uploadmessage";
    public static String getMessagesPrefix = "getmessages";

    public Command (String prefix, int argumentsCount){
        this.prefix = prefix;
        this.argumentsCount = argumentsCount;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public abstract SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread, SMPServer server);
}
