import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;

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
    private final String candidatesTable = "Candidates";

    // The name of the table containing the registered users
    //private final String registrationLog = "RegistrationLog";

    // The name of the table containing the voting log
    //private final String voteLog = "VoteLog";

    // The connection to the database used by the various methods of this class
    private Connection conn = null;


    public DatabaseManager() {
        try {
            this.conn = getConnection();
            System.out.println("Connected to database");
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
     * Run a SQL command which does not return a recordset:
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


    private String[] joinCandidateList(ArrayList<String> candidates) {
        ArrayList<String> positions = new ArrayList<String>();
        for(int i=0; i<candidates.size(); i++) {
            String[] thisResult = candidates.get(i).split(",");
            if (!positions.contains(thisResult[0])) {
                positions.add(thisResult[0]);
            }
        }
        String[] results = new String[positions.size()];
        for(int j=0; j<positions.size();j++) {
            results[j] = positions.get(j);
        }

        for(int k=0; k<candidates.size();k++) {
            String[] thisResult = candidates.get(k).split(",");
            int index = positions.indexOf(thisResult[0]);
            results[index] = results[index] + "," + thisResult[1];
        }
        return results;
    }


    public String[] getCandidates() {
        Statement stmt = null;
        try {
            String querey = "SELECT POSITION, NAME " +
                            "FROM " + this.candidatesTable +
                            " WHERE IS_OFFICIAL " +
                            "ORDER BY ORDERING";
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(querey);            
            if(!rs.isBeforeFirst()) {
                System.out.println("There are no candidates");
                return new String[0];
            } else {
                ArrayList<String> candidates = new ArrayList<String>();
                while (rs.next()) {
                    String position = rs.getString("POSITION");
                    String name = rs.getString("NAME");

                    String result = position + "," + name;
                    candidates.add(result);
                }
                return this.joinCandidateList(candidates);

            }
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR");
            e.printStackTrace();
            return new String[0];
        }

    }


    /**
     * Connect to MySQL and do some stuff.
     */
    public void run() {
        // Connect
        // Connect to MySQL

        // Let us vote for Bernie
        try {
            String voteString = "UPDATE " + this.candidatesTable +
                                " SET VOTES = VOTES + 1 " +
                                "WHERE POSITION='President' AND NAME='Bernard Sanders'";
            for(int i=0; i<20; i++) {
                this.executeUpdate(voteString);
            }
            System.out.println("Voted for Bernie 20 times");




            System.out.println("Now, let's check the results.");

            Statement stmt = null;
            String querey = "SELECT * FROM Candidates";

            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(querey);

            while (rs.next()) {
                String position = rs.getString("POSITION");
                String candidate = rs.getString("NAME");
                long numVotes = rs.getInt("VOTES");

                System.out.println("Position: " + position + "\tCandidate: "
                                    +candidate + "\t" + "Votes: " +
                                    numVotes);
            }

            System.out.println("\nNow let's see the candidates for the position of GOD");

            querey = "SELECT * FROM Candidates WHERE Position='President'";
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(querey);
            System.out.println(rs);
            while (rs.next()) {
                String position = rs.getString("POSITION");
                String candidate = rs.getString("NAME");
                long numVotes = rs.getInt("VOTES");
                int status = rs.getInt("IS_OFFICIAL");
                System.out.println("Position: " + position + "\tCandidate: "
                                    +candidate + "\t" + "Votes: " +
                                    numVotes);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: Could not drop the table");
            e.printStackTrace();
            return;
        }

        System.out.println("Let's look at the candidates with getCandidates()");
        String[] theCandidates = getCandidates();
        for ( String eachResult : theCandidates) {
            System.out.println(eachResult);
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