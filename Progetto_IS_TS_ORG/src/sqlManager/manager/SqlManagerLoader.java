package sqlManager.manager;


import abstractSqlManager.AbstractSqlManager;
import sqlManager.dbmodel.dbcompany.DbCompany;
import sqlManager.dbmodel.dbemployee.DbEmployee;
import sqlManager.dbmodel.dbemployee2ur.DbEmployee2UR;
import sqlManager.dbmodel.dbrole.DbRole;
import sqlManager.dbmodel.dbu2r.DbU2r;
import sqlManager.dbmodel.dbunit.DbUnit;

import java.sql.*;
import java.util.*;

public class SqlManagerLoader extends AbstractSqlManager {

    public SqlManagerLoader() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public List<DbRole> getDbRoles() {
        List<DbRole> ret = new ArrayList<>();
        try {
            String sql = "SELECT * FROM roles";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                ret.add(new DbRole(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List<DbEmployee> getDbEmployees() {
        List<DbEmployee> ret = new ArrayList<>();
        try {
            String sql = "SELECT * FROM employees";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                int sn = resultSet.getInt("sn");
                ret.add(new DbEmployee(id, name, surname, email, sn));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public DbCompany getDbCompany(String name,List<DbRole> roles,List<DbEmployee> employees) {
        DbCompany ret = null;
        try {
            String sql = "SELECT * FROM companies WHERE name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return null;
            int id = resultSet.getInt("id");
            ret = new DbCompany(id, name);
            setDbUnitsToCompany(ret);
            setU2RtoUnits(ret.getUnits(),roles);
            setE2URToUnits(ret.getUnits(),employees);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List<DbCompany> getDbCompany() {
        List<DbCompany> dbCompanies = new ArrayList<>();
        try {
            String sql = "SELECT * FROM companies";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                dbCompanies.add(new DbCompany(id, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbCompanies;
    }

    private void setDbUnitsToCompany(DbCompany c) {
        HashMap<Integer, DbUnit> map = new HashMap<>();
        try {
            String sql = "SELECT * FROM units WHERE companyId=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, c.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int parentUnit = resultSet.getInt("parentUnit");
                int code = resultSet.getInt("code");
                DbUnit tmp = new DbUnit(name, id, code, null, parentUnit, 0, c);
                map.put(id, tmp);
                c.addUnit(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (DbUnit u : c.getUnits()) {
            if (u.getParentUnitId() == 0 || u.getParentUnitId() == -1) continue;
            u.setParentUnit(map.get(u.getParentUnitId()));
        }
    }
    private void setU2RtoUnits(List<DbUnit> units,List<DbRole> roles) {
        try {
            String sql="SELECT * FROM u2r";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                int id=resultSet.getInt("id");
                int unitId=resultSet.getInt("unitId");
                int roleId=resultSet.getInt("roleId");
                DbUnit u=null;
                DbRole r=null;

                for(DbUnit unit : units)
                    if(unit.getId()==unitId) {u=unit; break; }
                if(u==null) continue;
                for(DbRole role : roles)
                    if(role.getId()==roleId) { r=role; break; }
                if(r==null) continue;

                u.addAdmittedRole(new DbU2r(id,u,r));
            }
        }catch (Exception e) {e.printStackTrace(); }
    }
    private void setE2URToUnits(List<DbUnit> units,List<DbEmployee> employees) {
        try {
            //int companyId=units.get(0).getCompany().getId();
            String sql = "SELECT * FROM e2ur";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id=resultSet.getInt("id");
                int empId = resultSet.getInt("empId");
                int u2rId = resultSet.getInt("u2rId");
                DbUnit u=null;
                DbU2r u2r = null;
                DbEmployee e = null;
                for(DbUnit unit : units)
                    for(DbU2r dbU2r : unit.getAdmittedRoles())
                        if(u2rId==dbU2r.getId()) {
                            u=unit; u2r=dbU2r; break;
                        }

                if(u==null || u2r==null) continue;
                for(DbEmployee dbe : employees)
                    if(dbe.getId()==empId) { e=dbe; }
                if(e==null) continue;

                u.addEmployee2UR(new DbEmployee2UR(id,e,u2r));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

