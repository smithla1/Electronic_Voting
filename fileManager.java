import java.io.*;

public class fileManager {
    private FileOutputStream registration_out_file;
    private FileInputStream registration_in_file;
    private FileOutputStream voter_out_file;
    private FileInputStream voter_in_file;
    private FileOutputStream results_out_file;
    private FileInputStream results_in_file;
    
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
}
