package prog.command.specificCommand;

import prog.command.Command;
import prog.model.role.Role;
import prog.model.unit.Unit;

public class RemoveRoleFromUnitCommand implements Command {
    private final Unit selectedUnit;
    private final Role selectedRole;

    public RemoveRoleFromUnitCommand(Unit selectedUnit, Role selectedRole) {
        this.selectedRole=selectedRole;
        this.selectedUnit=selectedUnit;
    }
    @Override
    public boolean doIt() {
        if(selectedUnit==null || selectedRole==null) return false;
        return selectedUnit.removeRoleFromUnit(selectedRole);

    }
}
