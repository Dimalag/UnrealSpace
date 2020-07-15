package org.UnrealSpace.Database;

import java.sql.*;

public class Database {
    private static Database DATABASE_SINGLE_INSTANCE = null;

    private final String name = "UnrealSpace";
    private final String url = "jdbc:postgresql://localhost:5432/" + name;
    private final String username = "marssoul";
    private final String password = "64918534519735194928752937562038465629838475";
    private Connection connection = null;

    private Database() {
        try {
            connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void connect() throws Exception {
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to db");
    }

    /*public boolean save(String tableName, String signatureDBVars, String vars) {
        return execute("insert into " + tableName + " (" + signatureDBVars + ") values(" +
                vars + ")", true);
    }*/

    public Connection getConn() { return connection; }
    public void executeUpdate(String query) {
        try (Statement statement = connection.createStatement()) {
            ResultSet testt = statement.getResultSet();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(query);
        }
    }

    public <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> int save(String procName, T1 value1, T2 value2, T3 value3, T4 value4, T5 value5,
                                                                   T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            setProc(proc, 3, value3);
            setProc(proc, 4, value4);
            setProc(proc, 5, value5);
            setProc(proc, 6, value6);
            setProc(proc, 7, value7);
            setProc(proc, 8, value8);
            setProc(proc, 9, value9);
            setProc(proc, 10, value10);
            setProc(proc, 11, value11);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1, T2, T3, T4, T5, T6, T7> int save(String procName, T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?, ?, ?, ?, ?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            setProc(proc, 3, value3);
            setProc(proc, 4, value4);
            setProc(proc, 5, value5);
            setProc(proc, 6, value6);
            setProc(proc, 7, value7);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1, T2, T3, T4, T5, T6> int save(String procName, T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?, ?, ?, ?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            setProc(proc, 3, value3);
            setProc(proc, 4, value4);
            setProc(proc, 5, value5);
            setProc(proc, 6, value6);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1, T2, T3, T4, T5> int save(String procName, T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?, ?, ?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            setProc(proc, 3, value3);
            setProc(proc, 4, value4);
            setProc(proc, 5, value5);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1, T2, T3, T4> int save(String procName, T1 value1, T2 value2, T3 value3, T4 value4) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?, ?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            setProc(proc, 3, value3);
            setProc(proc, 4, value4);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1, T2, T3> int save(String procName, T1 value1, T2 value2, T3 value3) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            setProc(proc, 3, value3);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1, T2> int save(String procName, T1 value1, T2 value2) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?, ?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            setProc(proc, 2, value2);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public <T1> int save(String procName, T1 value1) {
        try (CallableStatement proc = connection.prepareCall("{call " + procName + "(?)}")) {
            proc.registerOutParameter(1, Types.INTEGER);
            setProc(proc, 1, value1);
            proc.execute();
            return proc.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public void clearAll() {
        try (CallableStatement proc = connection.prepareCall("{call clear_all()}")) {
            proc.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static <T> void setProc(CallableStatement proc, int index, T value) throws SQLException {
        if (value == null)
            proc.setNull(index, 0);
        else if (value instanceof Boolean)
            proc.setBoolean(index, (Boolean)value);
        else if (value instanceof Integer)
            proc.setInt(index, (Integer)value);
        else if (value instanceof Float)
            proc.setFloat(index, (Float)value);
        else if (value instanceof String)
            proc.setString(index, (String) value);
        else
            throw new SQLException("setProc not that type" + value.toString());
    }

    /*public static <T1, T2, T3, T4, T5, T6> String createVars(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6) {
        return createVar(value1) + ", " + createVar(value2)
                + ", " + createVar(value3) + ", " + createVar(value4)
                + ", " + createVar(value5) + ", " + createVar(value6);
    }

    public static <T1, T2, T3, T4, T5> String createVars(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
        return createVar(value1) + ", " + createVar(value2)
                + ", " + createVar(value3) + ", " + createVar(value4) + ", " + createVar(value5);
    }

    public static <T1, T2, T3, T4> String createVars(T1 value1, T2 value2, T3 value3, T4 value4) {
        return createVar(value1) + ", " + createVar(value2)
                + ", " + createVar(value3) + ", " + createVar(value4);
    }

    public static <T1, T2, T3> String createVars(T1 value1, T2 value2, T3 value3) {
        return createVar(value1) + ", " + createVar(value2) + ", " + createVar(value3);
    }

    public static <T1, T2> String createVars(T1 value1, T2 value2) {
        return createVar(value1) + ", " + createVar(value2);
    }

    public static <T1> String createVars(T1 value1) {
        return createVar(value1);
    }

    private static <T> String createVar(T value) {
        if (value == null)
            return "NULL";
        else if (value instanceof String)
            return "'" + value.toString() + "'";
        else return value.toString();
    }*/

    public static Database getInstance() {
        if (DATABASE_SINGLE_INSTANCE == null)
            DATABASE_SINGLE_INSTANCE = new Database();
        return DATABASE_SINGLE_INSTANCE;
    }
}
