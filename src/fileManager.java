import java.io.FileWriter;
import java.io.FileInputStream;
import java.lang.StringBuffer;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class fileManager {
    private FileWriter registration_out_file = null;
    private FileReader registration_in_file = null;
    private String voter_out_file = "voterLogFile.csv";
    private String voter_in_file = "voterLogFile.csv";
    private String results_out_file = "resultsLogFile.csv";
    private String results_in_file = "resultsLogFile.csv";

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
    
    private boolean hasVoted( byte[] encryptedRegistrationID ) {
        return false;
    }
    
    private String[] getCandidates() {
        return new String[0];
    }
    
    private void castVote(String[] selection) {
        
    }
    
    private String[] getTalley() {
        return new String[0];
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
    }
}
