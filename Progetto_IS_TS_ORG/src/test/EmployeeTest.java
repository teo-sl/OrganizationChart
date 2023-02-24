package test;

import org.junit.Before;
import org.junit.Test;
import prog.model.employee.Employee;

import static org.junit.Assert.*;

public class EmployeeTest {
    private Employee employee;
    private int sn=1;
    private String name="Mario",surname="Rossi",email="prova@prova.it";

    @Before
    public void before() {
        employee=new Employee(sn,name,surname,email);
    }

    @Test
    public void getSurname() {
        assertEquals(name,employee.getName());
    }

    @Test
    public void getEmail() {
        assertEquals(email,employee.getEmail());
    }

    @Test
    public void getSn() {
        assertEquals(sn,employee.getSn());
    }

    @Test
    public void getName() {
        assertEquals(name,employee.getName());
    }
}