package pattersonjack.distributedcomputingca.Shared;

public record HostData (String hostname, int port) {
    public static HostData defaultHostData = new HostData("localhost", 7);
}
