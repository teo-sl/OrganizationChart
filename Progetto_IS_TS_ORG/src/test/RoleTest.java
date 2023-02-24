package test;

import org.junit.Before;
import org.junit.Test;
import prog.model.role.Role;

import static org.junit.Assert.*;

public class RoleTest {
    Role role;
    private String name="testName";
    @Before
    public void before() {
        role=new Role(name);
    }

    @Test
    public void getName() {
        assertEquals(name,role.getName());
    }
}