import java.security.SecureRandom;
import java.lang.StringBuffer;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

public class registrationID {
    // Plain text registration ID
    private String unsecureRegistrationID;
    // Encrypted registration ID
    private byte[] secureRegistrationID;
    
    
    public registrationID ( String[] PID ) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        unsecureRegistrationID = createRegistrationID( PID );
        secureRegistrationID = createSecureRegistrationID(unsecureRegistrationID);
    }

    
    private String createRegistrationID( String[] PID) {
        // This will work under the assumption that PID has the following contents:
        //  Index 0: Full name in the format "FIRST MIDDLE LAST"
        //  Index 1: DOB in the format "MM/DD/YYYY"
        //  Index 2: SSN in the format "#########"
        //      The first three numbers are the area number
        //      The next two numbers are the group number
        //      The remaining four numbers are serial numbers
        //
        // This method will create a registration ID that can be used to
        // log into the polls. This registration ID will have the format:
        //  ####-####-####-####
        // The first 10 numbers will be randomly generated. The 11th and
        // 12th numbers shall be the last two digits of the year of birth.
        // The remaining 4 numbers will be the last four digits of a the
        // given social security number.

        char[] allowedChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        SecureRandom random = new SecureRandom();
        StringBuffer password = new StringBuffer();

        for(int i = 0; i < 10; i++) {
            password.append(allowedChars[ random.nextInt(allowedChars.length) ]);
        }

        String year_of_birth = PID[1].split("/")[2].substring(2,4);
        String serial_number = PID[2].substring(5, 9);

        password.append(year_of_birth);
        password.append(serial_number);

        return password.toString();
    }

    
    private byte[] createSecureRegistrationID( String registrationID )
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String password = "THISISTHEMASTERPASSWORD";

        PasswordEncryptionService secure = new PasswordEncryptionService();

        byte[] securePassword = secure.getEncryptedPassword(registrationID,
            password.getBytes());

        return securePassword;
    }

    
    public String getRegistrationID() {
        return unsecureRegistrationID;
    }


    public byte[] getSecureRegistrationID() {
        return secureRegistrationID;
    }

    
    public static void main(String[] args) throws NoSuchAlgorithmException, 
            InvalidKeySpecException {

        String[] PID = {"Logan Allen Smith", "06/20/1995", "123456789"};
        registrationID regKey = new registrationID(PID);
        System.out.println(regKey.getRegistrationID());
        System.out.println(regKey.getSecureRegistrationID());
    }
}
