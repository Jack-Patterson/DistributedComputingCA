package pattersonjack.distributedcomputingca.Shared;

import com.google.gson.Gson;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SMPSocket {

    private final Socket socket;
    private final Gson serializer;
    private BufferedReader input;
    private PrintWriter output;

    public SMPSocket(HostData hostData) throws IOException {
        this.serializer = new Gson();
        this.socket = new Socket(InetAddress.getByName(hostData.hostname()), hostData.port());
        setStreams();
    }

    public SMPSocket(Socket socket) throws IOException {
        this.serializer = new Gson();
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        InputStream inStream = socket.getInputStream();
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        input = new BufferedReader(inStreamReader);

        OutputStream outStream = socket.getOutputStream();
        OutputStreamWriter outStreamReader = new OutputStreamWriter(outStream);
        output = new PrintWriter(outStreamReader);
    }

    public void sendMessage(int statusCode, String command, String message) throws IOException {
        String messageToSend = serializer.toJson(new SMPMessage(statusCode, command, message));

        output.print(messageToSend + "\n");
        output.flush();
    }

    public void sendMessage(SMPMessage smpMessage) throws IOException {
        String messageToSend = serializer.toJson(smpMessage);

        output.print(messageToSend + "\n");
        output.flush();
    }

    public SMPMessage receiveMessage() throws IOException {
        return serializer.fromJson(input.readLine(), SMPMessage.class);
    }

    public void closeConnection() throws IOException {
        socket.close();
    }
}
