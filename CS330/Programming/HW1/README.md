# Instructions

For this assignment, you will be using the existing Employees database (see /files for database)

This time around we want to allow the manager to access some of the database but without having to teach them SQL. 
Typically that would be the time to develop a web interface or better yet a RESTful API but this is still a database class, so, nope.

Instead, we will be building a command line interface (CLI) for our HR manager and pretend that they would be using such an interface to manage the employee database. 
You will be using Java for this part of the assignment and its JDBC connector to access MySQL. There is a handful of IDEs (e.g., IntelliJ) or you can just work everything through vim, nano, etc. 
There is plenty of online tutorials for Java and MySQL (e.g., https://www.vogella.com/tutorials/MySQLJava/article.html (Links to an external site.) or 
http://www.ntu.edu.sg/home/ehchua/programming/java/jdbc_basic.html (Links to an external site.)). 
Notice that in this example the credentials are hard-coded in the application as opposed to an external file. 

# Spec

java main show employees department <department name> # printing the employee table as a tsv for a particular department

    <empid> <first_name> <last_name>

    <empid> <first_name> <last_name>

    <empid> <first_name> <last_name>
    

java main add employee <first_name> <last_name> <dept_name> <birthdate> <gender> <salary> # adding a new employee to the database

    Employee <first_name> <last_name> added!
    
java main delete employee <empid> # deleting an employee from the database

    Employee <first_name> <last_name> deleted!
    
 or
 
    Employee with id <empid> does not exist.
    
java main show salaries sum # displaying the total salary payouts for the latest year for all employees
    
    $900000
