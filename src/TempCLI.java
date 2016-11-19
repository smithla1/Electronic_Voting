
import java.util.Arrays;
import java.util.Scanner;

public class TempCLI {

	public static void main(String[] args) throws Exception {

    	String name,DOB,SSN,regID,choice,choice2;
    	String[] finalSelection = new String[3];

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

                    //System.out.println(currentUserID.getRegistrationID());

        			if (!logic.userIsRegistered(PID)){
        				logic.registerUser(PID, currentUserID.getRegistrationID());
        				System.out.println("You have been registered successfully here is your registration number: " + currentUserID.getRegistrationID());
                        System.out.println("Make sure to hold on to that number, as you will need it to vote!");
                        System.out.println("Have a nice day!");
                        System.exit(0);
        			}
        			else{
        				System.out.println("You are already registered!");
                        System.out.println("Have a nice day!");
                        System.exit(0);
        			}
        		} 
        		catch (Exception e) {
                    e.printStackTrace();
        			System.out.println("There was an error creating registration ID");
        			break;
        		}
                finally {
                    try {
                        if (userInput != null) {
                            userInput.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        	 
            }
             //Segment of CLI to handle voting
             else if(temp.equalsIgnoreCase("Y")){
            	 System.out.println("Please enter your 16 digit registration number");
            	 //takes user input and parses out non alpha characters.
            	 regID = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
            	 //try{
	            	 if(logic.userIsRegistered(regID)){
	            		 //Here we know that the user is registered.
	            		 boolean procede = false;
	            		 while (procede != true){
	            			 System.out.println("Here are the canditidates: ");
	            			 System.out.println(logic.getCandidates()[0]);
	            			 System.out.println("Please select your presidential candidate");
	            			 choice=userInput.nextLine();
	            			 //This is just a safety check to ensure the chosen candidate is a valid one.
	            			 boolean contains = false;
	            			 for (String item : logic.getCandidates()[0].split(",")) {
	            			     if (choice.equalsIgnoreCase(item)) {
	            			         contains = true;
	            			         break; // No need to look further.
	            			     } 
	            			 }
	            			 if (contains){
	            				 System.out.println("You chose: " + choice.toLowerCase() + " are you sure? (Y/N)");
	            				 choice2 = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
	            				 if (choice2.equalsIgnoreCase("Y")){
	            					 procede = true;
	            				 }
	            				 else if(choice2.equalsIgnoreCase("N")){
	            					 procede = false;
	            				 }
	            				 else{
	            					 System.out.println("Please choose a valid candidate");
	            				 }
	            				 finalSelection[0]=choice.toLowerCase();
	            			 }
	            		 }
	            		 procede = false;
	            		 while (procede != true){
	            			 System.out.println("Here are the canditidates: ");
	            			 System.out.println(logic.getCandidates()[1]);
	            			 System.out.println("Please select your vice presidential candidate");
	            			 choice=userInput.nextLine();
	            			 //This is just a safety check to ensure the chosen candidate is a valid one.
	            			 boolean contains = false;
	            			 for (String item : logic.getCandidates()[1].split(",")) {
	            			     if (choice.equalsIgnoreCase(item)) {
	            			         contains = true;
	            			         break; // No need to look further.
	            			     } 
	            			 }
	            			 if (contains){
	            				 System.out.println("You chose: " + choice.toLowerCase() + " are you sure? (Y/N)");
	            				 choice2 = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
	            				 if (choice2.equalsIgnoreCase("Y")){
	            					 procede = true;
	            				 }
	            				 else if(choice2.equalsIgnoreCase("N")){
	            					 procede = false;
	            				 }
	            				 else{
	            					 System.out.println("Please choose a valid candidate");
	            				 }
	            				 finalSelection[1]=choice.toLowerCase();
	            			 }
	            		 }
	            		 procede = false;
	            		 while (procede != true){
	            			 System.out.println("Here are the canditidates: ");
	            			 System.out.println(logic.getCandidates()[2]);
	            			 System.out.println("Please select your senate candidate");
	            			 choice=userInput.nextLine();
	            			 //This is just a safety check to ensure the chosen candidate is a valid one.
	            			 boolean contains = false;
	            			 for (String item : logic.getCandidates()[2].split(",")) {
	            			     if (choice.equalsIgnoreCase(item)) {
	            			         contains = true;
	            			         break; // No need to look further.
	            			     } 
	            			 }
	            			 if (contains){
	            				 System.out.println("You chose: " + choice.toLowerCase() + " are you sure? (Y/N)");
	            				 choice2 = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
	            				 if (choice2.equalsIgnoreCase("Y")){
	            					 procede = true;
	            				 }
	            				 else if(choice2.equalsIgnoreCase("N")){
	            					 procede = false;
	            				 }
	            				 else{
	            					 System.out.println("Please choose a valid candidate");
	            				 }
	            				 finalSelection[2]=choice.toLowerCase();
	            			 }
	            		 }
	            		 
	            		 logic.castVote(finalSelection);
	            	 }	
	            }
        	userInput.close();
    	}
	}
}