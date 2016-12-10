import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.PreparedStatement;

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
    private final String dbName = "LSAM_evoting";
    
    // The name of the table containing the results (will change to results later)
    private final String candidatesTable = "Candidates";

    // The name of the table containing the registered users
    private final String registrationLog = "RegistrationLog";

    // The name of the table containing the voting log
    private final String votingLog = "VotingLog";

    // This will test whether our encryption is working
    private final String testBytes = "ByteTable";

    // The connection to the database used by the various methods of this class
    private Connection conn = null;


    public DatabaseManager() {
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


    /**
     * Run a SQL command which returns a ResultSet
     *
     * @throws SQLException if something goes wrong
     */
    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = null;
        stmt = this.conn.createStatement();
        return stmt.executeQuery(query);       
    }


    /**
     * This method will add a user to the database table containing
     * information about registered users.
     *
     * @param registrationID    This is a unique value assigned to each
     *                          voter.
     * @param PID                a string array containing a user's full
     *                           name, their date of birth, and their
     *                           social security number
     */
    protected void addRegisteredVoter( String registrationID, String[] PID) {
        String name = PID[0].toUpperCase().hashCode();
        String dob = PID[1].hashCode();
        int ssnHash = PID[2].hashCode();

        String query = "SELECT * FROM RegistrationLog WHERE " +
                       "NAME='" + name + "' AND DOB='" + dob + 
                       "' AND SSN=" + ssnHash;
        int regIDHash = registrationID.hashCode();

        try {
            if (!isRegistered(PID)) {
                String updateStmt = "INSERT INTO " + this.registrationLog + 
                                    " (NAME, DOB, SSN, REG_ID)" +
                                    " VALUES ('" + name + "', '" + dob +
                                    "', " + ssnHash + ", " + regIDHash + ")";
                executeUpdate(updateStmt);
            }
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR");
            e.printStackTrace();
        }
    }


    /**
     * This method will check is a given registration ID is in the
     * database table containing registration logs of each voter.
     * If the given registration ID is present that means the voter
     * is registered to vote.
     *
     * @param registrationID    This is a unique value assigned to each
     *                          voter.
     * @return                  A boolean value, with true indicating the
     *                          given user's registrationID is present in
     *                          the database
     */
    protected boolean isRegistered(String registrationID) {
        try {
            String query = "SELECT * FROM " + registrationLog +
                           " WHERE REG_ID=" + registrationID.hashCode();

            ResultSet rs = executeQuery(query);
            if (!rs.isBeforeFirst()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("ERROR DETERMINING REGISTRATION STATUS");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method will test to see if a user is registered. If the user
     * is registered, this method will return true, otherwise false.
     *
     * @param PID   a string array containing a user's full name, their date
     *              of birth, and their social security number.
     * @return      a boolean value where true means the user is in the
     *              registration database, and false means they are not.
     */
    protected boolean isRegistered(String[] PID) {
        String name = PID[0].toUpperCase().hashCode();
        String dob = PID[1].hashCode();
        int ssnHash = PID[2].hashCode();

        String query = "SELECT * FROM " + registrationLog + " WHERE " +
                       "NAME='" + name + "' AND DOB='" + dob + 
                       "' AND SSN=" + ssnHash;
        try {
            ResultSet rs = executeQuery(query);  

            if (!rs.isBeforeFirst()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("ERROR DETERMINING REGISTRATION STATUS");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method will check if a given registration ID is present in
     * the database table containing logs of every registration ID used
     * to cast a vote.
     * 
     * @param registrationID    This is a unique value assigned to each
     *                          voter.
     */
    private void addVote( String registrationID ) {
        try {
            if(!hasVoted(registrationID)) {
                String insertStmt = "INSERT INTO " + votingLog +
                                    " (REG_ID) VALUES (" + 
                                    registrationID.hashCode() + ")";
                executeUpdate(insertStmt);
            }
        } catch (SQLException e) {
            System.out.println("ERROR ADDING VOTE");
            e.printStackTrace();
        }
    }


    /**
     * This method will check the Voting Logs for the given registration
     * ID. If the ID is present, this indicates that the user with the
     * given registration ID has already voted. If the registration ID is
     * not present in the database, this indicates that the user has not
     * voted.
     *
     * @param registrationID    This is a unique value assigned to each
     *                          voter.
     * @return                  A boolean value, where true indicates that
     *                          the user with the given registration ID
     *                          has voted, and false indicates that the user
     *                          with the given registration ID has not voted.
     */
    protected boolean hasVoted( String registrationID ) {
        try {
            String query = "SELECT * FROM " + votingLog +
                           " WHERE REG_ID=" + registrationID.hashCode();
            ResultSet rs = executeQuery(query);

            if (!rs.isBeforeFirst()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("ERROR CHECKING DATABASE");
            e.printStackTrace();
            return false;
        }
    }


    private String verifyVote() {
        try {
            int numberOfVoters = 0;
            String selectStatement = "SELECT COUNT(REG_ID) FROM VotingLog";
            ResultSet rs = executeQuery(selectStatement);
            if (!rs.isBeforeFirst()) {
                numberOfVoters = 0;
            } else {
                rs.first();
                numberOfVoters = rs.getInt("COUNT(REG_ID)");
            }
            selectStatement = "SELECT MAX(VOTES) FROM Candidates";
            rs = executeQuery(selectStatement);
            rs.first();
            int maxVotesForACandidate = rs.getInt("MAX(VOTES)");

            String status;
            if (numberOfVoters >= maxVotesForACandidate) {
                status = "VALID CERTIFICATION";
            } else {
                status = "INVALID CERTIFICATION - POTENTIAL FRAUD DETECTED, USE PAPER BALLOTS";
            }
            String returnString = "VOTE CERTIFICATION: " + status + "\n" + 
                                numberOfVoters + 
                                " voters have voted with a maximum of " +
                                maxVotesForACandidate +
                                " votes for any one candidate.";
            return returnString;
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR");
            e.printStackTrace();
            return "";
        }
    }

    private void printPaperBallot(String[] selection) {
        for (String position: selection) {
            String[] pieces = position.split(",");
            if (pieces[1].equals("#")) {
                System.out.println("Position: " + pieces[0] + "\tAbstained");
            } else {
                System.out.println("Position: " + pieces[0] + "\t1 Vote For " + pieces[1]);
            }
        }
    }


    protected void castVote( String[] selection, String registrationID) {
        addVote(registrationID);
        printPaperBallot(selection);

        try {
            for (String position : selection) {
                String[] pieces = position.split(",");

                // If the user abstained, no more work is needed
                if (pieces[1].equals("#")) {
                    continue; }
                
                String query = "SELECT * FROM " + candidatesTable +
                               " WHERE POSITION='" + pieces[0] +
                               "' AND NAME='" + pieces[1] + "'";
                ResultSet rs = executeQuery(query);
                if(!rs.isBeforeFirst()) {
                    //need to get ordering of the position
                    String posQuery = "SELECT ORDERING FROM " + candidatesTable +
                                   " WHERE POSITION='" + pieces[0] + "'";
                    ResultSet rs2 = executeQuery(posQuery);
                    rs2.first();
                    int ordering = rs2.getInt("ORDERING");
                    // add the new candidate with one vote
                    String insertStmt = "INSERT INTO " + candidatesTable +
                                        " (POSITION, NAME, VOTES, IS_OFFICIAL, ORDERING) " +
                                        "VALUES ('" + pieces[0] + "','" +
                                        pieces[1] + "',1,0," + ordering + ")";
                    executeUpdate(insertStmt);
                } else {
                    String updateStmt = "UPDATE " + candidatesTable + 
                                        " SET VOTES = VOTES + 1 " +
                                        "WHERE POSITION='" + pieces[0] + "' AND NAME='" +
                                        pieces[1] + "'";
                    executeUpdate(updateStmt);
                }
            }
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR WHEN CASTING VOTE");
            e.printStackTrace();
        }
    }


    /**
     * This method will take an ArrayList of candidates and output a string
     * array that is formated as expected by System Logic, which is a string
     * array in which each index in the array is a position, with the
     * candidates and their votes appended to the position and separated by
     * commas.
     *
     * This method will take an ArrayList of candidates formated as:
     *   index[0] - President,Jon Snow
     *   index[1] - Vice President,Mike Pence
     *   ...
     *
     * The ArrayList may also contain the votes as well, formated as:
     *   index[0] - President,Jon Snow,5
     *   index[1] - Vice President,Mike Pence,2
     *   ...
     */
    private String[] joinCandidateList(ArrayList<String> candidates,
                                       boolean addVotes) {

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
            if (addVotes) {results[index] = results[index]+","+thisResult[2];}
        }
        return results;
    }


    /**
     * This method acts as a helper method to several other methods
     * in this class. It will access the candidate database table
     * and return either the official candidates or all candidates,
     * along with either returning the votes or not the votes.
     * The decision to gather certain data is determined by the boolean
     * values passed to it. 
     */
    private String[] parseCandidates(boolean onlyOfficialCandidates,
                                    boolean getVotes) {

        //This will change the SQL Query statement
        String selectClause;
        String whereClause;

        if (getVotes) {
            selectClause = "SELECT POSITION, NAME, VOTES ";
        } else {
            selectClause = "SELECT POSITION, NAME ";
        } 
        if (onlyOfficialCandidates) {
            whereClause = " WHERE IS_OFFICIAL ";
        } else {
            whereClause = " ";
        }

        try {
            String query = selectClause + " " +
                            "FROM " + this.candidatesTable +
                            whereClause +
                            "ORDER BY ORDERING";
            ResultSet rs = executeQuery(query);        
            if(!rs.isBeforeFirst()) {
                System.out.println("There are no candidates");
                return new String[0];
            } else {
                ArrayList<String> candidates = new ArrayList<String>();
                while (rs.next()) {
                    String position = rs.getString("POSITION");
                    String name = rs.getString("NAME");
                    String result = position + "," + name;
                    if (getVotes) { result = result + "," + rs.getInt("VOTES");}
                    candidates.add(result);
                }
                return this.joinCandidateList(candidates, getVotes);

            }
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR");
            e.printStackTrace();
            return new String[0];
        }

    }


    protected String[] getCandidates() {
        return parseCandidates(true, false);
    }

    protected String[] getTalley() {
        String[] tally = parseCandidates(false, true);
        String cert = verifyVote();

        String[] returnArray = new String[tally.length+1];
        returnArray[0] = cert;
        for (int i=0; i<tally.length; i++) {
            returnArray[i+1] = tally[i];
        }
        return returnArray;
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
            String query = "SELECT * FROM Candidates";

            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String position = rs.getString("POSITION");
                String candidate = rs.getString("NAME");
                long numVotes = rs.getInt("VOTES");

                System.out.println("Position: " + position + "\tCandidate: "
                                    +candidate + "\t" + "Votes: " +
                                    numVotes);
            }

            System.out.println("\nNow let's see the candidates for the position of President");

            query = "SELECT * FROM Candidates WHERE Position='President'";
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(query);
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

        System.out.println("Let's look at the candidates with getTalley()");
        String[] theTally = getTalley();
        for ( String each : theTally) {
            System.out.println(each);
        }

        System.out.println("Let's see if I am registered");
        String[] PID = {"Logan Allen Smith","06/20/1995","123456789"};
        System.out.println(isRegistered(PID));


        System.out.println("Let's add a voter and see if they show up.");
        System.out.println("Daniel Day Lewis, 11/11/1911, 987654321, 0123456789012345");
        String[] PID2 = {"Daniel Day Lewis", "11/11/1911", "987654321"};
        String regId = "0123456789012345";
        //addRegisteredVoter(regId, PID2);
        System.out.println("Daniel is registered? : " + isRegistered(PID2));

        System.out.println("Let's see if Daniel's REG_ID is in the database");
        System.out.println("0123456789012345 in database? " + isRegistered(regId));

        System.out.println("Let's add a vote and see if it shows in the database");
        addVote(regId);
        System.out.println("Has Daniel voted? : " + hasVoted(regId));

        System.out.println("Let's have Daniel vote for Ted Cruz and Sarah Palin." +
            "\nPlease not that this will only work here, as I am ignoring checks on " +
            "people who have already voted.");
        String[] dannielVote = {"President,Ted Cruz", "Vice President,Sarah Palin", "Senator,#", "Governor,#"};
        castVote(dannielVote, regId);

    }


    /**
     * Connect to the DB and do some stuff
     */
    public static void main(String[] args) {
        DatabaseManager app = new DatabaseManager();
        app.run();
    }

}