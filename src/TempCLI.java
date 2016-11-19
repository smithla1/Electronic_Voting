
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class TempCLI {

	public static void main(String[] args) throws Exception {

    	String name,DOB,SSN,regID,choice,choice2;

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
        		} catch (Exception e) {
                    e.printStackTrace();
        			System.out.println("There was an error creating registration ID");
        			break;
        		} finally {
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
            	 //takes user input and parses out alpha characters.
            	 regID = userInput.nextLine().replaceAll("[a-zA-Z]+","");
            	 //try{
            	 if(logic.userIsRegistered(regID)){
            		
                    if (logic.userHasVoted(regID)) {
                        System.out.println("\nAccording to our records, you have already voted.\nPlease note that attempting to vote twice is a type of voter fraud, which is ILLEGAL.\nHave a good day!\n");
                        userInput.close();
                        System.exit(0);
                    }
                    String[] candidates = logic.getCandidates();
                    ArrayList<String> selection = new ArrayList<String>();

                    System.out.println("For this part we will show you the position and then the official candidates for that position.\nYou may choose an official candidate or a write in candidate. You may abstain by typing #.\nPlease type your selection carefully.");

                    for (String position : candidates) {
                        String[] pieces = position.split(",");
                        System.out.println("\nPosition: " + pieces[0]);
                        System.out.print("Candidates: ");
                        for(int i=1; i<pieces.length; i++) {
                            if (pieces[i].equals("WRITE-IN")) {
                                break;
                            }
                            System.out.print(pieces[i]);
                            if (! (i== pieces.length-1)) {
                                System.out.print(" || ");
                            }
                        }

                        System.out.print("\nChoose your candidate: ");
                        selection.add(pieces[0] + "," +userInput.nextLine());

                    }
            		
                    String[] finalSelection = new String[selection.size()];
                    finalSelection = selection.toArray(finalSelection);
            		logic.castVote(finalSelection, regID);
                    userInput.close();
                    System.out.println("\nYour selections have been recorded!\nThank you for voting!");
                    System.exit(0);
            	 } else {
                    System.out.println("I'm sorry, but you are not registered. Please register before trying to vote.");
                    userInput.close();
                    System.exit(0);
                 }	
            }

            else {
                System.out.println("That input wasn't recognized. Try again.");
                temp = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
            }
    	}
	}
}