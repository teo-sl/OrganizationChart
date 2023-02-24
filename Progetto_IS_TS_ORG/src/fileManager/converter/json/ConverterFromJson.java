package fileManager.converter.json;

import abstractSqlManager.AbstractSqlManager;
import fileManager.converter.ConverterFromFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sqlManager.dbmodel.dbunit.DbUnit;

import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class ConverterFromJson extends AbstractSqlManager implements ConverterFromFile {
    private JSONParser jsonParser;


    public ConverterFromJson() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            jsonParser = new JSONParser();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean convertFromFile(String path) {
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
            JSONArray array = (JSONArray) jsonObject.get("organizationalcharts");
            return getDbFromJson(array);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean getDbFromJson(JSONArray array) {
        try {
            if (array == null) return false;
            connection.setAutoCommit(false);

            removeAll();
            load((JSONArray) ((JSONObject) array.get(0)).get("roles"), "roles");
            loadEmployees((JSONArray) ((JSONObject) array.get(1)).get("employees"));
            load((JSONArray) ((JSONObject) array.get(2)).get("companies"), "companies");
            loadUnits((JSONArray) ((JSONObject) array.get(3)).get("units"));
            loadU2R((JSONArray) ((JSONObject) array.get(4)).get("u2r"));
            loadE2UR((JSONArray) ((JSONObject) array.get(5)).get("e2ur"));

            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        return true;
    }

    private void loadU2R(JSONArray jsonArray) throws SQLException {
        if (jsonArray == null) return;
        StringBuilder sb = new StringBuilder("INSERT INTO u2r (id,unitId,roleId) VALUES ");
        int l = 0;
        for (Object object : jsonArray) {
            JSONObject record = (JSONObject) object;
            int id = Integer.parseInt(record.get("id") + "");
            int unitId = Integer.parseInt(record.get("unitId") + "");
            int roleId = Integer.parseInt(record.get("roleId") + "");
            if (l != 0) sb.append(" , ");
            l++;
            sb.append(" ( " + id + " , " + unitId + " , " + roleId + " ) ");
        }
        if (l == 0) return;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sb.toString());
    }

    private void load(JSONArray jsonArray, String table) throws SQLException {
        if (jsonArray == null) return;
        StringBuilder sb = new StringBuilder("INSERT INTO " + table + " (id,name) VALUES ");
        int l = 0;
        for (Object object : jsonArray) {
            JSONObject record = (JSONObject) object;
            int id = Integer.parseInt(record.get("id") + "");
            String name = (String) record.get("name");
            if (l != 0) sb.append(" , ");
            l++;
            sb.append(" ( " + id + " , \"" + name + "\" ) ");
        }
        if (l == 0) return;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sb.toString());
    }

    private void loadUnits(JSONArray jsonArray) throws Exception {
        if (jsonArray == null) return;
        StringBuilder sb = new StringBuilder("INSERT INTO units (id,name,companyId,parentUnit,code) VALUES ");
        int l = 0;
        Map<Integer, List<DbUnit>> map = new HashMap<>();
        for (Object object : jsonArray) {
            JSONObject record = (JSONObject) object;
            int id = Integer.parseInt(record.get("id") + "");
            String name = (String) record.get("name");
            int companyId = Integer.parseInt(record.get("companyId") + "");
            int parentUnit = Integer.parseInt(record.get("parentUnit") + "");
            int code = Integer.parseInt(record.get("code") + "");
            if (!map.containsKey(companyId)) map.put(companyId, new ArrayList<>());
            DbUnit tmp = new DbUnit(name, id, code, null, parentUnit, -1, null); //per la radice il parentUnit è 0
            map.get(companyId).add(tmp);
        }
        for (Integer company : map.keySet()) {
            if (map.get(company).size() == 0) throw new IllegalArgumentException();
            LinkedList<DbUnit> dbUnits = new LinkedList<>();
            for (DbUnit u : map.get(company))
                if (u.getParentUnitId() == 0) {
                    dbUnits.add(u);
                    break;
                }
            while (!dbUnits.isEmpty()) {
                DbUnit unit = dbUnits.removeFirst();
                Integer parentUnitId = null;
                if (unit.getParentUnitId() != 0) parentUnitId = unit.getParentUnitId();

                if (l != 0) sb.append(" , ");
                l++;
                sb.append(" ( " + unit.getId() + " , \"" + unit.getName() + "\" , " + company + " , " + parentUnitId + " , " + unit.getCode() + " ) ");
                for (DbUnit u : map.get(company)) {
                    if (u.getParentUnitId() == unit.getId())
                        dbUnits.addLast(u);
                }
            }
        }
        if (l == 0) return;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sb.toString());
    }

    private void loadE2UR(JSONArray jsonArray) throws SQLException {
        if (jsonArray == null) return;
        StringBuilder sb = new StringBuilder("INSERT INTO e2ur (id,empId,u2rId) VALUES ");
        int l = 0;
        for (Object object : jsonArray) {

            JSONObject record = (JSONObject) object;
            int id = Integer.parseInt(record.get("id") + "");
            int empId = Integer.parseInt(record.get("empId") + "");
            int u2rId = Integer.parseInt(record.get("u2rId") + "");
            if (l != 0) sb.append(" , ");
            l++;
            sb.append(" ( " + id + " , " + empId + " , " + u2rId + " ) ");
        }
        if (l == 0) return;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sb.toString());
    }

    private void loadEmployees(JSONArray jsonArray) throws SQLException {
        String sql = "INSERT INTO employees (id,name,surname,email,sn) VALUES (?,?,?,?,?)";

        for (Object object : jsonArray) {
            PreparedStatement statement = connection.prepareStatement(sql);
            JSONObject record = (JSONObject) object;
            int id = Integer.parseInt(record.get("id") + "");
            String name = (String) record.get("name");
            String surname = (String) record.get("surname");
            String email = (String) record.get("email");
            int sn = Integer.parseInt(record.get("sn") + "");
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setString(4, email);
            statement.setInt(5, sn);
            statement.executeUpdate();
        }

    }

    private void removeAll() throws SQLException {
        String[] tables = {"companies", "roles", "employees"};
        for (String table : tables) {
            String sql = " DELETE FROM " + table + " ; ";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
    }


}
