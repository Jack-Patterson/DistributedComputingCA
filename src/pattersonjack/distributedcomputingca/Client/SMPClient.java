package pattersonjack.distributedcomputingca.Client;

import pattersonjack.distributedcomputingca.Shared.Commands.CommandService;
import pattersonjack.distributedcomputingca.Shared.HostData;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;
import pattersonjack.distributedcomputingca.Shared.SMPSocket;

import java.io.IOException;
import java.net.InetAddress;

public class SMPClient {

    private final SMPSocket socket;
    private final CommandService commandService;

    public SMPClient(HostData hostData) throws IOException {
        InetAddress host = InetAddress.getByName(hostData.hostname());
        socket = new SMPSocket(host, hostData.port());
        commandService = new CommandService();

        System.out.println(socket.receiveMessage().message());
    }

    public void sendMessage(String messageAsText) throws IOException, ClassNotFoundException {

        SMPMessage message = commandService.parseText(messageAsText);

        socket.sendMessage(message);
    }

    public SMPMessage receiveMessage() throws IOException {
        SMPMessage message = socket.receiveMessage();

        if (message.command().equals(SMPMessage.CommandServerResponseLogout)){
            try {
                socket.closeConnection();
            }
            catch (Exception ignored){}
        }

        return message;
    }
}
