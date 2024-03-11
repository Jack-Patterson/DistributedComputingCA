package pattersonjack.distributedcomputingca.Shared;

import com.google.gson.Gson;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class SMPSocket {

    private final SSLSocket socket;
    private final Gson serializer;
    private BufferedReader input;
    private PrintWriter output;

    public SMPSocket(SSLSocket socket) throws IOException {
        this.serializer = new Gson();
        this.socket = socket;
        setStreams();
    }

    /**
     * This method is used to create the input and output streams for the socket.
     *
     * @throws IOException if there is an error creating the streams.
     */
    private void setStreams() throws IOException {
        InputStream inStream = socket.getInputStream();
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        input = new BufferedReader(inStreamReader);

        OutputStream outStream = socket.getOutputStream();
        OutputStreamWriter outStreamReader = new OutputStreamWriter(outStream);
        output = new PrintWriter(outStreamReader);
    }

    /**
     * This method is used to send a message to the other socket.
     *
     * @param smpMessage the message to send.
     */
    public void sendMessage(SMPMessage smpMessage) {
        String messageToSend = serializer.toJson(smpMessage);

        output.print(messageToSend + "\n");
        output.flush();
    }

    /**
     * This method is used to receive a message from the other socket.
     *
     * @return the message received.
     * @throws IOException if there is an error reading the message.
     */
    public SMPMessage receiveMessage() throws IOException {
        return serializer.fromJson(input.readLine(), SMPMessage.class);
    }

    /**
     * This method is used to close the connection to the other socket.
     *
     * @throws IOException if there is an error closing the connection.
     */
    public void closeConnection() throws IOException {
        socket.close();
    }
}
