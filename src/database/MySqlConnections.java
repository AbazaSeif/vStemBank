/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alienware
 */
public class MySqlConnections {

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String CONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/RFIT";
    private static final String CONNECTION_USERNAME = "root";
    private static final String CONNECTION_PASSWORD = "trinitron";
    private Connection MySQLDB = null;
    private Statement Stat = null;

    public void createConnection() throws SQLException {
        try {
            Class.forName(DRIVER_CLASS);

        } catch (ClassNotFoundException e) {
            System.err.println("Driver class is not found, cause:"
                    + e.getMessage());
        }
        MySQLDB = DriverManager.getConnection(CONNECTION_URL + "?useUnicode=yes&characterEncoding=UTF-8", CONNECTION_USERNAME, CONNECTION_PASSWORD);
        Stat = MySQLDB.createStatement();
    }

    public ResultSet getAll(String TableName) {
        try {
            ResultSet rs = null;
            if (MySQLDB == null) {
                createConnection();
                MySQLDB.setAutoCommit(true);
            }

            rs = Stat.executeQuery("select * from " + TableName);

            return rs;
        } catch (SQLException ex) {
        }

        return null;
    }

    public List<List> get(String TableName, Map<String, String> Where, List<String> sql) {
        try {
            Map<String, String> ColType = GetTypes(TableName, Where);
            ResultSet rs = null;
            if (MySQLDB == null) {
                createConnection();
                MySQLDB.setAutoCommit(true);
            }
            String WhereQuerey = "";
            int Loop = 1;
            for (Map.Entry<String, String> entry : Where.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String Type = null;
                for (Map.Entry<String, String> TypeCol : ColType.entrySet()) {
                    if (TypeCol.getKey().equals(key)) {
                        Type = TypeCol.getValue();
                        break;
                    }
                }
                if (Type == null) {
                    break;
                }
                switch (Type) {
                    case "INT":
                        WhereQuerey += key + " = " + Integer.parseInt(value);
                        break;
                    case "TINYINT":
                        WhereQuerey += key + " = " + value;
                        break;
                    case "VARCHAR":
                        WhereQuerey += key + " = '" + value + "'";
                        break;
                    case "DATETIME":
                        WhereQuerey += key + " = '" + value + "'";
                        break;
                    default:
                        WhereQuerey += key + " = " + value;
                }
                if (Loop >= Where.size()) {
                    WhereQuerey += "";
                } else {
                    WhereQuerey += " AND ";
                }
                Loop++;
            }

            rs = Stat.executeQuery("select * from " + TableName + " Where " + WhereQuerey);
            List<List> FResult = new ArrayList<>();
            while (rs.next()) {
                List<String> Result = new ArrayList<>();
                for (String s : sql) {
                    try {
                        String TestType = rs.getObject(s).getClass().getTypeName();
                        switch (TestType) {
                            case "java.lang.String":
                                Result.add(rs.getString(s));
                                break;
                            case "java.sql.Timestamp":
                                Result.add(String.valueOf(rs.getTimestamp(s)));
                                break;
                            case "java.lang.Boolean":
                                Result.add(String.valueOf(rs.getBoolean(s)));
                                break;
                            case "java.lang.Integer":
                                Result.add(String.valueOf(rs.getInt(s)));
                                break;
                        }
                    } catch (NullPointerException e) {
                        Result.add("NULL");
                    }
                }
                FResult.add(Result);
            }
            Stat.close();
            rs.close();
            MySQLDB.close();
            MySQLDB = null;
            if (FResult.size() > 0) {
                return FResult;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            return null;
        }
    }

    private Map<String, String> GetTypes(String Table, Map<String, String> Data) {
        try {
            Map<String, String> ColType = new HashMap<>();
            if (MySQLDB == null) {
                createConnection();
                MySQLDB.setAutoCommit(true);
            }
            ResultSet RSBK;
            String QuerySelect = "Select * from " + Table + " LIMIT 1";
            RSBK = Stat.executeQuery(QuerySelect);
            int CountCol = RSBK.getMetaData().getColumnCount();
            for (Map.Entry<String, String> entry : Data.entrySet()) {
                for (int i = 1; i <= CountCol; i++) {
                    if (RSBK.getMetaData().getColumnName(i).equals(entry.getKey())) {
                        ColType.put(entry.getKey(), RSBK.getMetaData().getColumnTypeName(i));
                    }
                }
            }
            RSBK.close();
            Stat.close();
            MySQLDB.close();
            MySQLDB = null;
            return ColType;
        } catch (SQLException ex) {
            return null;
        }
    }

    public boolean set(String Table, Map<String, String> Data) {
        try {
            List<List> Result = get(Table, Data, java.util.Arrays.asList("name", "id", "cardid"));
            if (Result != null) {
                return false;
            }
            Map<String, String> ColType = GetTypes(Table, Data);
            ResultSet rs = null;
            if (MySQLDB == null) {
                createConnection();
                MySQLDB.setAutoCommit(true);
            }

            String QuereyKeys = "(";
            String QuereyValues = "(";
            int Loop = 1;
            for (Map.Entry<String, String> entry : Data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String Type = null;
                for (Map.Entry<String, String> TypeCol : ColType.entrySet()) {
                    if (TypeCol.getKey().equals(key)) {
                        Type = TypeCol.getValue();
                        break;
                    }
                }
                switch (Type) {
                    case "INT":
                        QuereyValues += Integer.parseInt(value);
                        break;
                    case "TINYINT":
                        QuereyValues += value;
                        break;
                    case "VARCHAR":
                        QuereyValues += "'" + value + "'";
                        break;
                    case "DATETIME":
                        QuereyValues += key + " = '" + value + "'";
                        break;
                    default:
                        QuereyValues += value;
                }
                if (Loop >= Data.size()) {
                    QuereyKeys += key + ")";
                    QuereyValues += ")";
                } else {
                    QuereyKeys += key + ",";
                    QuereyValues += ",";
                }
                Loop++;
            }

            String SQLQuery = "INSERT INTO " + Table + " " + QuereyKeys + " VALUES " + QuereyValues;
            Stat.executeUpdate(SQLQuery);
            return true;
        } catch (SQLException ex) {
            System.err.println("ERROR : " + ex.getMessage());
            return false;
        }
    }

    public boolean update(String Table, Map<String, String> UpdateData, Map<String, String> Where) {
        try {
            Map<String, String> ColType = GetTypes(Table, UpdateData);
            Map<String, String> ColTypeWhere = GetTypes(Table, Where);
            if (MySQLDB == null) {
                createConnection();
                MySQLDB.setAutoCommit(true);
            }
            String WhereQuerey = "";
            String UpdateQuerey = "";
            int Loop = 1;
            for (Map.Entry<String, String> entry : Where.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String Type = null;
                for (Map.Entry<String, String> TypeCol : ColTypeWhere.entrySet()) {
                    if (TypeCol.getKey().equals(key)) {
                        Type = TypeCol.getValue();
                        break;
                    }
                }
                switch (Type) {
                    case "INT":
                        WhereQuerey += key + " = " + Integer.parseInt(value);
                        break;
                    case "TINYINT":
                        WhereQuerey += key + " = " + value;
                        break;
                    case "VARCHAR":
                        WhereQuerey += key + " = '" + value + "'";
                        break;
                    case "DATETIME":
                        WhereQuerey += key + " = '" + value + "'";
                        break;
                    default:
                        WhereQuerey += key + " = " + value;
                }
                if (Loop >= Where.size()) {
                    WhereQuerey += "";
                } else {
                    WhereQuerey += " AND ";
                }
                Loop++;
            }
            Loop = 1;
            for (Map.Entry<String, String> entry : UpdateData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String Type = null;
                for (Map.Entry<String, String> TypeCol : ColType.entrySet()) {
                    if (TypeCol.getKey().equals(key)) {
                        Type = TypeCol.getValue();
                        break;
                    }
                }
                switch (Type) {
                    case "INT":
                        UpdateQuerey += key + " = " + Integer.parseInt(value);
                        break;
                    case "TINYINT":
                        UpdateQuerey += key + " = " + value;
                        break;
                    case "VARCHAR":
                        UpdateQuerey += key + " = '" + value + "'";
                        break;
                    case "DATETIME":
                        UpdateQuerey += key + " = '" + value + "'";
                        break;
                    default:
                        UpdateQuerey += key + " = " + value;
                }
                if (Loop >= UpdateData.size()) {
                    UpdateQuerey += " ";
                } else {
                    UpdateQuerey += " , ";
                }
                Loop++;
            }

            Stat.executeUpdate("UPDATE " + Table + " SET " + UpdateQuerey + " Where " + WhereQuerey);
            Stat.close();
            MySQLDB.close();
            MySQLDB = null;
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean Delete(String Table, Map<String, String> Where) {
        try {
            Map<String, String> ColType = GetTypes(Table, Where);
            ResultSet rs = null;
            if (MySQLDB == null) {
                createConnection();
                MySQLDB.setAutoCommit(true);
            }
            String WhereQuerey = "";
            int Loop = 1;
            for (Map.Entry<String, String> entry : Where.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String Type = null;
                for (Map.Entry<String, String> TypeCol : ColType.entrySet()) {
                    if (TypeCol.getKey().equals(key)) {
                        Type = TypeCol.getValue();
                        break;
                    }
                }
                if (Type == null) {
                    break;
                }
                switch (Type) {
                    case "INT":
                        WhereQuerey += key + " = " + Integer.parseInt(value);
                        break;
                    case "TINYINT":
                        WhereQuerey += key + " = " + value;
                        break;
                    case "VARCHAR":
                        WhereQuerey += key + " = '" + value + "'";
                        break;
                    case "DATETIME":
                        WhereQuerey += key + " = '" + value + "'";
                        break;
                    default:
                        WhereQuerey += key + " = " + value;
                }
                if (Loop >= Where.size()) {
                    WhereQuerey += "";
                } else {
                    WhereQuerey += " AND ";
                }
                Loop++;
            }

            Stat.executeUpdate("DELETE FROM " + Table + " Where " + WhereQuerey);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
