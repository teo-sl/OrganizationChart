package sqlManager.dbmodel.dbu2r;

import sqlManager.dbmodel.dbrole.DbRole;
import sqlManager.dbmodel.dbunit.DbUnit;

public class DbU2r {
    private int id;
    private final DbUnit unit;
    private final DbRole role;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public DbUnit getUnit() {
        return unit;
    }

    public DbRole getRole() {
        return role;
    }

    public DbU2r(int id, DbUnit unit, DbRole role) {
        this.id=id;
        this.unit = unit;
        this.role = role;
    }
}
