package prog.model.unit;

import prog.subject.Subject;
import prog.model.company.Company;
import prog.model.employee.Employee;
import prog.model.employee2uni.Employee2Unit;
import prog.model.role.Role;
import prog.observer.Observer;
import prog.updateType.UpdateType;

import java.util.*;

public class Unit implements Subject {
    private static int N=1;
    private String name;
    private final int code;
    private Company company;
    private Unit parentUnit;
    private int parentUnitCode;

    private final Set<Role> admittedRoles=new HashSet<>();
    private final List<Observer> observers=new ArrayList<>();
    private final List<Employee2Unit> employee2Units=new ArrayList<>();


    public boolean removeRoleFromUnit(Role selectedRole) {
        if(!admittedRoles.contains(selectedRole)) return false;
        admittedRoles.remove(selectedRole);
        removeE2U(selectedRole);
        notifyObservers(UpdateType.ROLE);
        return true;
    }
    private void removeE2U(Role role) {
        List<Employee2Unit> tmp=new ArrayList<>();
        for(Employee2Unit e2u : employee2Units) if(e2u.getRole().equals(role)) tmp.add(e2u);
        for(Employee2Unit e2u : tmp) employee2Units.remove(e2u);
    }
    public boolean addRoleToUnit(Role selectedRole) {
        if(admittedRoles.contains(selectedRole)) return false;
        admittedRoles.add(selectedRole);
        notifyObservers(UpdateType.ROLE);
        return true;
    }

    public Set<Role> getAdmittedRoles() {
        return Collections.unmodifiableSet(admittedRoles);
    }

    public Unit(String name, int code, Company company, Unit parentUnit, int parentUnitCode, boolean load) {
        if(load) this.code=code;
        else {this.code=N; N++; }
        this.name=name;
        this.parentUnitCode=parentUnitCode;
        this.company=company;
        this.parentUnit=parentUnit;
    }

    public int getParentUnitCode() {
        return parentUnitCode;
    }



    public void setParentUnitCode(int parentUnitCode) {
        this.parentUnitCode = parentUnitCode;
    }




    public int getCode() {
        return code;
    }



    public static void setN(int N) { Unit.N=N;}
    public String getName() {
        return name;
    }

    public Unit getParentUnit() { return parentUnit; }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }


    public void setParentUnit(Unit parentUnit) {
        this.parentUnit = parentUnit;
        if(parentUnit==null) { parentUnitCode=-1; return; }
        this.parentUnitCode =parentUnit.getCode();
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public void unregisterObserver(Observer o) {
            observers.remove(o);
    }

    @Override
    public void notifyObservers(UpdateType t) {
        for(Observer o : observers)
            o.update(this,t);
    }
    public void setName(String name) {
        this.name=name;
        notifyObservers(UpdateType.UNIT_NAME);
    }
    public void addRole(Role r, Employee e) {
        Employee2Unit tmp=new Employee2Unit(this,e,r);
        addRole(tmp);
    }
    public void addRole(Employee2Unit e2u) {
        if(!admittedRoles.contains(e2u.getRole()) || employee2Units.contains(e2u)) return;
        employee2Units.add(e2u);
        notifyObservers(UpdateType.ROLE);
    }

    public List<Employee2Unit> getEmployee2Units() {
        return Collections.unmodifiableList(employee2Units);
    }

    public void removeRole(Employee2Unit e2u) {
        if(employee2Units.remove(e2u))
            notifyObservers(UpdateType.ROLE);
    }
    public void removeRole(Role r, Employee e) {
        Employee2Unit e2u=new Employee2Unit(this,e,r);
        removeRole(e2u);
    }

    public String toString() {
        StringBuilder sb=new StringBuilder(100);
        if(company==null) System.out.println("ciao");
        else sb.append("Company: "+company.getName()+"\n\n");

        sb.append("Admitted roles: ");
        sb.append(admittedRoles+"\n\n");
        sb.append("Unit name: "+name+"\n\n");
        sb.append("Assigned roles:\n");
        for(Employee2Unit e2u : employee2Units)
            sb.append(e2u.getRole().getName()+": "+e2u.getEmployee().toString()+"\n");
        return sb.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return code==unit.code;
    }

    public int hashCode() {
        return (code+"").hashCode();
    }


}
