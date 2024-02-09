package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.Services.CommandService;
import patterson.jack.distributedcomputing.ca.Services.SecurityService;

public class RunSMPServer {

    private static final int defaultServerPort = 7;

    public static void main(String[] args) {
        SecurityService securityService = new SecurityService();
        CommandService commandService = new CommandService(securityService);

        int serverPort;
        if (args.length >= 1) {
            serverPort = Integer.parseInt(args[0]);
        }
        else {
            serverPort = defaultServerPort;
        }

        SMPServer smpServer = new SMPServer(commandService, serverPort);
        smpServer.runServer();
    }
}
