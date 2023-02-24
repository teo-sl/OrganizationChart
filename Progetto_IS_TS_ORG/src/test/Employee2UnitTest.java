package test;

import org.junit.Before;
import org.junit.Test;
import prog.model.employee.Employee;
import prog.model.employee2uni.Employee2Unit;
import prog.model.role.Role;
import prog.model.unit.Unit;

import static org.junit.Assert.*;

public class Employee2UnitTest {
    private Employee employee;
    private Role role;
    private Unit unit;
    private Employee2Unit employee2Unit;

    @Before
    public void before() {
        employee=new Employee(1,"prova","prova","prova@prova.it");
        role=new Role("role");
        unit=new Unit("unitName",-1,null,null,-1,false);
        employee2Unit=new Employee2Unit(unit,employee,role);
    }
    @Test
    public void getUnit() {
        assertEquals(unit,employee2Unit.getUnit());
    }

    @Test
    public void getEmployee() {
        assertEquals(employee,employee2Unit.getEmployee());
    }

    @Test
    public void getRole() {
        assertEquals(role,employee2Unit.getRole());
    }
}