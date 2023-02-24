package sqlManager.dbmodel.dbemployee2ur;


import sqlManager.dbmodel.dbemployee.DbEmployee;
import sqlManager.dbmodel.dbu2r.DbU2r;

public class DbEmployee2UR {
    private int id;
    private DbEmployee employee;
    private final DbU2r u2r;

    public DbEmployee2UR(int id, DbEmployee employee, DbU2r u2r) {
        this.id = id;
        this.employee = employee;
        this.u2r = u2r;
    }

    public int getId() {
        return id;
    }

    public DbEmployee getEmployee() {
        return employee;
    }

    public DbU2r getU2r() {
        return u2r;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmployee(DbEmployee employee) {
        this.employee = employee;
    }

}
