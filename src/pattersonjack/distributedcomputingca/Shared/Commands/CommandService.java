package pattersonjack.distributedcomputingca.Shared.Commands;

import pattersonjack.distributedcomputingca.Shared.SMPMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        if (argumentsLength < usedCommand.getArgumentsCount() && !(usedCommand instanceof GetMessageCommand)){
            throw new IllegalArgumentException("Command requires " + usedCommand.getArgumentsCount() +
                    " argument(s) provided. Please provide " + (usedCommand.getArgumentsCount() - argumentsLength) +
                    " more argument(s).");
        }

        return usedCommand;
    }

    public SMPMessage parseText(String text) throws ClassNotFoundException {
        List<String> textSplit = new ArrayList<>(Arrays.stream(text.trim().split(" ")).toList());
        String commandStr = textSplit.get(0);
        textSplit.removeFirst();
        String textWithoutCommand = String.join(" ", textSplit);

        Command usedCommand;

        Optional<Command> usedCommandOptional = commands.stream()
                .filter(command -> commandStr.equalsIgnoreCase(command.getPrefix()))
                .findFirst();
        if (usedCommandOptional.isPresent()) {
            usedCommand = usedCommandOptional.get();
        }
        else {
            throw new ClassNotFoundException("Command could not be found in the string " + text);
        }

        int argumentsLength = textWithoutCommand.trim().length() == 0 ? 0 : textWithoutCommand.trim().split(" ").length;
        if (argumentsLength < usedCommand.getArgumentsCount() && !(usedCommand instanceof GetMessageCommand)){
            throw new IllegalArgumentException("Command requires " + usedCommand.getArgumentsCount() +
                    " argument(s). Please provide " + (usedCommand.getArgumentsCount() - argumentsLength) +
                    " more argument(s).");
        }

        return new SMPMessage(200, usedCommand.getPrefix(), textWithoutCommand);
    }
}
