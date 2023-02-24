package prog.command.specificCommand;

import prog.command.Command;
import prog.model.company.Company;

public class SetCompanyName implements Command {
    private final Company c;
    private final String newName;

    public SetCompanyName(Company c, String newName) {
        this.c = c;
        this.newName = newName;
    }

    @Override
    public boolean doIt() {
        if (c == null || newName == null || newName.equals("")) return false;
        c.setName(newName);
        return true;
    }
}

