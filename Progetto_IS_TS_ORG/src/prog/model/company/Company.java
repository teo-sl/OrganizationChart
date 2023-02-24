package prog.model.company;

import prog.model.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {

    private String name;
    private final List<Unit> units;

    public Company(String name) {
        this.name = name;
        units=new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }
    public void clearUnits() { units.clear();}



    public boolean equals(Object o) {
        if(o==null) return false;
        if(o==this) return true;
        if(!(o instanceof Company)) return false;
        Company c = (Company) o;
        return name.equals(c.name);
    }
    public int hashCode() {
        return name.hashCode();
    }


    public void setName(String name) {
        this.name = name;
    }
    public boolean removeUnit(Unit u) {
        return units.remove(u);
    }


    public void addUnit(Unit u) {
        units.add(u);
        u.setCompany(this);
    }
}
