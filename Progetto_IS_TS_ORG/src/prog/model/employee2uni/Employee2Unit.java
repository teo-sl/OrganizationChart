package prog.model.employee2uni;

import prog.model.employee.Employee;
import prog.model.role.Role;
import prog.model.unit.Unit;

public class Employee2Unit {
    private final Unit unit;
    private final Employee employee;
    private final Role role;

    public Employee2Unit(Unit unit, Employee employee, Role role) {
        this.unit = unit;
        this.employee = employee;
        this.role = role;
    }

    public Unit getUnit() {
        return unit;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Role getRole() {
        return role;
    }

    public boolean equals(Object o) {
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof Employee2Unit)) return false;
        Employee2Unit e2u=(Employee2Unit) o;
        return e2u.role.equals(role) && e2u.unit.equals(unit) && e2u.employee.equals(employee);
    }
    public int hashCode() {
        return (role.getName()+employee.getSn()+unit.getCode()).hashCode();
    }

}
