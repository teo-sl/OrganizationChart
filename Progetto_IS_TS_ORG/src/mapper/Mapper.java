package mapper;


import prog.model.company.Company;
import prog.model.employee.Employee;
import prog.model.employee2uni.Employee2Unit;
import prog.model.role.Role;
import prog.model.unit.Unit;
import sqlManager.dbmodel.dbu2r.DbU2r;
import sqlManager.manager.SqlManagerSaver;
import sqlManager.dbmodel.dbcompany.DbCompany;
import sqlManager.dbmodel.dbemployee.DbEmployee;
import sqlManager.dbmodel.dbemployee2ur.DbEmployee2UR;
import sqlManager.dbmodel.dbrole.DbRole;
import sqlManager.dbmodel.dbunit.DbUnit;
import sqlManager.manager.SqlManagerLoader;
import sqlManager.manager.SqlManagerUtility;

import java.util.*;

public class Mapper {
    private final SqlManagerSaver sqlManagerSaver =new SqlManagerSaver();
    private final SqlManagerLoader sqlManagerLoader = new SqlManagerLoader();
    private final SqlManagerUtility sqlManagerUtility = new SqlManagerUtility();

    public boolean loadFromDb(String companyName,Company c, Set<Role> roleSet, Set<Employee> employeeSet) {
        List<DbRole> roles=sqlManagerLoader.getDbRoles();
        List<DbEmployee> employees=sqlManagerLoader.getDbEmployees();
        DbCompany dbc=sqlManagerLoader.getDbCompany(companyName,roles,employees);
        if(dbc==null || dbc.getUnitsSize()==0) return false;
        c.clearUnits();
        convertRoles(roleSet,roles); convertEmployees(employeeSet,employees);
        convertCompany(c,dbc,roleSet,employeeSet);
        return true;
    }

    public boolean saveToDb(Company c, Set<Role> roles, Set<Employee> employees) {
        List<DbRole> dbRoles=convertRoles2Db(roles);
        List<DbEmployee> dbEmployees=convertEmployees2Db(employees);
        sqlManagerSaver.updateRandE(dbRoles,dbEmployees);
        DbCompany dbCompany=convertCompany2Db(c,dbEmployees,dbRoles);
        return sqlManagerSaver.save2Db(dbCompany);
    }


    private void convertEmployees(Set<Employee> employees, List<DbEmployee> dbEmployees) {
        if(employees==null) employees=new HashSet<>();
        employees.clear();

        for(DbEmployee dbEmployee : dbEmployees)
            employees.add(new Employee(dbEmployee.getSn(),dbEmployee.getName(),dbEmployee.getSurname(),dbEmployee.getEmail()));

    }

    private void convertRoles(Set<Role> roles,List<DbRole> dbRoles) {
        if(roles==null) roles=new HashSet<>();
        roles.clear();

        for(DbRole dbRole : dbRoles)
            roles.add(new Role(dbRole.getName()));
    }
    private void convertCompany(Company c,DbCompany dbc,Set<Role> roles,Set<Employee> employees) {
        if(c==null) c=new Company(dbc.getName());
        c.setName(dbc.getName());
        c.clearUnits();
        Map<Integer,Unit> map=new HashMap<>();
        Map<String,Role> roleMap=new HashMap<>();
        Map<Integer,Employee> employeeMap=new HashMap<>();

        for(Role r : roles) roleMap.put(r.getName(),r);
        for(Employee e : employees) employeeMap.put(e.getSn(),e);

        for(DbUnit dbUnit : dbc.getUnits()) {
            Unit unit = new Unit(dbUnit.getName(),dbUnit.getCode(),c,null,-1,true);
            map.put(unit.getCode(),unit);
            if(dbUnit.getParentUnit()!=null)
                unit.setParentUnitCode(dbUnit.getParentUnit().getCode());
            c.addUnit(unit);
            for(DbU2r u2r : dbUnit.getAdmittedRoles())
                if(roleMap.containsKey(u2r.getRole().getName()))
                    unit.addRoleToUnit((roleMap.get(u2r.getRole().getName())));
            for(DbEmployee2UR e2ur : dbUnit.getEmployee2Units()) {
                Role role=roleMap.get(e2ur.getU2r().getRole().getName());
                Employee emp=employeeMap.get(e2ur.getEmployee().getSn());
                unit.addRole(role,emp);
            }

        }
        for(Unit u : c.getUnits())
            if(map.containsKey(u.getParentUnitCode()))
                u.setParentUnit(map.get(u.getParentUnitCode()));
    }


    private DbCompany convertCompany2Db(Company c,List<DbEmployee> employees,List<DbRole> roles) {
        DbCompany ret = new DbCompany(c.getName());
        Map<Integer,DbUnit> map=new HashMap<>();
        Map<String,DbRole> roleMap=new HashMap<>();
        Map<Integer,DbEmployee> employeeMap=new HashMap<>();
        for(DbRole dbRole : roles) roleMap.put(dbRole.getName(),dbRole);
        for(DbEmployee dbEmployee : employees) employeeMap.put(dbEmployee.getSn(),dbEmployee);
        for(Unit u : c.getUnits()) {
            DbUnit tmp=new DbUnit(u.getName(),0,u.getCode(),null,0,-1,ret);
            map.put(tmp.getCode(),tmp);
            if(u.getParentUnit()!=null)
                tmp.setParentUnitCode(u.getParentUnit().getCode());
            ret.addUnit(tmp);
            for(Role role : u.getAdmittedRoles())
                tmp.addAdmittedRole(new DbU2r(0,tmp,roleMap.get(role.getName())));
            for(Employee2Unit e2u : u.getEmployee2Units()) {
                DbU2r u2r=null;
                for(DbU2r dbU2r : tmp.getAdmittedRoles()) if(dbU2r.getRole().getName().equals(e2u.getRole().getName())) { u2r=dbU2r; break; }
                DbEmployee employee=employeeMap.get(e2u.getEmployee().getSn());
                tmp.addEmployee2UR(new DbEmployee2UR(0,employee,u2r));
            }

        }
        for(DbUnit u : ret.getUnits())
            if(map.containsKey(u.getParentUnitCode()))
                u.setParentUnit(map.get(u.getParentUnitCode()));
        return ret;
    }

    private List<DbRole> convertRoles2Db(Set<Role> roles) {
        List<DbRole> ret=new ArrayList<>();
        for(Role r : roles) {
            ret.add(new DbRole(r.getName()));
        }
        return ret;
    }
    private List<DbEmployee> convertEmployees2Db(Set<Employee> employees) {
        List<DbEmployee> ret=new ArrayList<>();
        for(Employee e : employees)
            ret.add(new DbEmployee(0,e.getName(), e.getSurname(),e.getEmail(),e.getSn()));
        return ret;
    }

    public List<String> getCompanyString() {
        List<String> ret= new ArrayList<>();
        for(DbCompany dbc : sqlManagerLoader.getDbCompany())
            ret.add(dbc.getName());
        return ret;
    }


    public int getMaxUnitCode() {
        int ret= sqlManagerUtility.getMaxUnitCode();
        if(ret==-1) return 0;
        return ret;
    }

    public void loadEmpAndRoleFromDb(Set<Role> roles,Set<Employee> employees) {
        List<DbRole> dbRoles= sqlManagerLoader.getDbRoles();
        List<DbEmployee> dbEmployees= sqlManagerLoader.getDbEmployees();
        convertRoles(roles,dbRoles);
        convertEmployees(employees,dbEmployees);
    }

    public void saveEmpAndRole2Db(Set<Role> roles,Set<Employee> employees) {
        List<DbRole> dbRoles=convertRoles2Db(roles);
        List<DbEmployee> dbEmployees=convertEmployees2Db(employees);
        sqlManagerSaver.updateRandE(dbRoles,dbEmployees);
    }

}
