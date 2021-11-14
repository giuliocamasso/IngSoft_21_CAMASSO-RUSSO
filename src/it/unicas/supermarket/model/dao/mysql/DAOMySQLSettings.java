package it.unicas.supermarket.model.dao.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOMySQLSettings {

    // Default settings
    public final static String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
    public final static String HOST = "localhost";
    public final static String USERNAME = "market_user";
    public final static String PWD = "ROOT";
    public final static String SCHEMA = "market";
    public final static String PARAMETERS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // local DB data
    private String host = "localhost";
    // private String host = "127.0.0.1";
    private String username = "market_user";
    private String password = "ROOT";
    private String schema = "market";

    // getter and setter: @Host
    public String getHost()                                 { return host; }

    public void setHost(String host)                        { this.host = host; }

    // getter and setter: @Username
    public String getUsername()                             { return username; }

    public void setUsername(String username)                { this.username = username; }

    // getter and setter: @Password
    public String getPassword()                             { return password; }

    public void setPassword(String password)                { this.password = password; }

    // getter and setter: @schema
    public String getSchema()                               { return schema; }

    public void setSchema(String schema)                    { this.schema = schema; }

    static{
        try {
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static DAOMySQLSettings currentDAOMySQLSettings = null;

    public static DAOMySQLSettings getCurrentDAOMySQLSettings(){
        if (currentDAOMySQLSettings == null){
            currentDAOMySQLSettings = getDefaultDAOSettings();
        }
        return currentDAOMySQLSettings;
    }

    public static DAOMySQLSettings getDefaultDAOSettings(){
        DAOMySQLSettings daoMySQLSettings = new DAOMySQLSettings();
        daoMySQLSettings.host = HOST;
        daoMySQLSettings.username = USERNAME;
        daoMySQLSettings.schema = SCHEMA;
        daoMySQLSettings.password = PWD;
        return daoMySQLSettings;
    }

    public static void setCurrentDAOMySQLSettings(DAOMySQLSettings daoMySQLSettings){
        currentDAOMySQLSettings = daoMySQLSettings;
    }

    public static Statement getStatement() throws SQLException{
        if (currentDAOMySQLSettings == null){
            currentDAOMySQLSettings = getDefaultDAOSettings();
        }
        return DriverManager.getConnection("jdbc:mysql://" + currentDAOMySQLSettings.host  + "/" + currentDAOMySQLSettings.schema + PARAMETERS, currentDAOMySQLSettings.username, currentDAOMySQLSettings.password).createStatement();
    }

    public static void closeStatement(Statement st) throws SQLException{
        st.getConnection().close();
        st.close();
    }

}
