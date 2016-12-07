import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.Border;


public class GUI extends JFrame{
	//Default generated VersionUID
	private static final long serialVersionUID = 1L;
	
	private final CardLayout cl = new CardLayout();
    private final JPanel cards = new JPanel(cl);
    private final Border border = BorderFactory.createEmptyBorder(60, 60, 60, 60);
    private JTextField nameField=null,dobField=null,ssnField=null;
    private JButton mvToRegisterBtn,confirmRegisterBtn,mvToVoteBtn,cancelBtn,cancelBtn1,cancelBtn2;
    private JPanel panel1, panel2, panel3, panel4, panel5;
    private JFrame frame = new JFrame();
    private SystemLogic logic = new SystemLogic();
    private String tempRegistrationID;
    

    public GUI() {

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7,7,7,7);
        setUIFont (new javax.swing.plaf.FontUIResource("Helvetica",Font.PLAIN,24));
        
        
        
        //setContentPane(contentPane);

        panel1 = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.setBorder(border);
        panel1.setBackground(Color.CYAN);
        JLabel welcome = new JLabel("Welcome to our E-Voting System!");
        welcome.setHorizontalAlignment(AbstractButton.CENTER);
        panel1.add(welcome, gbc);
        
        cards.add(panel1, "E-Voting");
        gbc.gridwidth = 1;

        
        
        panel2 = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Registration");
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel2.add(label, gbc);
        panel2.setBorder(border);
        panel2.setBackground(Color.CYAN);
        
        //Beginning of the actual registration code
        String[] labels = {"Name: ", "DOB: ", "SSN: "};
        String defaultName = "First Middle Last";
        String defaultDOB = "MM/DD/YYYY";
        String defaultSSN = "AAA-GG-SSSS";
        int numPairs = labels.length;
        //Create and populate the panel.
        for(int i=1; i<numPairs+1; i++){
            JLabel tempLabel = new JLabel(labels[i-1]);
            gbc.gridx = 0;
            gbc.gridy = i;
            tempLabel.setHorizontalAlignment(AbstractButton.CENTER);
            panel2.add(tempLabel, gbc);
            
            switch (i-1){
            	case 0: nameField = new JTextField(defaultName, 20);
            			tempLabel.setLabelFor(nameField);
            			gbc.gridx = 1;
                        gbc.gridy = i;
            			panel2.add(nameField, gbc);
            	case 1: dobField = new JTextField(defaultDOB, 20);
            			tempLabel.setLabelFor(dobField);
            			gbc.gridx = 1;
                        gbc.gridy = i;
            			panel2.add(dobField, gbc);
            	case 2: ssnField = new JTextField(defaultSSN, 20);
            			tempLabel.setLabelFor(ssnField);
            			gbc.gridx = 1;
                        gbc.gridy = i;
            			panel2.add(ssnField, gbc);
            }
        }
        
        //Code to clear hint values when the textField is clicked
        nameField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(nameField.getText().equals(defaultName))
            		nameField.setText("");
            }
        });
        dobField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(dobField.getText().equals(defaultDOB))
            		dobField.setText("");
            }
        });
        ssnField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	if(ssnField.getText().equals(defaultSSN))
            		ssnField.setText("");
            }
        });
        cards.add(panel2, "Registration");

        panel3 = new JPanel(new GridBagLayout());
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel3.setBorder(border);
        panel3.setBackground(Color.CYAN);
        panel3.add(new JLabel("Voting"), gbc);
        
        //Code to handle registration ID input
        JTextField registrationID = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel3.add(registrationID, gbc);
        JLabel tempLabel = new JLabel("Registration ID:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel3.add(tempLabel, gbc);
        cards.add(panel3, "Voting");
        
        panel4 = new JPanel(new GridBagLayout());
        panel4.setBorder(border);
        panel4.setBackground(Color.CYAN);
        String[] positionStrings = logic.getCandidates();
        int currentPositionY=0, currentCandidatePositionY=1, j=1;
        ButtonGroup[] buttons = new ButtonGroup[positionStrings.length];
        for(int i=0; i<positionStrings.length; i++){
        	String[] pieces = positionStrings[i].split(",");
        	gbc.gridx = 0;
        	gbc.gridy = currentPositionY;
        	currentPositionY+=2;
            JLabel tempLabel1 = new JLabel(pieces[0]);
            tempLabel1.setPreferredSize(new Dimension(250, 30));
            ButtonGroup tempButtonGroup = new ButtonGroup();
            panel4.add(tempLabel1, gbc);
            
            for(j=1; j<pieces.length; j++){
            	gbc.gridx = j-1;
                gbc.gridy = currentCandidatePositionY;
                JLabel tempLabel2 = new JLabel(pieces[j]);
                JRadioButton tempRadioButton = new JRadioButton();
                tempRadioButton.setPreferredSize(new Dimension(275, 30));
                tempRadioButton.setHorizontalAlignment(AbstractButton.CENTER);
                tempRadioButton.setBackground(Color.CYAN);
                tempRadioButton.setText(tempLabel2.getText());
                tempButtonGroup.add(tempRadioButton);
                panel4.add(tempRadioButton, gbc);
            	
            }
            
            JLabel tempLabel2 = new JLabel("Abstain");
            tempLabel2.setPreferredSize(new Dimension(275, 30));
            gbc.gridx = j;
            gbc.gridy = currentCandidatePositionY-1;
            tempLabel2.setHorizontalAlignment(AbstractButton.CENTER);
            //panel4.add(tempLabel2, gbc);
            JRadioButton tempRadioButton = new JRadioButton();
            tempRadioButton.setPreferredSize(new Dimension(275, 30));
            tempRadioButton.setHorizontalAlignment(AbstractButton.CENTER);
            tempRadioButton.setBackground(Color.CYAN);
            tempRadioButton.setText(tempLabel2.getText());
            gbc.gridy = currentCandidatePositionY;
            tempButtonGroup.add(tempRadioButton);
            buttons[i] = tempButtonGroup;
            panel4.add(tempRadioButton, gbc);
            currentCandidatePositionY+=2;
            
            
        }
        cards.add(panel4, "Voting1");
        
        panel5 = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel5.setBorder(border);
        panel5.setBackground(Color.CYAN);
        panel5.add(new JLabel("Admin Panel"), gbc);
        JButton shutdown = new JButton("Shutdown");
        shutdown.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		System.exit(0);
        	}
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel5.add(shutdown, gbc);
        JButton getTally = new JButton("Get Tally");
        getTally.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		String[] results;
        		results = logic.getTalley();
        		JOptionPane.showMessageDialog(frame, results);
        	}
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel5.add(getTally, gbc);
        cards.add(panel5, "Admin Panel");
        JButton cancel3 = new JButton("Cancel");
        cancel3.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		registrationID.setText("");
        		cl.show(cards, "E-Voting");
        	}
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel5.add(cancel3, gbc);
        
        cards.add(panel5, "Admin Panel");
        mvToRegisterBtn = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(mvToRegisterBtn, gbc);
        mvToRegisterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	cl.show(cards, "Registration");
            }});
        
        
        confirmRegisterBtn = new JButton("Confirm");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel2.add(confirmRegisterBtn, gbc);
        confirmRegisterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//Takes in inputs 
            	String name, dob, ssn,nameUnsafe, dobUnsafe, ssnUnsafe;
            	
            	nameUnsafe = nameField.getText();
            	dobUnsafe = dobField.getText();
            	ssnUnsafe = ssnField.getText();
            	name = alphaStrip(nameField.getText());
            	dob = dateStrip(dobField.getText());
            	ssn = dateStrip(ssnField.getText());
            	//Check to see if inputs are not empty
            	if(!nameUnsafe.equals("") && !dobUnsafe.equals("") && !ssnUnsafe.equals("")){
            		//Check to see if inputs are not their default values
            		if(!(nameUnsafe.equals(defaultName) || dobUnsafe.equals(defaultDOB) || ssnUnsafe.equals(defaultSSN))){
            			//check to make sure the input DOB is the correct length
            			if(dateStrip(dob).length() == 10){
            				//check to make sure the input SSN is the correct length
            				if(dateStrip(ssn).length() == 9){
            					
                    			
                    			String [] PID = {name, dob, ssn};
                        		try {
                        			registrationID currentUserID = new registrationID(PID);
                        			if (!logic.userIsRegistered(PID)){
                        				logic.registerUser(PID, currentUserID.getRegistrationID());
                        				
                        				//Code to handle copying registrationID to system clipboard (Only after successful registration)
                        				tempRegistrationID = currentUserID.getRegistrationID();
                        				StringSelection stringSelection = new StringSelection(tempRegistrationID);
                        				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                        				clpbrd.setContents(stringSelection, null);
                        				
                        				//Informing the user on their successful registration.
                        				JOptionPane.showMessageDialog(frame, "Your have been successfully registered!" + "\n" 
                        				+ "Here is your registration ID please do not lose it (It has been copied to clipboard for your convenience)" + "\n" + 
                        				currentUserID.getRegistrationID());
                        				
                        				//Registration is finished, time to reset the values of the textFields (To maintain privacy and reset for next voter)...
                        				nameField.setText(defaultName);
                        				dobField.setText(defaultDOB);
                        				ssnField.setText(defaultSSN);
                        				cl.show(cards, "E-Voting");
                        				
                        			}
                        			else{JOptionPane.showMessageDialog(frame, "You are already registered!");}
                        		} catch (Exception exception) {
                        			exception.printStackTrace();
                        			JOptionPane.showMessageDialog(frame, "There was an error registering you.");
                        		}
            				}
            				else{JOptionPane.showMessageDialog(frame, "Please enter your SSN in the correct format: " + defaultSSN);}
            			}
            			else{JOptionPane.showMessageDialog(frame, "Please enter the date in the correct format: " + defaultDOB);}
            		}
            		else{JOptionPane.showMessageDialog(frame, "Please override the default vaules.");}
            	}
            	else{JOptionPane.showMessageDialog(frame, "Please do not leave any fields blank");}
            }});
        
        
        mvToVoteBtn = new JButton("Start Voting");
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel1.add(mvToVoteBtn, gbc);
        mvToVoteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.show(cards, "Voting");
            }});
        
        JButton confirmRegistrationIDBtn = new JButton("Confirm");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel3.add(confirmRegistrationIDBtn, gbc);
        confirmRegistrationIDBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String inputRegID = dateStrip(registrationID.getText());
            	if(!(inputRegID.equals(""))){
            		if(inputRegID.length() == 6){
            			if(inputRegID.equals("111111")){
            				//User is an admin
            				cl.show(cards, "Admin Panel");
            			}
            		}
            		else if(inputRegID.length() == 16){
            			try {
							if(logic.userIsRegistered(inputRegID)){
								if(!(logic.userHasVoted(inputRegID))){
									//registrationID.setText("");
									cl.show(cards, "Voting1");
								}
								else{JOptionPane.showMessageDialog(frame, "You have already voted!");}
							}
							else{
								JOptionPane.showMessageDialog(frame, "Registration ID is incorrect!");
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(frame, "There was an error checking if you are registered");
						}
            		}
            		else{JOptionPane.showMessageDialog(frame, "Please input your 16 digit registration ID in the correct format");}
            	}
            	else{JOptionPane.showMessageDialog(frame, "Please do not leave the registration ID blank");}
                
            }});
        
        JButton confirmVoteBtn = new JButton("Confirm");
        gbc.gridx = j;
        gbc.gridy = currentCandidatePositionY+3;
        panel4.add(confirmVoteBtn, gbc);
        confirmVoteBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String[] pieces;
        		String[] myVote = new String[positionStrings.length];
        		for(int i=0; i<positionStrings.length; i++){
        			pieces = positionStrings[i].split(",");
        			for(int j=0; j<buttons.length; j++){
        				//System.out.println(buttons[j].getSelection());
            			if(buttons[j].getSelection() == null){
            				myVote[i]=pieces[0] + "," + "#";
            			}
            			else if(buttons[j].getSelection() != null){
            				myVote[i]=pieces[0] + "," + pieces[j];
            			}
            		}
        			
        		}
        		//for(int k=0; k<myVote.length; k++)
        			//System.out.println(myVote[k]);
        		
        		//clears selection
        		logic.castVote(myVote, registrationID.getText());
        		JOptionPane.showMessageDialog(frame, "Thank you for voting!");
        		for(int k=0; k<buttons.length; k++){
        			buttons[k].clearSelection();
        		}
        		registrationID.setText("");
        		cl.show(cards, "E-Voting");
        	}
        });
        
        
        cancelBtn = new JButton("Cancel");
        gbc.gridx = 0;
        gbc.gridy = 4;
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	nameField.setText(defaultName);
				dobField.setText(defaultDOB);
				ssnField.setText(defaultSSN);
                cl.show(cards, "E-Voting");
            }
        });
        panel2.add(cancelBtn, gbc);
        
        
        cancelBtn1 = new JButton("Cancel");
        gbc.gridx = 0;
        gbc.gridy = 2;
        cancelBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	registrationID.setText("");
                cl.show(cards, "E-Voting");
            }
        });
        panel3.add(cancelBtn1, gbc);
        
        cancelBtn2 = new JButton("Cancel");
        gbc.gridx = 0;
        gbc.gridy = currentCandidatePositionY+3;
        cancelBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	for(int i=0; i<buttons.length; i++){
            		buttons[i].clearSelection();
            	}
            	registrationID.setText("");
                cl.show(cards, "E-Voting");
            }
        });
        panel4.add(cancelBtn2, gbc);
        

        add(cards);

        cl.show(cards, "E-Voting");
    }
    
    private String alphaStrip(String stringToStip){
    	return stringToStip.replaceAll("[^A-Za-z]", "");
    }
    private String dateStrip(String stringToStip){
    	return stringToStip.replaceAll("[^0-9/]", "");
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
          Object key = keys.nextElement();
          Object value = UIManager.get (key);
          if (value != null && value instanceof javax.swing.plaf.FontUIResource)
            UIManager.put (key, f);
          }
        } 
    
	public static void main(String[] args) {
		//Thread Safety
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                GUI frame = new GUI();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
