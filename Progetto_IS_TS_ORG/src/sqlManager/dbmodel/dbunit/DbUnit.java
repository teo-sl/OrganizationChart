package sqlManager.dbmodel.dbunit;


import sqlManager.dbmodel.dbcompany.DbCompany;
import sqlManager.dbmodel.dbemployee2ur.DbEmployee2UR;
import sqlManager.dbmodel.dbu2r.DbU2r;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbUnit {
    private final String name;
    private int id;
    private final int code;
    private DbUnit parentUnit;
    private int parentUnitId;
    private int parentUnitCode;
    private DbCompany company;
    private final List<DbEmployee2UR> employee2Units=new ArrayList<>();
    private final List<DbU2r> admittedRoles=new ArrayList<>();


    public DbUnit(String name, int id, int code, DbUnit parentUnit, int parentUnitId, int parentUnitCode, DbCompany company) {
        this.name = name;
        this.id = id;
        this.code = code;
        this.parentUnit = parentUnit;
        this.parentUnitId = parentUnitId;
        this.parentUnitCode = parentUnitCode;
        this.company = company;
    }

    public List<DbU2r> getAdmittedRoles() {
        return Collections.unmodifiableList(admittedRoles);
    }
    public void addAdmittedRole(DbU2r u2r) { admittedRoles.add(u2r);}

    public void setId(int id) {
        this.id = id;
    }

    public List<DbEmployee2UR> getEmployee2Units() {
        return Collections.unmodifiableList(employee2Units);
    }
    public void addEmployee2UR(DbEmployee2UR dbEmployee2UR) { employee2Units.add(dbEmployee2UR); }

    public int getParentUnitId() {
        return parentUnitId;
    }


    public int getCode() {
        return code;
    }

    public void setParentUnitCode(int parentUnitCode) {
        this.parentUnitCode = parentUnitCode;
    }

    public int getParentUnitCode() {
        return parentUnitCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DbUnit getParentUnit() {
        return parentUnit;
    }

    public DbCompany getCompany() {
        return company;
    }

    public void setParentUnit(DbUnit parentUnit) {
        this.parentUnit = parentUnit;
        this.parentUnitId=parentUnit.getId();
        this.parentUnitCode=parentUnit.getCode();
    }

    public void setCompany(DbCompany company) {
        this.company = company;
    }
}
