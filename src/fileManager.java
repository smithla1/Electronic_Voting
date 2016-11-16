import java.io.FileWriter;
import java.io.FileInputStream;
import java.lang.StringBuffer;
import java.io.IOException;

public class fileManager {
    private FileWriter registration_out_file = null;
    private String registration_in_file = "registrationLogFile.csv";
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
            information.append(encryptedRegistrationID);
            information.append("\n");

            registration_out_file.write(information.toString());
            registration_out_file.close();

            System.out.println(information);

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
    
    private boolean inRegistrationDB( byte[] encryptedRegistrationID ) {
        return false;
    }
    
    private boolean inVoterDB( byte[] encryptedRegistrationID ) {
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
        fileManager myManager = new fileManager();
        String[] myPID = {"Logan", "Allen", "Smith"};
        myManager.addRegisteredVoter(new byte[0], myPID);

    }
}
