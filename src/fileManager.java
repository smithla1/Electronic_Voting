import java.io.FileWriter;
import java.io.FileInputStream;
import java.lang.StringBuffer;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class fileManager {
    private FileWriter registration_out_file = null;
    private FileReader registration_in_file = null;
    private FileWriter voter_out_file = null;
    private FileReader voter_in_file = null;
    private FileWriter results_out_file = null;
    private FileReader results_in_file = null;

    public fileManager() {
        registration_in_file = null;
    }


    private void addRegisteredVoter( byte[] encryptedRegistrationID, String[] PID) {
        try {
            registration_out_file = new FileWriter("regLog.csv", true);
            StringBuffer information = new StringBuffer();
            for(int i =0; i<PID.length; i++) {
                information.append(PID[i]);
                information.append(",");
            }
            information.append(encryptedRegistrationID.toString());
            information.append("\n");

            registration_out_file.write(information.toString());
            registration_out_file.close();
            registration_out_file = null;

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    
    private boolean isRegistered( byte[] encryptedRegistrationID ) {
        try {
            registration_in_file = new FileReader("regLog.csv");
            BufferedReader br = new BufferedReader(registration_in_file);

            String line = br.readLine();
            while( line != null ) {
                if ( encryptedRegistrationID.toString().equals(line.split(",")[3])) {
                    return true;
                }
                line = br.readLine();
            }

            return false;

        } catch (FileNotFoundException fnfe) {
            // The file was not found
            // This could mean that no one has been registered to vote,
            //  as the file would exist if someone had.
            // In this case, the person we are checking for hasn't registered
            //  so we should return false.
            return false;

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }


    private void addVote( byte[] encryptedRegistrationID ) {

        try {
            voter_out_file = new FileWriter("voterLogs.csv", true);

            voter_out_file.write(encryptedRegistrationID.toString());
            voter_out_file.close();
            voter_out_file = null;

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }        
    }

    
    private boolean hasVoted( byte[] encryptedRegistrationID ) {
        try {
            voter_in_file = new FileReader("voterLogs.csv");
            BufferedReader br = new BufferedReader(voter_in_file);

            String line = br.readLine();
            while( line != null ) {
                if ( encryptedRegistrationID.toString().equals(line.split(",")[3])) {
                    return true;
                }
                line = br.readLine();
            }

            return false;

        } catch (FileNotFoundException fnfe) {
            // The file was not found
            // This could mean that no one has case a vote,
            //  as the file would exist if someone had.
            // In this case, the person we are checking for hasn't voted
            //  so we should return false.
            return false;

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        
        } finally {
            try {
                if (voter_in_file != null) {
                voter_in_file.close();
                voter_in_file = null;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }

   /**
    * This method will add the voter's encrypted registration ID to the list
    * of encrypted registration ID's. 
    *
    * It will also cast the user's vote. It is important to note that the
    * user's vote WILL NOT be associated with their registration ID. The
    * registration ID will be placed in a file that contains the registration
    * ID of every person that has cast a vote. The candidates that others select
    * are placed in a different file which is used to keep track of the votes
    * each candidate has accumulated.
    *
    */
    private void castVote(String[] selection, byte[] encryptedRegistrationID) {
        // Add their registration ID to the file keeping track of who
        // all has voted.
        addVote(encryptedRegistrationID);

    }

   /**
    * This method will return the official candidates for each position.
    * It will read candidates.csv and look at each line, which has the
    * following format:
    *   Position,Candidate,VotesForCandidate,...,WRITE-IN,Candidate,VotesForCandidate,...
    * In this implementation, the official candidates are in the first
    * section.
    */
    private String[] getCandidates() {
        String[] candidates = parseCandidates();
        return candidates;
    }
    

    private String[] getTalley() {
        return new String[0];
    }

    private String[] parseCandidates() {
        try {
            results_in_file = new FileReader("candidates.csv");
            BufferedReader br = new BufferedReader(results_in_file);

            ArrayList<String> positions = new ArrayList<String>();

            String line = br.readLine();
            while (line != null) {
                positions.add(line);
                line = br.readLine();
            }

            String[] information = new String[positions.size()];
            return positions.toArray(information);

        } catch (FileNotFoundException fnfe) {
            // The file was not found
            // This could mean that no candidates have been entered.
            // There are also no positions which means the file has
            // not been populated beforehand. This is a critical flaw.
            // WE NEED TO DECIDE HOW TO HANDLE THIS UPSTREAM
            return new String[0];

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new String[0];

        } finally {
            try {
                if (results_in_file != null) {
                    results_in_file.close();
                    results_in_file = null;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }


    public static void main(String[] args) {
        //Create a fileManager class
        fileManager myManager = new fileManager();

        //Create some dummy data
        String[] myPID = {"Logan", "Werbenjagermanjensen", "Smith"};
        byte[] testID = new byte[8];

        //Register this dummy
        myManager.addRegisteredVoter(testID, myPID);

        System.out.print("Logan is registered to vote: ");

        //Test finding an entry
        System.out.println(myManager.isRegistered(testID));

        String[] theCandidates = myManager.getCandidates();
        for (int i = 0; i<theCandidates.length; i++) {
            System.out.println(theCandidates[i]);
        }
    }
}
