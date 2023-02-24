package prog.communicator;

import prog.model.employee.Employee;
import prog.model.role.Role;
import prog.model.unit.Unit;
import prog.view.TreeView;

public class Communicator {
    private final TreeView t;

    public Communicator(TreeView t) {
        this.t = t;
    }

    public Unit getSelectedUnit() {
        return t.getSelectedUnit();
    }
    public Role getSelectedRole() {
        return t.getSelectedRole();
    }
    public Employee getSelectedEmployee() {
        return t.getSelectedEmployee();
    }

    public void addEmployee(Employee employee) {
        t.addEmployee(employee);
    }
}
