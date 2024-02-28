package pattersonjack.distributedcomputingca.Server;

import pattersonjack.distributedcomputingca.Shared.HostData;
import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class RunSMPServer {

    public static void main(String[] args) throws UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
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
