
public class SystemLogic {
    //private DataBaseManagment output_class = new DataBaseManagment()
    private boolean isAdministrator;
    private boolean registeringOrVoting;
    private String[] candidates;
    
    public SystemLogic() {
        //initialize and or connect to database
        isAdministrator = false; //set this to false by default, according to our admin log in method
    }
   
    private boolean registerOrVote(boolean decision){
        return true;
    }
    
    private String getUserPID(String[] PID) {
        return "";
    }
    
    private String createRegistrationNumber(String[] PID) {
        return "";
    }
    
    private void registerUser(String[] PID, String registrationID){ //The following three methods are different from our deliverable
        //encrypt ID and register user
    }
    
    private boolean userIsRegistered(String registrationID) {
        return true;
    }
    
    private boolean userHasVoted(String registrationID) {
        return true;
    }
    
    private String[] getCandidates() {
        return new String[0];
    }
    
    private void castVote(String[] selection) {
        //enter their vote and record that they have voted
    }
    
    private void printBallot(String[] selection) { //Different from our third deliverable
        //print the vote
    }
    
    private String[] getTalley() {
        return new String[0];
    }
    
    public static void main(String[] args) {
        
    }
}
