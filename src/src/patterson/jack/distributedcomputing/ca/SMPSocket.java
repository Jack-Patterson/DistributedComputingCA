package patterson.jack.distributedcomputing.ca;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SMPSocket {

    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public SMPSocket(InetAddress acceptorHost, int acceptorPort) throws IOException {
        this.socket = new Socket(acceptorHost, acceptorPort);
        setStreams();
    }

    public SMPSocket(Socket socket) throws IOException {
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

    public void sendMessage(int statusCode, String message) throws IOException {
        String messageToSend = "Status: " + statusCode + ", Message: " + message;

        output.print(message + "\n");
        output.flush();
    }

    public void sendMessage(SMPMessage smpMessage) throws IOException {
        output.print(smpMessage.toString() + "\n");
        output.flush();
    }

    public SMPMessage receiveMessage() throws IOException {

        return SMPMessage.parseMessageString(input.readLine());
    }
}
