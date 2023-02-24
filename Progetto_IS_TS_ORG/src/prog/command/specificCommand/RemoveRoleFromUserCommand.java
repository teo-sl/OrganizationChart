package prog.command.specificCommand;

import prog.command.Command;
import prog.model.employee.Employee;
import prog.model.role.Role;
import prog.model.unit.Unit;

public class RemoveRoleFromUserCommand implements Command {
    private final Role selectedRole;
    private final Unit selectedUnit;
    private final Employee selectedEmployee;

    public RemoveRoleFromUserCommand(Role selectedRole, Unit selectedUnit,Employee selectedEmployee) {
        this.selectedRole = selectedRole;
        this.selectedUnit = selectedUnit;
        this.selectedEmployee=selectedEmployee;
    }
    @Override
    public boolean doIt() {
        if(selectedRole==null || selectedUnit==null || selectedEmployee==null) return false;
        selectedUnit.removeRole(selectedRole,selectedEmployee);
        return true;
    }
}
