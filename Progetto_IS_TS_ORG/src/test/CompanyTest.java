package test;

import org.junit.Before;
import org.junit.Test;
import prog.model.company.Company;
import prog.model.unit.Unit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompanyTest {
    Company c;
    Unit unit1;
    Unit unit2;
    @Before
    public void before() {
        c=new Company("testCompany");
        unit1=new Unit("unit1",-1,c,null,-1,false);
        unit2=new Unit("unit2",-1,c,null,-1,false);
        c.addUnit(unit1);
    }

    @Test
    public void getName() {
        assertEquals("testCompany",c.getName());
    }

    @Test
    public void getUnits() {
        List<Unit> expected=new ArrayList<>();
        expected.add(unit1);
        assertEquals(expected,c.getUnits());
    }

    @Test
    public void clearUnits() {
        c.clearUnits();
        assertTrue(c.getUnits().isEmpty());
    }


    @Test
    public void setName() {
        c.setName("newName");
        assertEquals("newName",c.getName());
    }

    @Test
    public void removeUnit() {
        c.removeUnit(unit1);
        assertFalse(c.getUnits().contains(unit1));
    }

    @Test
    public void addUnit() {
        c.addUnit(unit2);
        assertTrue(c.getUnits().contains(unit2));
    }
}