package patterson.jack.distributedcomputing.ca;

public record SMPMessage(int statusCode, String message) {

    @Override
    public String toString() {
        return "Status: " + statusCode + ", Message: " + message;
    }
}
