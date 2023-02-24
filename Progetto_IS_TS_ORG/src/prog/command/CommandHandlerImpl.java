package prog.command;

public class CommandHandlerImpl implements CommandHandler{

    @Override
    public void handle(Command c) {
        c.doIt();
    }
}
