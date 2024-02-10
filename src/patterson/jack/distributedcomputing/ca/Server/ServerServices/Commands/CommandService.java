package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.SMPMessage;
import patterson.jack.distributedcomputing.ca.Server.ServerServices.SecurityService;

import java.util.ArrayList;
import java.util.Optional;

public class CommandService {
    private final ArrayList<Command> commands;

    public CommandService() {
        this.commands = new ArrayList<>();
        initializeCommands();
    }

    private void initializeCommands(){
        commands.add(new LoginCommand(Command.loginPrefix, 2));
        commands.add(new LogoutCommand(Command.logoutPrefix, 0));
        commands.add(new UploadMessageCommand(Command.uploadMessagePrefix, 1));
        commands.add(new GetMessageCommand(Command.getMessagesPrefix, 1));
    }

    public Command parseCommand(SMPMessage message) throws ClassNotFoundException {
        Command usedCommand;

        Optional<Command> usedCommandOptional = commands.stream()
                .filter(command -> message.command().equalsIgnoreCase(command.getPrefix()))
                .findFirst();
        if (usedCommandOptional.isPresent()) {
            usedCommand = usedCommandOptional.get();
        }
        else {
            throw new ClassNotFoundException("Command could not be found in the string " + message);
        }

        int argumentsLength = message.message().trim().length() == 0 ? 0 : message.message().trim().split(" ").length;
        if (argumentsLength < usedCommand.getArgumentsCount()){
            throw new IllegalArgumentException("Command requires " + usedCommand.getArgumentsCount() +
                    " argument(s) provided. Please provide " + (usedCommand.getArgumentsCount() - argumentsLength) +
                    " more argument(s).");
        }

        return usedCommand;
    }
}
