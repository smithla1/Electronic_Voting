import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * This class will connect to a database and issue queries and updates as
 * necessary. 
 *
 * This code is adapted from the DBDemo.java file provided by Dr. X.
 *
 * The userName will be root and the password will be toor.
 *  - for a real world application, this would be hashed instead of being
 *    stored as plain text.
 *
 */

public class DatabaseManager {

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
    
    // The name of the table containing the results (will change to results later)
    private final String candidatesTableName = "Candidates";

    // The name of the table containing the candidates
    //private final String candidatesTableName = "Candidates";

    // The name of the table containing the registered users
    //private final String registrationLog = "RegistrationLog";

    // The name of the table containing the voting log
    //private final String voteLog = "VoteLog";

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


    /**
     * Connect to MySQL and do some stuff.
     */
    public void run() {
        // Connect
        // Connect to MySQL
        Connection conn = null;
        try {
            conn = this.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the database");
            e.printStackTrace();
            return;
        }

        // Let us vote for Bernie
        try {
            String voteString = "UPDATE " + this.candidatesTableName +
                                " SET VotesRecieved = VotesRecieved + 1 " +
                                "WHERE Position='President' AND Name='Bernard Sanders'";
            for(int i=0; i<20; i++) {
                this.executeUpdate(conn, voteString);
            }
            System.out.println("Voted for Bernie 20 times");

            System.out.println("Now, let's check the results.");

            Statement stmt = null;
            String querey = "SELECT * FROM Candidates";

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(querey);

            while (rs.next()) {
                String position = rs.getString("Position");
                String candidate = rs.getString("Name");
                int numVotes = rs.getInt("VotesRecieved");

                System.out.println("Position: " + position + "\tCandidate: "
                                    +candidate + "\t" + "Votes: " +
                                    numVotes);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: Could not drop the table");
            e.printStackTrace();
            return;
        }

    }


    /**
     * Connect to the DB and do some stuff
     */
    public static void main(String[] args) {
        DatabaseManager app = new DatabaseManager();
        app.run();
    }

}