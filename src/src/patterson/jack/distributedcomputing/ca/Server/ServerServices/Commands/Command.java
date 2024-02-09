package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

public abstract class Command {
    private final String[] prefixes;
    private final int argumentsCount;

    public Command (String[] prefixes, int argumentsCount){
        this.prefixes = prefixes;
        this.argumentsCount = argumentsCount;
    }
}
