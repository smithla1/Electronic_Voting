import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces

public class UserInterface extends Frame implements ActionListener {
    private Label Voting;
    private TextField textbox;
    private Button register;
    private Button vote;
    
    public UserInterface() {
        setLayout(new FlowLayout());
        
        Voting = new Label("Choose");
        add(Voting);
        
        textbox = new TextField("", 15);
        textbox.setEditable(false);       
        add(textbox);
        
        register = new Button("Register to Vote");
        add(register);
        
        vote = new Button("Cast Your Vote");
        add(vote);
        
        register.addActionListener(this);
        vote.addActionListener(this);
        
        
        setTitle("Electronic Voting System");
        setSize(500,500);
        
        setVisible(true);
        
    }
    
    public static void main(String[] args) {
        UserInterface app = new UserInterface();
    }
    
    // ActionEvent handler - Called back upon button-click.
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == register) {
            textbox.setText("REGISTERING");
        } else {
            textbox.setText("VOTING");
        }
        //++count; // increase the counter value
        // Display the counter value on the TextField tfCount
        //tfCount.setText(count + ""); // convert int to String
    }
}
