import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class will connect to a database and issue queries and updates as
 * necessary. 
 *
 * This code is adapted from the DBDemo.java file provided by Dr. X.
 *
 * The userName will be root and the password will be toor.
 *
 *
 */

public class databaseManager {

    // localhost username
    private final String userName = "root";

    // localhost password
    private final String password = "toor";

    // The name of the computer running MySQL
    private final String serverName = "localhost";

    // The port of the MySQL server (default is 3306)
    private final int portNumber = 3306;

    // The name of the database we are testing with (this default is installed with MySQL)
    private final String dbName = "test";
    
    // The name of the table we are testing with
    private final String tableName = "JDBC_TEST";


    /**
     * Get a new database connection
     * 
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        conn = DriverManager.getConnection("jdbc:mysql://"
                + this.serverName + ":" + this.portNumber + "/" + this.dbName,
                connectionProps);

        return conn;
    }


    /**
     * Run a SQL command which does not return a recordset:
     * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
     * 
     * @throws SQLException If something goes wrong
     */
    public boolean executeUpdate(Connection conn, String command) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(command); // This will throw a SQLException if it fails
            return true;
        } finally {

            // This will run whether we throw an exception or not
            if (stmt != null) { stmt.close(); }
        }
    }

    
}