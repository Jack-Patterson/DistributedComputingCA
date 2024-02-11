package pattersonjack.distributedcomputingca.Client;

import pattersonjack.distributedcomputingca.HostData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunSMPClientCli {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            HostData hostData = getHostData(bufferedReader);

            SMPClient smpClient = new SMPClient(hostData);

        } catch (Exception ioException) {
            ioException.printStackTrace();
        }

    }

    private static HostData getHostData(BufferedReader bufferedReader) throws IOException {
        System.out.println("Please enter the hostname of the server.");
        String hostname = bufferedReader.readLine();
        if (hostname.isEmpty())
            hostname = HostData.defaultHostData.hostname();

        System.out.println("Please enter the port of the server.");
        String portStr = bufferedReader.readLine();
        int port;
        if (portStr.isEmpty())
            port = HostData.defaultHostData.port();
        else
            port = Integer.parseInt(portStr);

        return new HostData(hostname, port);
    }
}
