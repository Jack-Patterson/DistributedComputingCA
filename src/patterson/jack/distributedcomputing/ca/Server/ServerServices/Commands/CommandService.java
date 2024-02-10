package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.Server.ServerServices.SecurityService;

import java.util.ArrayList;
import java.util.Optional;

public class CommandService {
    private final ArrayList<Command> commands;

    public CommandService(SecurityService securityService) {
        this.commands = new ArrayList<>();
        initializeCommands();
    }

    private void initializeCommands(){
        commands.add(new LoginCommand("login", 2));
        commands.add(new LoginCommand("logout", 0));
        commands.add(new LoginCommand("uploadmessage", 1));
        commands.add(new LoginCommand("getmessages", 1));
    }

    public Command parseCommand(String message) throws ClassNotFoundException {
        String[] commandAsArray = message.trim().split(" ");
        String potentialPrefix = commandAsArray[0];
        Command usedCommand;

        Optional<Command> usedCommandOptional = commands.stream()
                .filter(command -> potentialPrefix.equalsIgnoreCase(command.getPrefix()))
                .findFirst();
        if (usedCommandOptional.isPresent())
            usedCommand = usedCommandOptional.get();
        else {
            throw new ClassNotFoundException("Command could not be found in the string " + message);
        }

        return usedCommand;
    }
}
