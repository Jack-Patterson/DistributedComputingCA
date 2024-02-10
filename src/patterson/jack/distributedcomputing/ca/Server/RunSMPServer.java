package patterson.jack.distributedcomputing.ca.Server;

import patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands.CommandService;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.SecurityService;

public class RunSMPServer {

    private static final int defaultServerPort = 7;

    public static void main(String[] args) {
        CommandService commandService = new CommandService();

        int serverPort;
        if (args.length >= 1) {
            serverPort = Integer.parseInt(args[0]);
        } else {
            serverPort = defaultServerPort;
        }

        SMPServer smpServer = new SMPServer(commandService, serverPort);
        smpServer.runServer();
    }
}
