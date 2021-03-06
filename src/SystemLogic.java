import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SystemLogic {
    private boolean isAdministrator;
    private boolean registeringOrVoting;
    private String[] candidates;
    private DatabaseManager myManager;
    private registrationID currentUserID;
    
    public SystemLogic() {
        //initialize and or connect to database
        isAdministrator = false; //set this to false by default, according to our admin log in method
        myManager = new DatabaseManager();
    }
   
    protected boolean registerOrVote(boolean decision){
    	registeringOrVoting = decision;
        return registeringOrVoting;
    }
    
    protected String getUserPID(String[] PID) {
        return PID.toString();
    }
    
    protected String createRegistrationNumber(String[] PID) {
    	try{
    		currentUserID = new registrationID(PID);
    		//I decided to return the regID but this could just as easily be a secure regID
    		return currentUserID.getRegistrationID();
    	}
    	catch(Exception e){
    		return "There was an error creating a registration number";
    	}
    }
    
    protected void registerUser(String[] PID, String registrationID) throws Exception{ //The following three methods are different from our deliverable
        myManager.addRegisteredVoter(registrationID, PID);
    }
    
    protected boolean userIsRegistered(String registrationID) throws Exception {
    	try{
    		if(myManager.isRegistered(registrationID)){
    			return true;
    		}
    		else{
    			return false;
    		}
    	}
    	catch(Exception e){
            e.printStackTrace();
    		System.out.println("There was an error checking if the user was registered");
    		throw e;
    		
        }
    }

    protected boolean userIsRegistered(String[] PID) throws Exception {
        try{
            if(myManager.isRegistered(PID)){
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("There was an error checking if the user was registered");
            throw e;
            
        }
    }    
    
    protected boolean userHasVoted(String registrationID) {
        try {
			return myManager.hasVoted(registrationID);
		} catch (Exception e){
            e.printStackTrace();
			System.out.println("There was an error determining if this user has voted");
			//return true so user cannot vote if an error occurs at this stage.
			return true;
		}
    }
    
    protected String[] getCandidates() {
    	candidates = myManager.getCandidates();
        return candidates;
    }
    
    protected void castVote(String[] selection, String registrationID) {
        myManager.castVote(selection, registrationID);
    }
    
    protected void printBallot(String[] selection) { //Different from our third deliverable
    	System.out.println(selection);
    }
    
    protected String[] getTalley() {
    	return myManager.getTalley();
    }
    
    public static void main(String[] args) {
        
    }
}
