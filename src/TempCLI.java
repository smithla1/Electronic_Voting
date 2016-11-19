
import java.util.Arrays;
import java.util.Scanner;

public class TempCLI {
	public static void main(String[] args) {
	String name,DOB,SSN,regID,choice, choice2;
	SystemLogic logic = new SystemLogic();
	Scanner userInput = new Scanner(System.in);
	System.out.println("Are you registered? (Y/N)");
	//Here I am grabbing the user input and assigning it to temp and stripping all non alpha characters
    String temp = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
    while(!temp.equalsIgnoreCase("Q")){
    	//Segment of CLI to handle registration.
    	if(temp.equalsIgnoreCase("N")){
    		System.out.println("OK beginning registration");
    		System.out.println("Please enter your full name in FIRST MIDDLE LAST");
    		//Need to make this safe before final
    		name = userInput.nextLine();
    		System.out.println("Please enter your date of birth in the format of: MM/DD/YYYY");
    		DOB = userInput.nextLine();
    		String[] DOBWithoutDelims = DOB.split("/-");
    		DOB = Arrays.toString(DOBWithoutDelims);
    		System.out.println("Please enter your Social Security Number");
    		SSN = userInput.nextLine();
    		String [] PID = {name, DOB, SSN};
    		
    		try {
    			registrationID currentUserID = new registrationID(PID);
    			if (!logic.userIsRegistered(currentUserID.getRegistrationID())){
    				logic.registerUser(PID, currentUserID.getRegistrationID());
    			}
    			else{
    				System.out.println("Error voter is already registered.");
    			}
    		} 
    		catch (Exception e) {
    			System.out.println("There was an error creating registration ID");
    			break;
    		}
    	 
     }
     //Segment of CLI to handle voting
     else if(temp.equalsIgnoreCase("Y")){
    	 System.out.println("Please enter your 10 digit registration number");
    	 //takes user input and parses out non alpha characters.
    	 regID = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
    	 try{
    	 if(logic.userIsRegistered(regID)){
    		 //Here we know that the user is registered.
    		 boolean procede = false;
    		 while (procede != true){
    			 System.out.println("Here are the canditidates: ");
    			 System.out.println(logic.getCandidates());
    			 System.out.println("Please select your candidate");
    			 choice=userInput.nextLine().replaceAll("[^a-zA-Z]+","");
    			 //This is just a safety check to ensure the chosen candidate is a valid one.
    			 if (Arrays.asList(logic.getCandidates()).contains(choice)){
    				 System.out.println("You chose: " + choice + "are you sure? (Y/N)");
    				 choice2 = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
    				 if (choice2.equalsIgnoreCase("Y")){
    					 procede = true;
    				 }
    				 else if(choice2.equalsIgnoreCase("N")){
    					 procede = false;
    				 }
    				 else{
    					 System.out.println("You chose: " + choice + "are you sure? (Y/N)");
    					 choice = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
    				 }
    			 }
    		 }
    		 //logic.castVote(selection);
    	 }
    	 }
    	 catch(Exception e){
    		 System.out.println("System is exiting");
    		 break;
    	 }
    	 
     }
     else{
    	 System.out.println("Please enter Y or N (or Q to quit)"); 
     }
    temp = userInput.next(); 
	}
    userInput.close();
	}
}