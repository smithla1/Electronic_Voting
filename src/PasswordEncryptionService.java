/**
 * PLEASE NOTE: CREDIT FOR THIS CODE GOES TOWARDS JERRY ORR, WHO WROTE
 *              THE ORIGIONAL VERSION OF THIS CODE. FOR THE PURPOSE OF
 *              OUR PROJECT, WE MODIFIED THE NAMES OF SOME METHOD AND
 *              PARAMETERS TO BETTER FIT OUR PROJECT.
 *              
 * 
 * The website where Jerry Orr broached the subject of security
 * practices and proposed the original version of this class can
 * be found here:
 *  http://blog.jerryorr.com/2012/05/secure-password-storage-lots-of-donts.html
 * 
 */


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordEncryptionService {

 public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
   throws NoSuchAlgorithmException, InvalidKeySpecException {
  // Encrypt the clear-text password using the same salt that was used to
  // encrypt the original password
  byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

  // Authentication succeeds if encrypted password that the user entered
  // is equal to the stored hash
  return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
 }

 public byte[] getEncryptedPassword(String password, byte[] salt)
   throws NoSuchAlgorithmException, InvalidKeySpecException {
  // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
  // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
  String algorithm = "PBKDF2WithHmacSHA1";
  // SHA-1 generates 160 bit hashes, so that's what makes sense here
  int derivedKeyLength = 160;
  // Pick an iteration count that works for you. The NIST recommends at
  // least 1,000 iterations:
  // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
  // iOS 4.x reportedly uses 10,000:
  // http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
  int iterations = 20000;

  KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

  SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

  return f.generateSecret(spec).getEncoded();
 }

 public byte[] generateSalt() throws NoSuchAlgorithmException {
  // VERY important to use SecureRandom instead of just Random
  SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

  // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
  byte[] salt = new byte[8];
  random.nextBytes(salt);

  return salt;
 }
 
 public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
	 PasswordEncryptionService test = new PasswordEncryptionService();
	 
	 byte[] salt = test.generateSalt();
	 
	 System.out.println(salt);
	 
	 String myPassword = "passwordpasswordpasswordpassword1";
	 byte[] securePassword = test.getEncryptedPassword(myPassword, salt);
   System.out.println(securePassword.length);
	 
	 System.out.println(securePassword);
	 
	 boolean isMe = test.authenticate("password1", securePassword, salt);
	 
	 System.out.println(isMe);
 }
}