import java.awt.*;
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
    private JPanel panel1, panel2, panel3; 
    

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
        int numPairs = labels.length;
        //Create and populate the panel.
        for(int i=1; i<numPairs+1; i++){
            JLabel tempLabel = new JLabel(labels[i-1]);
            gbc.gridx = 0;
            gbc.gridy = i;
            panel2.add(tempLabel, gbc);
            
            switch (i-1){
            	case 0: nameField = new JTextField("First Middle Last", 20);
            			tempLabel.setLabelFor(nameField);
            			gbc.gridx = 1;
                        gbc.gridy = i;
            			panel2.add(nameField, gbc);
            	case 1: dobField = new JTextField("MM/DD/YYYY", 20);
            			tempLabel.setLabelFor(dobField);
            			gbc.gridx = 1;
                        gbc.gridy = i;
            			panel2.add(dobField, gbc);
            	case 2: ssnField = new JTextField("AAA-GG-SSSS", 20);
            			tempLabel.setLabelFor(ssnField);
            			gbc.gridx = 1;
                        gbc.gridy = i;
            			panel2.add(ssnField, gbc);
            }
        }
        nameField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	nameField.setText("");
            }
        });
        dobField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
            	dobField.setText("");
            }
        });
        ssnField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
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

        mvToRegisterBtn = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(mvToRegisterBtn, gbc);
        mvToRegisterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //cl.show(cards, "E-Voting");
            	cl.show(cards, "Registration");
            }});
        confirmRegisterBtn = new JButton("Confirm");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel2.add(confirmRegisterBtn, gbc);
        confirmRegisterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //cl.show(cards, "E-Voting");
            	cl.show(cards, "E-Voting");
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
        gbc.gridy = 2;
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.show(cards, "E-Voting");
            }
        });
        panel3.add(cancelBtn, gbc);
        
        
        cancelBtn1 = new JButton("Cancel");
        gbc.gridx = 0;
        gbc.gridy = 4;
        cancelBtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.show(cards, "E-Voting");
            }
        });
        panel2.add(cancelBtn1, gbc);
        

        add(cards);

        cl.show(cards, "E-Voting");
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
