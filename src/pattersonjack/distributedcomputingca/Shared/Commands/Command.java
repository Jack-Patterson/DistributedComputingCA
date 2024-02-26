package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Server.SMPServerThread;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

public abstract class Command {
    private final String prefix;
    private final int argumentsCount;

    public static String loginPrefix = "login";
    public static String logoutPrefix = "logout";
    public static String uploadMessagePrefix = "uploadmessage";
    public static String getMessagesPrefix = "getmessages";

    public Command(String prefix, int argumentsCount) {
        this.prefix = prefix;
        this.argumentsCount = argumentsCount;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getArgumentsCount() {
        return argumentsCount;
    }

    public abstract SMPMessage execute(SMPMessage sentMessage, SMPServerThread thread);
}
