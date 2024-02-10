package patterson.jack.distributedcomputing.ca.Server.ServerServices.Commands;

import patterson.jack.distributedcomputing.ca.Server.SMPServer;
import patterson.jack.distributedcomputing.ca.Server.SMPServerThread;

class LoginCommand extends Command {

    public LoginCommand(String prefix, int argumentsCount) {
        super(prefix, argumentsCount);
    }

    @Override
    public void execute(SMPServer smpServer, SMPServerThread smpServerThread) {

    }
}
