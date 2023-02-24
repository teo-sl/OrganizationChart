package prog.command.specificCommand;

import prog.command.Command;
import prog.model.role.Role;
import prog.model.unit.Unit;

public class AddRoleToUnitCommand implements Command {
    private final Unit selectedUnit;
    private final Role selectedRole;

    public AddRoleToUnitCommand(Unit selectedUnit, Role selectedRole) {
        this.selectedUnit = selectedUnit;
        this.selectedRole = selectedRole;
    }

    @Override
    public boolean doIt() {
        if(selectedRole==null || selectedUnit==null) return false;
        return selectedUnit.addRoleToUnit(selectedRole);
    }
}
