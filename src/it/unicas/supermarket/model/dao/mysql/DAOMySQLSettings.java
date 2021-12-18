package it.unicas.supermarket.model.dao.mysql;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe delle impostazioni DAO-MySQL
 */
public class DAOMySQLSettings {

    // impostazioni di default
    public final static String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
    public final static String HOST = "localhost";
    public final static String USERNAME = "market_user";
    public final static String PWD = "ROOT";
    public final static String SCHEMA = "market";
    public final static String PARAMETERS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private String host = "localhost";
    private String username = "market_user";
    private String password = "ROOT";
    private String schema = "market";

    private static DAOMySQLSettings currentDAOMySQLSettings = null;

    // getter e setter: Host
    public String getHost()                                 { return host; }
    public void setHost(String host)                        { this.host = host; }

    // getter e setter: Username
    public String getUsername()                             { return username; }
    public void setUsername(String username)                { this.username = username; }

    // getter e setter: Password
    public String getPassword()                             { return password; }
    public void setPassword(String password)                { this.password = password; }

    // getter e setter: Schema
    public String getSchema()                               { return schema; }
    public void setSchema(String schema)                    { this.schema = schema; }

    // getter e setter impostazioni
    public static DAOMySQLSettings getCurrentDAOMySQLSettings(){
        if (currentDAOMySQLSettings == null){
            currentDAOMySQLSettings = getDefaultDAOSettings();
        }
        return currentDAOMySQLSettings;
    }
    public static void setCurrentDAOMySQLSettings(DAOMySQLSettings daoMySQLSettings){
        currentDAOMySQLSettings = daoMySQLSettings;
    }

    static{
        try {
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Il metodo setta le impostazione di default
     * @return daoMySQLSettings
     */
    public static DAOMySQLSettings getDefaultDAOSettings(){
        DAOMySQLSettings daoMySQLSettings = new DAOMySQLSettings();
        daoMySQLSettings.host = HOST;
        daoMySQLSettings.username = USERNAME;
        daoMySQLSettings.schema = SCHEMA;
        daoMySQLSettings.password = PWD;
        return daoMySQLSettings;
    }

    /**
     * Metodo di utilita che restituisce lo statement a partire dall connessione corrente
     * @return statement con connessione definita dalla classe settings
     */
    public static Statement getStatement() throws SQLException{
        if (currentDAOMySQLSettings == null){
            currentDAOMySQLSettings = getDefaultDAOSettings();
        }
        return DriverManager.getConnection("jdbc:mysql://" + currentDAOMySQLSettings.host  + "/" + currentDAOMySQLSettings.schema + PARAMETERS, currentDAOMySQLSettings.username, currentDAOMySQLSettings.password).createStatement();
    }

    /**
     * metodo di utilita' che chiude la connessione e lo statement
     */
    public static void closeStatement(Statement st) throws SQLException{
        st.getConnection().close();
        st.close();
    }

}
