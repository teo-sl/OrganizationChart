package test;

import org.junit.Before;
import org.junit.Test;
import prog.model.unit.Unit;
import prog.subject.Subject;
import prog.model.company.Company;
import prog.model.employee.Employee;
import prog.model.employee2uni.Employee2Unit;
import prog.model.role.Role;
import prog.observer.Observer;
import prog.updateType.UpdateType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class UnitTest {
    private Unit u;
    private Unit child;
    private Unit testUnit;
    private Role r;
    private Employee e;
    private ObserverStub observerStub;


    @Before
    public void before() {
        Unit father=new Unit("fatherUnit",-1,null,null,-1,false);
        child=new Unit("childUnit",-1,null,father,father.getCode(),false);
        testUnit=new Unit("tmp",80,null,null,79,true);
        r = new Role("Direttore");
        father.addRoleToUnit(r);
        e =new Employee(1,"Mario","Rossi","email@prova.it");
        u=father;
        observerStub=new ObserverStub();
        u.registerObserver(observerStub);
    }

    private static class ObserverStub implements Observer {
        private boolean flag=false;
        public void update(Subject s, UpdateType t) { flag=true; }
        public boolean getFlag() { return flag; }
    }

    @Test
    public void removeRoleFromUnit() {
        u.removeRoleFromUnit(r);
        assertFalse(u.getAdmittedRoles().contains(r));
    }

    @Test
    public void getAmdmittedRole() {
        Role newRole = new Role("tmp");
        List<Role> roles=new ArrayList<>();
        roles.add(r); roles.add(newRole);
        u.addRoleToUnit(newRole);
        assertTrue(u.getAdmittedRoles().containsAll(roles));
    }

    @Test
    public void addRoleToUnit() {
        Role newRole=new Role("CEO");
        u.addRoleToUnit(newRole);
        assertTrue(u.getAdmittedRoles().contains(newRole));
    }

    @Test
    public void setParentUnitCode() {
        int parentUnitCode=5;
        u.setParentUnitCode(parentUnitCode);
        assertEquals(parentUnitCode,u.getParentUnitCode());
    }
    @Test
    public void getCode() {
        assertEquals(80,testUnit.getCode());
    }

    @Test
    public void setN() {
        int newN=8;
        Unit.setN(newN);
        Unit tmp=new Unit("tmp",-1,null,null,-1,false);
        assertEquals(newN,tmp.getCode());
    }

    @Test
    public void getName() {
        assertEquals("fatherUnit",u.getName());
    }

    @Test
    public void getParentUnit() {
        assertEquals(u,child.getParentUnit());
    }

    @Test
    public void setParentUnit() {
        testUnit.setParentUnit(u);
        assertEquals(u,testUnit.getParentUnit());
    }

    @Test
    public void getParentUnitCode() {
        assertEquals(79,testUnit.getParentUnitCode());
    }

    @Test
    public void setName() {
        String newName="newName";
        u.setName(newName);
        assertEquals(newName,u.getName());
    }

    @Test
    public void addRole() {
        u.addRole(r,e);
        assertTrue(u.getEmployee2Units().contains(new Employee2Unit(u,e,r)));
    }

    @Test
    public void addNotAdmittedRole() {
        Role notAdmitted=new Role("tmp");
        u.addRole(notAdmitted,e);
        assertFalse(u.getEmployee2Units().contains(new Employee2Unit(u,e,notAdmitted)));
    }

    @Test
    public void removeRole() {
        u.removeRole(r,e);
        assertFalse(u.getEmployee2Units().contains(new Employee2Unit(u,e,r)));
    }

    @Test
    public void setCompany() {
        Company c = new Company("testCompany");
        u.setCompany(c);
        String toString=u.toString();
        String[] parts=toString.split("(\n|\s)");
        assertEquals(c.getName(),parts[1]);
    }
    @Test
    public void notifyObserver() {
        u.notifyObservers(null);
        assertTrue(observerStub.getFlag());
    }
}