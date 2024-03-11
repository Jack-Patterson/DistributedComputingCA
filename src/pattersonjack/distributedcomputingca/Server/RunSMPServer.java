package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;
import pattersonjack.distributedcomputingca.Shared.HostData;

public class RunSMPServer {

    /**
     * Main method to run the server
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        CommandService commandService = new CommandService();

        int serverPort = HostData.defaultHostData.port();

        SMPServer smpServer = new SMPServer(commandService, serverPort);
        smpServer.run();
    }
}
