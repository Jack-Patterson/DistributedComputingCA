package pattersonjack.distributedcomputingca.Client;

import pattersonjack.distributedcomputingca.Shared.HostData;
import pattersonjack.distributedcomputingca.Shared.SMPMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunSMPClientCli {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        try {
            HostData hostData = getHostData(reader);

            SMPClient client = new SMPClient(hostData);
            runCli(client, reader);
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }

    private static void runCli(SMPClient client, BufferedReader reader) {
        boolean shouldEndConnection = false;
        while (!shouldEndConnection) {

            System.out.println("""
                    You may now send commands to the server. The commands are as follows:
                    1. login [username] [password] - Used for logging in to the server.
                    2. logout - Used for logging out of the server.
                    3. uploadmessage [message] - Used to upload a message to the server.
                    4. getmessages [(OPTIONAL) index] - Will retrieve all messages from the server.
                    Please input commands with all required arguments as shown above. e.g. login jack 12345
                    \s""");

            try {
                String message = reader.readLine();

                client.sendMessage(message);
                SMPMessage response = client.receiveMessage();

                if (response.command().equals(SMPMessage.CommandServerResponseLogout)) {
                    shouldEndConnection = true;
                }

                System.out.println(response.message() + "\n");
            } catch (IllegalArgumentException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static HostData getHostData(BufferedReader reader) throws IOException {
        System.out.println("Please enter the hostname of the server.");
        String hostname = reader.readLine();
        if (hostname.isEmpty())
            hostname = HostData.defaultHostData.hostname();

        System.out.println("Please enter the port of the server.");
        String portStr = reader.readLine();
        int port;
        if (portStr.isEmpty())
            port = HostData.defaultHostData.port();
        else
            port = Integer.parseInt(portStr);

        return new HostData(hostname, port);
    }
}
