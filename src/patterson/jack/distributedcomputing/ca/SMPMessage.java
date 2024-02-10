package patterson.jack.distributedcomputing.ca;

public record SMPMessage(int statusCode, String command, String message) {

    public static int StatusOk = 200;
    public static int StatusBadRequest = 400;
    public static int StatusForbidden = 403;
    public static String CommandServerResponse = "server_response";
    public static SMPMessage StatusForbiddenMessage = new SMPMessage(SMPMessage.StatusForbidden, SMPMessage.CommandServerResponse, "Invalid credentials.");

    @Override
    public String toString() {
        return "Status: " + statusCode + ", Command: " + command + ", Message: " + message;
    }
}
