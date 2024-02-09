package patterson.jack.distributedcomputing.ca;

public class SMPMessage {

    private final int statusCode;
    private final String message;

    public SMPMessage(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Status: " + statusCode + ", Message: " + message;
    }

    public static SMPMessage parseMessageString(String inputMessage){
        try {
            String inputMessageWithRemovedPrefix = inputMessage.substring("Status: ".length());
            int separatorIndex = inputMessageWithRemovedPrefix.indexOf(", Message: ");

            if (separatorIndex == -1) {
                throw new IllegalArgumentException("Invalid input format.");
            }

            int statusCode = Integer.parseInt(inputMessageWithRemovedPrefix.substring(0, separatorIndex));
            String message = inputMessageWithRemovedPrefix.substring(separatorIndex + ", Message: ".length());

            return new SMPMessage(statusCode, message);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse input string: " + inputMessage, e);
        }
    }
}
