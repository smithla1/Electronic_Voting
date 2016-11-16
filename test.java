import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class test {
	
	public static final double FIRSTVARIABLE = 13;
	public static final double SECONDVARIABLE = 450;
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		/*
		int size = 5;
		
		System.out.println( (size == 5) ? true : false);
		System.out.println( (size == 4) ? true : false);
		
		System.out.println(5 == 4);
		System.out.println(5 == 3);
		
		Integer test = 1;
		Integer testing = 2;
		Integer testIt = 3;
		System.out.printf("test = %d, testing = %d, testIt = %d\n", test, testing, testIt);
		
		testing = test;
		test = testIt;
		
		System.out.printf("test = %d, testing = %d, testIt = %d\n", test, testing, testIt);
		*/
		
		
		
		/*
		FileOutputStream output = new FileOutputStream(new File("thisisatest.txt"));
		
		byte r = 0b1;
		byte r1 = 0b10;
		byte r2 = 0b11;
		byte r3 = (byte)0b10000000;
		byte r4 = 0b01000000;
		
		output.write(r);
		output.write(r1);
		output.write(r2);
		//output.write(r3);
		output.write(r4);
		*/
		/*
		byte b = 0b1;
		byte mask = (byte)0b10000000;
		
		byte b2 = (byte) (b | mask);
		System.out.println(b2);
		
		
		System.out.println("If a number has 22 digits and we want to elimnate the last 2");
		String test = "0123456789012345678901";
		String tested = test.substring(0, 5);
		System.out.printf("Test length = %d\nModded by five = %d\n", test.length(),test.length()%5);
		System.out.println(test.substring(0, test.length() - test.length() % 5));
		
		
		int capacity = 400;
		for(int i=0; i<5; i++){
			System.out.printf("Capacity = %d\nCapacity/2 = %d\n", capacity, capacity / 2);
			capacity /= 2;
		*/
		
		Integer test = 5;
		Integer test2 = 10;
		System.out.println(test.compareTo(test2));
		Integer test3 = null;
		
		String testString = "  4  5   6 7     10";
		String pattern = "\\s+";
		String[] tAr = testString.split(pattern);
		for (int i=0; i<tAr.length; i++)
			System.out.println(tAr[i]);
		
		
		java.util.PriorityQueue<Integer> heap = new java.util.PriorityQueue<Integer>(10);
		
		System.out.println("Int overflow testing");
		int n = (int) 2020020020 * 2;
		System.out.printf("Expected: %f\nActual: %d", 2020020020*2.0, (int) n);
		
		String hashTest = "a";
		System.out.println(hashTest);
		System.out.println((int)hashTest.charAt(0));
		
		System.out.println("---------------------------------------------------");
		
		String userRegistrationID = "Logan123456789";
		System.out.println(userRegistrationID);
		int hashCode = userRegistrationID.hashCode();
		System.out.println(hashCode);
		
		
		EncryptionService sec = new EncryptionService();
		byte[] salt = sec.generateSalt();
		
		byte[] secPass = sec.getEncryptedPassword(userRegistrationID, salt);
		
		System.out.println(sec.authenticate("Logan123456789", secPass, salt));
		System.out.println("The salt is: ");
		System.out.print(salt);
		
		System.out.println("\nThe user hash is: ");
		System.out.print(secPass);
		
		
		for(int i = 0; i<5; i++) {
		    System.out.println("The salt is: " + sec.generateSalt());
		}
	    
		System.out.println(sec.generateSalt());
	    System.out.println(sec.generateSalt());
		
	}
	

	private static String string(int arrival) {
		// TODO Auto-generated method stub
		return null;
	}
}
