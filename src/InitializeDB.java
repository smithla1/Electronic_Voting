import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;


public class InitializeDB {

    // localhost username
    private final String userName = "root";

    // localhost password
    private final String password = "toor";

    // The name of the computer running MySQL
    private final String serverName = "localhost";

    // The port of the MySQL server (default is 3306)
    private final int portNumber = 3306;

    // The name of the database we are testing with (this default is installed with MySQL)
    private final String dbName = "evoting";
    
    // The name of the table containing the results (will change to results later)
    private final String candidatesTable = "Candidates";

    // The name of the table containing the registered users
    private final String registrationLog = "RegistrationLog";

    // The name of the table containing the voting log
    private final String votingLog = "VotingLog";

    // The connection to the database used by the various methods of this class
    private Connection conn = null;

    // DatabaseManager instance to insert data
    private DatabaseManager manager;


    public InitializeDB() {
        try {
            this.conn = getConnection();
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database");
            e.printStackTrace();
            return;
        }
    }


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
                + this.serverName + ":" + this.portNumber + "/" +
                this.dbName + "?autoReconnect=true&useSSL=false",
                connectionProps);

        return conn;
    }


    /**
     * Run a SQL command which does not return a RecordSet:
     * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
     * 
     * @throws SQLException If something goes wrong
     */
    public boolean executeUpdate(String command) throws SQLException {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(command); // This will throw a SQLException if it fails
            return true;
        } finally {
            // This will run whether we throw an exception or not
            if (stmt != null) { stmt.close(); }
        }
    }


    public boolean initialize() {
        try {
            //First we need to check if the tables are present in the database
            DatabaseMetaData dbm = this.conn.getMetaData();

            // First check for 
            ResultSet tables = dbm.getTables(null, null, "CANDIDATES", null);
            String createStatement;
            if (!tables.next()) {
                // Table exists
                createStatement = "CREATE TABLE Candidates " + " ( " +
                                  "POSITION varchar(30) NOT NULL, " +
                                  "NAME varchar(70) NOT NULL, " +
                                  "VOTES BIGINT(10) UNSIGNED NOT NULL, " +
                                  "IS_OFFICIAL TINYINT(1) UNSIGNED NOT NULL, " +
                                  "ORDERING INT(10) UNSIGNED NOT NULL, " +
                                  "PRIMARY KEY (POSITION, NAME))";
                executeUpdate(createStatement);

                String updateStatement = "INSERT INTO Candidates " +
                                         "(POSITION, NAME, VOTES, " +
                                         "IS_OFFICIAL, ORDERING) " +
                                         "VALUES " +
                                         "('President', 'Hillary Clinton', 0, 1, 1), " +
                                         "('President', 'Donald Trump', 0, 1, 1), " +
                                         "('Vice President', 'Tim Kaine', 0, 1, 1), " +
                                         "('Vice President', 'Mike Pence', 0, 1, 1), " +
                                         "('Senator', 'Sebastian Van Delden', 0, 1, 1), " +
                                         "('Senator', 'Xenia Mountrouidou', 0, 1, 1)";
                executeUpdate(updateStatement);
                System.out.println(updateStatement);
            }

            tables = dbm.getTables(null, null, "RegistrationLog", null);

            if (!tables.next()) {
                createStatement = "CREATE TABLE RegistrationLog " + " ( " +
                                  "NAME varchar(70) NOT NULL, " +
                                  "DOB DATE NOT NULL, " +
                                  "SSN INT(11) NOT NULL, " +
                                  "REG_ID INT(11) NOT NULL, " +
                                  "PRIMARY KEY (NAME, SSN))";
                executeUpdate(createStatement);
            } 

            tables = dbm.getTables(null, null, "VotingLog", null);

            if (!tables.next()) {
                createStatement = "CREATE TABLE VotingLog " + " ( " +
                                  "REG_ID INT(11) NOT NULL, " +
                                  "PRIMARY KEY (REG_ID))";
                executeUpdate(createStatement);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR");
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        InitializeDB init = new InitializeDB();
        boolean worked = init.initialize();
        System.out.println(worked);
    }
}