package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.Server.ServerServices.SecurityService;

public class CommandService {
    public static Command LoginCommand = new LoginCommand(new String[] {"login", "li"}, 2);
    public static Command LogoutCommand = new LoginCommand(new String[]{"logout", "lo"}, 0);
    public static Command UploadMessageCommand = new LoginCommand(new String[]{"uploadmessage", "upload", "send", "um"}, 1);
    public static Command GetMessagesCommand = new LoginCommand(new String[]{"getmessages", "gm"}, 1);

    public CommandService(SecurityService securityService) {
    }

    public void parseCommand(String message, Command command){

    }
}
