package prog.command.specificCommand;

import prog.command.Command;
import prog.model.employee.Employee;
import prog.model.role.Role;
import prog.model.unit.Unit;

public class AddRoleToUserCommand implements Command {
    private final Role selectedRole;
    private final Unit selectedUnit;
    private final Employee selectedEmployee;

    public AddRoleToUserCommand(Role selectedRole, Unit selectedUnit,Employee selectedEmployee) {
        this.selectedRole = selectedRole;
        this.selectedUnit = selectedUnit;
        this.selectedEmployee=selectedEmployee;
    }

    @Override
    public boolean doIt() {
        if(selectedRole==null || selectedUnit==null || selectedEmployee==null) return false;
        selectedUnit.addRole(selectedRole,selectedEmployee);
        return true;
    }
}
