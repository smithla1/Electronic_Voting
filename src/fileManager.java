import java.io.FileWriter;
import java.io.FileInputStream;
import java.lang.StringBuffer;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class fileManager {
    private FileWriter registration_out_file;
    private FileReader registration_in_file;
    private FileWriter voter_out_file;
    private FileReader voter_in_file;
    private FileWriter results_out_file;
    private FileReader results_in_file;

    public fileManager() {
        //Do nothing
    }


   /**
    * This method will add a user to the file containing all registered
    * voters.
    *
    * @param encryptedRegistrationID    a byte array that contains the user's
    *                                   encrypted registration ID.
    * @param PID                        a string array containing a user's full
    *                                   name, their date of birth, and their
    *                                   social security number
    */ 
    protected void addRegisteredVoter( byte[] encryptedRegistrationID, String[] PID) {
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


   /**
    * This method will reference the file containing the encrypted
    * registration ID's of registered voters and determine whether or not
    * a given encrypted registration ID is in the file.
    *
    * The purpose of the method is to determine whether or not a given
    * user is registered to vote or not.
    *
    * @param encryptedRegistrationID    a byte array that contains the user's
    *                                   encrypted registration ID.
    * 
    * @return                           a boolean value where true means
    *                                   the user is registered and false
    *                                   means they are not.
    */ 
    protected boolean isRegistered( byte[] encryptedRegistrationID ) {
        try {
            registration_in_file = new FileReader("regLog.csv");
            BufferedReader br = new BufferedReader(registration_in_file);

            String line = br.readLine();
            while( line != null ) {
                if (encryptedRegistrationID.toString().equals(line.split(",")[3])){
                    return true;
                }
                line = br.readLine();
            }
            br.close();
            registration_in_file = null;

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


   /**
    * This method will add a user's encrypted registration ID to the
    * file containing the encrypted registration IDs of everyone
    * who has cast a vote.
    *
    * @param encryptedRegistrationID    a byte array that contains the user's
    *                                   encrypted registration ID. 
    */ 
    private void addVote( byte[] encryptedRegistrationID ) {

        try {
            voter_out_file = new FileWriter("voterLogs.csv", true);

            voter_out_file.write(encryptedRegistrationID.toString()+"\n");
            voter_out_file.close();
            voter_out_file = null;

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }        
    }


   /**
    * This method will determine if a user has cast a vote by checking
    * the file containing the encrypted registration IDs of everyone
    * who has cast a vote. If they given encrypted registration ID is in
    * the file, then the user has voted and if not, then the user has not
    * voted.
    *
    * @param encryptedRegistrationID    a byte array that contains the user's
    *                                   encrypted registration ID.
    *
    * @return                           a boolean value where true means the
    *                                   user has voted and where false means
    *                                   the user has not voted.
    */ 
    protected boolean hasVoted( byte[] encryptedRegistrationID ) {
        try {
            voter_in_file = new FileReader("voterLogs.csv");
            BufferedReader br = new BufferedReader(voter_in_file);

            String line = br.readLine();
            while( line != null ) {
                if (encryptedRegistrationID.toString().equals(line.split(",")[3])){
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
    * The candidate selection is expected to have this format:
    *   Index 0: Position,Candidate
    *   Index 1: Position,Candidate
    *   ...
    *   Index n: Position,Candidate
    * A vote for a write in candidate will merely have the write in candidate
    * as the candidate after the position. An abstained vote will be have the
    * candidate as a pound sign '#'.
    *
    * @param selection                  a string array containing the user's
    *                                   selected candidates
    * @param encryptedRegistrationID    a byte array that contains the user's
    *                                   encrypted registration ID.
    *
    * @see  IMPORTANT ASSUMPTION: We are assuming that the incoming selection
    *       will be in the same order as the positions are listed in the file
    *       containing the candidate information. In addition, it is assumed
    *       that every position will be present, even if the vote is abstained
    *       (see above for how to format an abstained vote).       
    */
    protected void castVote(String[] selection, byte[] encryptedRegistrationID) {
        // Add their registration ID to the file keeping track of who
        // all has voted.
        addVote(encryptedRegistrationID);

        // Get the current state of the election
        String[] results = parseResultsFile();

        // Now we need to look at the user's selection and rewrite the lines of
        // the candidate file to represent the user's voting choices.

        try {
            results_out_file = new FileWriter("candidates.csv");

            // Need to parse their selection and find what their vote was for each position
            for(int i=0; i<selection.length; i++) {
                // 'choice' will contain the position and user's choice
                String[] choice = selection[i].split(",");

                if (choice[1].equals("#")) { //user abstained
                    results_out_file.write(results[i]);
                    results_out_file.write("\n");
                } else { //user picked a candidate
                    // Iterate through the candidates to find the one the user
                    // picked

                    String[] positionResults = results[i].split(",");
                    for(int j=1;j<positionResults[i].length(); j=j+2) {
                        //if the last position is "WRITE-IN" and we have not found
                        // the user's candidate, then it's a write in we don't have
                        if (j == positionResults.length-1 && positionResults[j].equals("WRITE-IN")) {
                            StringBuffer newResult = new StringBuffer();
                            newResult.append(results[i]);
                            newResult.append(",");
                            newResult.append(choice[1]);
                            newResult.append(",");
                            newResult.append("1");
                            results_out_file.write(newResult.toString());
                            results_out_file.write("\n");
                            break;
                        } else if (positionResults[j].equals("WRITE-IN")) {
                            j--;
                        } else if (positionResults[j].equals(choice[1])){
                            String replacee = choice[1] + "," + "\\d+";
                            String replacer = choice[1] + ","
                                + (Integer.toString(((Integer.parseInt(positionResults[j+1]))+1)));
                            String newString = results[i].replaceFirst(replacee, replacer);

                            results_out_file.write(newString);
                            results_out_file.write("\n");
                            break;
                        }
                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();

        } finally {
            try {
                if (results_out_file != null) {
                    results_out_file.close();
                    results_out_file = null;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


   /**
    * This method will return the official candidates for each position.
    * It will read candidates.csv and look at each line, which has the
    * following format:
    *   Position,Candidate,VotesForCandidate,...,WRITE-IN,Candidate,VotesForCandidate,...
    * In this implementation, the official candidates are in the first
    * section, while the write in candidates are afterwards.
    *
    * @return   A string array containing the official candidates for each
    *           position.
    */
    protected String[] getCandidates() {
        String[] results = parseResultsFile();
        String[] candidates = new String[results.length];

        for(int i=0; i<results.length; i++) {
            String[] pieces = results[i].split(",");
            StringBuffer returnString = new StringBuffer();
            returnString.append(pieces[0]);
            for(int j=1; j<pieces.length; j=j+2) {
                if(pieces[j].equals("WRITE-IN")) {
                    break;
                }
                returnString.append(",");
                returnString.append(pieces[j]);
            }
            candidates[i] = returnString.toString();
        }

        return candidates;
    }

    
   /**
    * This method will return the results of the election.
    * The formate will be a string array where each string in the
    * array being a comma delineated list of the candidates for each
    * position. This will be the format of each string:
    *   Position,Candidate,VotesForCandidate,...,WRITE-IN,Candidate,VotesForCandidate,...
    * 
    * @return   A string array containing the current standings of the election
    */ 
    protected String[] getTalley() {
        return parseResultsFile();
    }


   /**
    * This method will parse the file containing all the candidates
    * and the votes for each respective candidate.
    *
    * @return   A string array with each position containing a line
    *           in the the file.
    */
    private String[] parseResultsFile() {
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

        System.out.println("These are the candidates:");
        String[] theCandidates = myManager.getCandidates();
        for (int i = 0; i<theCandidates.length; i++) {
            System.out.println(theCandidates[i]);
        }

        String[] myVote = {"President,Ted Cruz", "Vice President,Danny Boy", "Senator,#"};
        myManager.castVote(myVote, testID);

        String[] myOtherVote = {"President,JESUS", "Vice President,#", "Senator,#"};
        myManager.castVote(myOtherVote, testID);

        System.out.println("\nAn administrator would like to see how the election is going. Here is the tally:");
        String[] results = myManager.getTalley();

        for (String temp : results) {
            System.out.println(temp);
        }
    }
}
