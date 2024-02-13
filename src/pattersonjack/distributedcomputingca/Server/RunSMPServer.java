package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.HostData;
import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;

public class RunSMPServer {

    public static void main(String[] args) {
        CommandService commandService = new CommandService();

        int serverPort;
        if (args.length >= 1) {
            serverPort = Integer.parseInt(args[0]);
        } else {
            serverPort = HostData.defaultHostData.port();
        }

        SMPServer smpServer = new SMPServer(commandService, serverPort);
        smpServer.run();
    }
}
