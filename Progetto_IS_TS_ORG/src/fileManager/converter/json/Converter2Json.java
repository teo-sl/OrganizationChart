package fileManager.converter.json;


import fileManager.converter.ConverterToFile;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import abstractSqlManager.AbstractSqlManager;


import java.io.FileWriter;
import java.sql.*;

public class Converter2Json extends AbstractSqlManager implements ConverterToFile {

    private JSONObject data;


    public Converter2Json() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void convertDb() {
        JSONObject roles = convert("roles");
        JSONObject employees = convertEmployees();
        JSONObject companies = convert("companies");
        JSONObject units = convertUnits();
        JSONObject u2r = convertU2R();
        JSONObject e2ur = convertE2UR();
        JSONArray array = new JSONArray();
        array.add(roles);
        array.add(employees);
        array.add(companies);
        array.add(units);
        array.add(u2r);
        array.add(e2ur);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("organizationalcharts", array);
        data = jsonObject;
    }

    @Override
    public boolean saveToFile(String path) {
        if (data == null) return false;
        try {
            FileWriter file = new FileWriter(path);
            file.write(data.toString());
            file.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private JSONObject convertEmployees() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            String sql = "SELECT * FROM employees";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("id", resultSet.getInt("id"));
                record.put("name", resultSet.getString("name"));
                record.put("surname", resultSet.getString("surname"));
                record.put("email", resultSet.getString("email"));
                record.put("sn", resultSet.getInt("sn"));
                array.add(record);
            }
            jsonObject.put("employees", array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject convert(String table) {
        JSONObject jsonObject = new JSONObject();
        try {

            JSONArray array = new JSONArray();
            String sql = "SELECT * FROM " + table + "";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("id", resultSet.getInt("id"));
                record.put("name", resultSet.getString("name"));
                array.add(record);
            }
            jsonObject.put(table, array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private JSONObject convertUnits() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sql = "SELECT * FROM units";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray array = new JSONArray();

            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("id", resultSet.getInt("id"));
                record.put("name", resultSet.getString("name"));
                record.put("companyId", resultSet.getInt("companyId"));
                record.put("parentUnit", resultSet.getInt("parentUnit"));
                record.put("code", resultSet.getInt("code"));
                array.add(record);
            }
            jsonObject.put("units", array);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject convertE2UR() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sql = "SELECT * FROM e2ur";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray array = new JSONArray();
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("id", resultSet.getInt("id"));
                record.put("empId", resultSet.getInt("empId"));
                record.put("u2rId", resultSet.getInt("u2rId"));
                array.add(record);
            }
            jsonObject.put("e2ur", array);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private JSONObject convertU2R() {
        JSONObject jsonObject = new JSONObject();
        try {
            String sql = "SELECT * FROM u2r";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            JSONArray array = new JSONArray();
            while (resultSet.next()) {
                JSONObject record = new JSONObject();
                record.put("id", resultSet.getInt("id"));
                record.put("unitId", resultSet.getInt("unitId"));
                record.put("roleId", resultSet.getInt("roleId"));
                array.add(record);
            }
            jsonObject.put("u2r", array);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
