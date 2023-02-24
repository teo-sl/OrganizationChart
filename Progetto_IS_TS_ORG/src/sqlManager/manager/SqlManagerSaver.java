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

public class SqlManagerSaver extends AbstractSqlManager {

    public SqlManagerSaver() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public boolean save2Db(DbCompany dbCompany) {
        boolean ret = false;
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT IGNORE INTO companies (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dbCompany.getName());
            int rowsAffected = statement.executeUpdate();
            int k = getId("companies", dbCompany.getName());
            dbCompany.setId(k);
            clear(k);
            saveCompany(dbCompany);
            saveU2R(dbCompany.getUnits());
            saveE2UR(dbCompany.getUnits());
            ret = (rowsAffected > 0);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            }catch (Exception ee) {ee.printStackTrace();}

        }
        return ret;
    }
    private void saveE2UR(List<DbUnit> units) {
        try {
            StringBuilder sb = new StringBuilder("INSERT INTO e2ur (empId,u2rId) VALUES ");
            int l=0;
            for(DbUnit unit : units)
                for(DbEmployee2UR e2ur : unit.getEmployee2Units()) {
                    if(l!=0) sb.append(" , ");
                    l++;
                    sb.append(" ( "+e2ur.getEmployee().getId()+" , "+e2ur.getU2r().getId()+" ) ");
                }
            if(l==0) return;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sb.toString());
        }catch (Exception e) {e.printStackTrace();}
    }

    private void saveU2R(List<DbUnit> units) {
        try {
            StringBuilder sb=new StringBuilder("INSERT INTO u2r (unitId,roleId) VALUES ");
            int l=0;
            for(DbUnit unit : units) {
                for(DbU2r u2r : unit.getAdmittedRoles()) {
                    if (l != 0) sb.append(" , ");
                    l++;
                    sb.append(" ( "+unit.getId()+" , "+u2r.getRole().getId()+" ) ");
                }
            }
            if(l==0) return;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sb.toString(),Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            for(DbUnit u : units)
                for(DbU2r u2r : u.getAdmittedRoles()) {
                        resultSet.next();
                        u2r.setId(resultSet.getInt(1));
                }
        }catch (Exception e) {e.printStackTrace(); }
    }

    private void saveCompany(DbCompany dbCompany) {
        try {
            Statement statement;
            StringBuilder sb = new StringBuilder("INSERT INTO units (name,companyId,code) VALUES ");
            int l=0;
            for(DbUnit u : dbCompany.getUnits()) {
                if(l!=0) sb.append(" , ");
                l++;
                sb.append(" ( \""+u.getName()+"\" , "+dbCompany.getId()+" , "+u.getCode()+" ) ");
            }
            if(l==0) return;

            String sql=sb.toString();
            statement=connection.createStatement();
            statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);

            ResultSet resultSet=statement.getGeneratedKeys();
            int i=0;
            while (resultSet.next()) {
                int generatedId=resultSet.getInt(1);
                dbCompany.getUnits().get(i).setId(generatedId); i++;
            }

            if(dbCompany.getUnitsSize()==1) return;

            sb=new StringBuilder("UPDATE units SET parentUnit = ( CASE id ");
            StringBuilder sb2=new StringBuilder(" WHERE id IN ( ");
            l=0;
            for(DbUnit u : dbCompany.getUnits()) {
                if(u.getParentUnit()==null) continue;
                sb.append(" WHEN "+u.getId()+" THEN "+u.getParentUnit().getId()+" ");
                if(l!=0) sb2.append(" , ");
                l++;
                sb2.append(" "+u.getId()+" ");
            }
            sb.append(" END ) ");sb2.append(" ) ");
            sb.append(" "+sb2+" ");
            sql=sb.toString();
            statement=connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void updateRandE(List<DbRole> roles, List<DbEmployee> employees) {
        try {
            connection.setAutoCommit(false);
            saveEmployees(employees);
            saveRoles(roles);
            connection.commit();
            connection.setAutoCommit(true);
        }catch (Exception e) {e.printStackTrace(); }
    }

    private void saveRoles(List<DbRole> roles) {
        try {
            Map<String,DbRole> map=new HashMap<>();
            if(roles.size()==0) return;
            StringBuilder sb=new StringBuilder("INSERT IGNORE INTO roles (name) VALUES ");
            int l=0;
            for (DbRole dbRole : roles) {
                if(l!=0) sb.append(" , ");
                l++;
                sb.append(" ( \""+dbRole.getName()+"\" ) "); map.put(dbRole.getName(),dbRole);
            }
            if(l==0) return;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sb.toString());
            String sql="SELECT * FROM roles";
            statement= connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                int roleId=resultSet.getInt("id");
                String name=resultSet.getString("name");
                if(map.containsKey(name)) map.get(name).setId(roleId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveEmployees(List<DbEmployee> employees) {
        try {
            Map<Integer,DbEmployee> map=new HashMap<>();
            if(employees.size()==0) return;
            StringBuilder sb=new StringBuilder("INSERT INTO employees (name,surname,email,sn) VALUES ");
            int l=0;
            for (DbEmployee employee : employees) {
                if(l!=0) sb.append(" , ");
                l++;
                sb.append(" ( \""+employee.getName()+"\" , \""+employee.getSurname()+"\" , \""+employee.getEmail()+"\" , "+employee.getSn()+" ) ");
                map.put(employee.getSn(),employee);
            }
            if(l==0) return;

            sb.append(" AS new  ON DUPLICATE KEY UPDATE name=new.name , surname=new.surname , email=new.email ");
            Statement statement = connection.createStatement();
            statement.executeUpdate(sb.toString());
            statement=connection.createStatement();
            String sql="SELECT * FROM employees";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                int empId=resultSet.getInt("id");
                int sn=resultSet.getInt("sn");
                if(map.containsKey(sn)) map.get(sn).setId(empId);
            }
        }catch (Exception e) { e.printStackTrace(); }
    }

    private void clear(int companyId) {
        try {
            String sql = "DELETE FROM units WHERE companyId=? AND parentUnit is NULL";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,companyId);
            statement.executeUpdate();
        }catch (Exception e ){e.printStackTrace(); }

    }

    private int getId(String table, String name) {
        int ret = -2;
        try {
            String sql = "SELECT id FROM " + table + " WHERE name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ret = resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


}
