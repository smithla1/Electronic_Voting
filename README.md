To run this project: run the main class of GUI.java

To unlock the system when starting it, an administrator is required to
input their password to do so. For this deliverable, the password to do
so is "1111". 

Once you are using the program, and you want to access the administrator options
(which include getting the current results of the election/certifying the votes,
shutting down the system, and exiting the administrator options), you will
notice that the option to log in as an administrator is hidden. To log in,
follow these steps:  

1. When prompted to vote or register (Are you registered?), input Y, indicating that you are registered.  
2. The system will then prompt you for a 16 digit registration number (this is given to everyone that registers, see below for more details). Enter "0123456789120000"
3. You will be presented with the administrator options. Typing the command exit will return to the normal user interface.

Additional notes about the registration ID:
    The 16 digit registration ID is generated for each person that registers to
    vote. It uses their date
    of birth and their social security number as part of the ID. The rest of the
    registration ID is
    generated randomly for each person. The first ten digits are randomly
    generated. The next two digits
    are the last two values of the voter's year of birth. The last four digits
    are the last four digits of
    the voter's social security number. Since no social security numbers end in
    0000 (see [here](https://en.wikipedia.org/wiki/Social_Security_number#Valid_SSNs) for more
    details) we
    have decided to use
    unique registration ID's that end in 0000 to signify administrators. This
    will allow us to conceal the
    administrator login option from normal end users.


This command can be used to compile the source code with the mysql jar file:
```
  javac -cp .:/src/mysql-connector-java-5.1.40-bin.jar -d bin -sourcepath src src/*.java
```
And to run the code:
```
  java -cp .:mysql-connector-java-5.1.40-bin.jar:./bin GUI
```
The above statements are done from the top level directory of the project.

PLEASE NOTE: You must have an instance of MySQL running to run our program. We
assume the following things:
1. the username is "root"
2. the password is "toor"
3. the host is "localhost"

In addition, the file in the root directory of our project "registeredVoters.csv"
contains ten pre-registered voters. Their information is stored in plain text to
give you an idea of the information we collect. In addition, the fourth number is
the registration ID that you will need if you want to have them vote. It is only
for demonstrative purposes that these ten voters have their information in plain
text. All other voters have their information hashed inside the database.

For the purpose of recounts, we have backup ballots being printed to the command
line. For the purpose of recounts, please refer to these. Often recounts happen
because people do not trust technology and would prefer a lower tech paper ballot
to assuage their concerns of fraud.
