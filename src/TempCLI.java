import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

public class TempCLI {

    private static boolean running;
    private static boolean locked;
    private static Scanner userInput;

    public TempCLI() {
        running = true;
        locked = true;
        userInput = new Scanner(System.in);
    }

    public boolean isRunning() {
        return running;
    }

    private void displayCandidates( String[] selection) {
        for( String choice : selection ) {
            System.out.println(choice);
            String[] parts = choice.split(",");

            if (parts[1].equals("#")) {
                System.out.println("For " + parts[0] + " you have abstained from voting.");
            } else {
                System.out.println("For " + parts[0] + " you have chosen: " + parts[1]);
            }
        }
    }

    private void displayAdminOptions(SystemLogic logic) {
        System.out.println("Please choose an option:\nElection Status\nShutdown\nExit\n");
        String temp = userInput.nextLine();

        while (true) {
            if (!(temp.equalsIgnoreCase("ELECTION STATUS")) &&
                    !(temp.equalsIgnoreCase("SHUTDOWN")) &&
                    !(temp.equalsIgnoreCase("EXIT"))) {

                System.out.println("Unrecognized input. Please try again.");
                temp = userInput.nextLine();                
            } else {
                if (temp.equalsIgnoreCase("ELECTION STATUS")) {
                    String[] results = logic.getTalley();
                    for (String position : results) {
                        System.out.println(position);
                    }
                    System.out.println("\nPlease enter another command.");
                    temp = userInput.nextLine();
                } else if (temp.equalsIgnoreCase("SHUTDOWN")) {
                    System.out.println("\nShutting down the system.");
                    running = false;
                    return;
                } else {
                    return;
                }
            }
        }    

    }

	private void run() throws Exception {

        String password;
        while (locked) {
            System.out.println("ADMIN, please enter your PIN to unlock the system.");
            password = userInput.nextLine();
            if (password.equalsIgnoreCase("1111")) {
                locked = false;
            } else {
                System.out.println("INCORRECT");
            }
        }

    	String name,DOB,SSN,regID;

    	SystemLogic logic = new SystemLogic();

        System.out.println("\nHello! Welcome to the Electronic Voting System!");
    	System.out.println("Are you registered? (Y/N)");
    	//Here I am grabbing the user input and assigning it to temp and stripping all non alpha characters
        String temp = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
        while(!temp.equalsIgnoreCase("Q")){

        	//Segment of CLI to handle registration.
        	if(temp.equalsIgnoreCase("N")){

        		System.out.println("OK, beginning registration process.");

        		System.out.println("Please enter your full name in FIRST MIDDLE LAST");
        		//Need to make this safe before final
        		name = userInput.nextLine();

        		System.out.println("Please enter your date of birth in the format of: MM/DD/YYYY");
        		DOB = userInput.nextLine();

                while ( ! (Pattern.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d", DOB))) {
                    System.out.println("That was not formatted correctly. Please try again.");
                    DOB = userInput.nextLine();
                }
        		//String[] DOBWithoutDelims = DOB.split("/-");
        		//DOB = Arrays.toString(DOBWithoutDelims);

        		System.out.println("Please enter your Social Security Number");
        		SSN = userInput.nextLine();

                while (! (Pattern.matches("\\d{9}", SSN))) {
                    System.out.println("That was not formatted correctly. Please try again.");
                    SSN = userInput.nextLine();
                }
        		String [] PID = {name, DOB, SSN};
        		
        		try {

        			registrationID currentUserID = new registrationID(PID);

                    //System.out.println(currentUserID.getRegistrationID());

        			if (!logic.userIsRegistered(PID)){
        				logic.registerUser(PID, currentUserID.getRegistrationID());
        				System.out.println("You have been registered successfully here is your registration number: " + currentUserID.getRegistrationID());
                        System.out.println("Make sure to hold on to that number, as you will need it to vote!");
                        System.out.println("Have a nice day!");
                        return;
        			}
        			else{
        				System.out.println("You are already registered!");
                        System.out.println("Have a nice day!");
                        return;
        			}
        		} catch (Exception e) {
                    e.printStackTrace();
        			System.out.println("There was an error creating registration ID");
        			break;
        		} finally {
                    try {
                        if (userInput != null) {
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

                 //See if they are an administrator
                 if (regID.substring(12,16).equalsIgnoreCase("0000")) {
                    //User entered admin registration ID. Prompt for additional identification.
                    System.out.println("Please enter your password to access admin options: "); 
                    //password is 1111 for now

                    int count = 0;
                    while (count < 3) {
                        temp = userInput.nextLine();
                        if (temp.equalsIgnoreCase("1111")) {
                            displayAdminOptions(logic);
                            break;
                        } else {
                            System.out.println("Incorrect Password. Please try again:");
                            count++;
                        }
                    }
                    if (count == 3) {
                        System.out.println("Unauthorized access.");
                        return;
                    } else {
                        return;
                    }
                 }
            	 //try{
            	 if(logic.userIsRegistered(regID)){

                        if (logic.userHasVoted(regID)) {
                            System.out.println("\nAccording to our records, you have already voted.\nPlease note that attempting to vote twice is a type of voter fraud, which is ILLEGAL.\nHave a good day!\n");
                            return;
                        }

                        boolean confirmedSelection = false;
                        while (!confirmedSelection) {

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
                                        System.out.print(" | ");
                                    }
                                }

                                System.out.print("\nChoose your candidate: ");
                                selection.add(pieces[0] + "," +userInput.nextLine());

                            }
                    		
                            String[] finalSelection = new String[selection.size()];
                            finalSelection = selection.toArray(finalSelection);

                            System.out.println("You have finished selecting your candidates!"
                                                + " These are the candidates that you have selected!\n");
                            displayCandidates(finalSelection);

                            System.out.println("\nWould you like to change your chosen candidates? (Y/N)");

                            temp = userInput.nextLine();

                            while (!(temp.equalsIgnoreCase("Y")) && !(temp.equalsIgnoreCase("N"))) {
                                System.out.println("That input wasn't recognized. Please try again.");
                                userInput.nextLine();
                            }

                            if (temp.equalsIgnoreCase("Y")) {
                                continue;
                            } else {
                                logic.castVote(finalSelection, regID);
                                System.out.println("\nYour selections have been recorded!\nThank you for voting!");
                                return;
                            }
                        }
            	 } else {
                    System.out.println("I'm sorry, but you are not registered. Please register before trying to vote.");
                    return;
                 }	
            }

            else {
                System.out.println("That input wasn't recognized. Try again.");
                temp = userInput.nextLine().replaceAll("[^a-zA-Z]+","");
            }
    	}
	}

    public static void main(String[] args) throws Exception {

        TempCLI app = new TempCLI();

        while(app.isRunning()) {
            app.run();
        }
        userInput.close();
        System.out.println("Goodbye");

    }
}