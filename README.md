To run this project, please insure the the files candidates.csv, regLog.csv,
and voterLogs.csv are present in the same directory that you are in when
running the program. If you run it from the top level directory of our
project, then make sure that the aforementioned files are in that directory.

To unlock the system when starting it, an administrator is required to

input their password to do so. For this deliverable, the password to do
so is "111111". 

Once you are using the program, and you want to access the administrator options
(which include getting the current results of the election, shutting down
the system, and exiting the administrator options), you will notice that the
option to log in as an administrator is hidden. To log in, follow these steps:
    1. When prompted to vote or register (Are you registered?), input Y,
       indicating that you are registered.
    2. The system will then prompt you for a 16 digit registration number (this
       is given to everyone that registers, see below for more details). Enter
       "0123456789120000"
    3. You will be presented with the administrator options. Typing the command
       exit will return to the normal user interface.

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
    0000 (see here for more
    details: https://en.wikipedia.org/wiki/Social_Security_number#Valid_SSNs) we
    have decided to use
    unique registration ID's that end in 0000 to signify administrators. This
    will allow us to conceal the
    administrator login option from normal end users.


This command can be used to compile the source code with the mysql jar file:

  javac -cp .:/src/mysql-connector-java-5.1.40-bin.jar -d bin -sourcepath src src/*.java

And to run the code:
  java -cp .:mysql-connector-java-5.1.40-bin.jar:./bin TempCLI

The above statements are done from the top level directory of the project, with
the associated csv files there as well.