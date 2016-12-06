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
    private JButton mvToRegisterBtn,confirmRegisterBtn,mvToVoteBtn,cancelBtn,cancelBtn1;
    private JPanel panel1, panel2, panel3, panel4; 
    

    public GUI() {

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        //setContentPane(contentPane);

        panel1 = new JPanel(new GridBagLayout());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.setBorder(border);
        panel1.setBackground(Color.CYAN);
        panel1.add(new JLabel("Welcome"), gbc);
        cards.add(panel1, "E-Voting");

        
        
        panel2 = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Registration");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel2.add(label);
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel3.setBorder(border);
        panel3.setBackground(Color.CYAN);
        panel3.add(new JLabel("Voting"), gbc);
        cards.add(panel3, "Voting");
        
        panel4 = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel3.setBorder(border);
        panel3.setBackground(Color.CYAN);
        panel3.add(new JLabel("Voting"), gbc);
        cards.add(panel4, "Voting1");

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
            	JFrame frame = new JFrame();
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
            					SystemLogic logic = new SystemLogic();
                    			
                    			String [] PID = {name, dob, ssn};
                        		try {
                        			registrationID currentUserID = new registrationID(PID);
                        			if (!logic.userIsRegistered(PID)){
                        				logic.registerUser(PID, currentUserID.getRegistrationID());
                        				
                        				//Code to handle copying registrationID to system clipboard (Only after successful registration)
                        				String tempRegistrationID = currentUserID.getRegistrationID();
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
            	else{JOptionPane.showMessageDialog(frame, "please do not leave any fields blank");}
            }});
        
        
        mvToVoteBtn = new JButton("Start Voting");
        gbc.gridx = 2;
        gbc.gridy = 1;
        panel1.add(mvToVoteBtn, gbc);
        mvToVoteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.show(cards, "Voting");
            }});
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
        
        
        
        cancelBtn1 = new JButton("Cancel");
        panel2.add(cancelBtn, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        cancelBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.show(cards, "E-Voting");
            }
        });
        panel3.add(cancelBtn1, gbc);
        

        add(cards);

        cl.show(cards, "E-Voting");
    }
    
    private String alphaStrip(String stringToStip){
    	return stringToStip.replaceAll("[^A-Za-z]", "");
    }
    private String dateStrip(String stringToStip){
    	return stringToStip.replaceAll("[^0-9/]", "");
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
