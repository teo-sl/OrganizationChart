package sqlManager.dbmodel.dbcompany;

import sqlManager.dbmodel.dbunit.DbUnit;

import java.util.ArrayList;
import java.util.List;

public class DbCompany {
    private int id;
    private String name;
    private final List<DbUnit> units=new ArrayList<>();

    public DbCompany(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getUnitsSize() { return units.size(); }

    public DbCompany(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<DbUnit> getUnits() {
        return new ArrayList<>(units);
    }

    public void addUnit(DbUnit unit) { units.add(unit); }
}
