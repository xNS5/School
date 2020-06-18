import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;
import java.sql.*;

public class mainHelper {
    private static final String PATTERNSTRING = "\\=([^=&]+)\\&";
    private static final File CREDS = new File("credentials.txt");
    private static final String ADD1 = "INSERT INTO employees(emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (?,?,?,?,?,CURRENT_DATE());";
    private static final String ADD2 = "INSERT IGNORE INTO salaries(emp_no, salary, from_date, to_date) VALUES (?, ?, CURRENT_DATE(), '9999-01-01');";
    private static final String ADD3 = "INSERT IGNORE INTO dept_emp(emp_no, dept_no, from_date, to_date) SELECT ?, departments.dept_no, CURRENT_DATE(), '9999-01-01' FROM departments WHERE dept_name=?;";
    private static final String QUERYSTATEMENT = "select sum(salary) from salaries where to_date = '9999-01-01'";

    public static void main(final String[] args) {
        if (args.length != 0) {
            final String arg = args[0];
            try {
                Connection connect = initializeConnection();
                if (!connect.isValid(1)) {
                    connect.close();
                    throw new SQLException();
                }
                switch (arg.toUpperCase(Locale.getDefault())) {
                    case "SHOW":
                        if("salaries".equals(args[1])) {
                            sum(connect);
                        } else {
                            show(args, connect);
                        }
                        break;
                    case "ADD":
                        add(args, connect);
                        break;
                    case "DELETE":
                        delete(args, connect);
                        break;
                    default:
                        connect.close();
                        break;
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    /*
     * Connection parses the input from credentials.txt and grabs the username and password for the SQL database.
     * It then creates an arrayList of those arguments. After that, thoe credentials are added to a Parameters object.
     * Lastly, the parameter object is passed to the .getConnection(...) and returns the connection object.
     * */
    public static Connection initializeConnection() throws SQLException, FileNotFoundException {
        final ArrayList<String> arrList = new ArrayList<>();
        final Properties info = new Properties();
        final Scanner credsScanner = new Scanner(CREDS);
        final String credentials = credsScanner.nextLine();
        credsScanner.close();
        final Pattern pattern = Pattern.compile(PATTERNSTRING); //Grabbing strings matching the pattern
        final Matcher patternMatcher = pattern.matcher(credentials);

        //While the pattern matcher has input
        while (patternMatcher.find()) {
            arrList.add(patternMatcher.group(1));
        }

        //Putting username and password in Properties object
        info.put("user", arrList.get(0));
        info.put("password", arrList.get(1));
        info.put("useSSL", "true");
        return DriverManager.getConnection(credentials.substring(0, credentials.indexOf("?")), info); // .getConnection using the sql path and passing
        //properties to it
    }

    /*
     * Add imports user arguments into the employees, salary, and dept_emp tables. If something is wrong with the arguments, it returns null
     * */
    public static void add(final String[] args, final Connection conn) throws SQLException {
        final Statement statement = conn.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT MAX(emp_no) FROM employees");
        if (args.length == 8) {
            // Input format:  <first_name> <last_name> <dept_name> <birthdate> <gender> <salary>
            final String firstName = args[2];
            final String lastName = args[3];
            final String deptName = args[4];
            final String birthDate = args[5];
            final String gender = args[6];
            final int salary = Integer.parseInt(args[7]);
            //Getting highest employee ID to avoid conflicts
            resultSet.next();
            final int newID = resultSet.getInt("MAX(emp_no)") + 1;
            resultSet.close();
            statement.close();

            //Adding to employees table
            PreparedStatement preparedStatement = conn.prepareStatement(ADD1);
            preparedStatement.setInt(1, newID); //getInt(MAX(emp_no))
            preparedStatement.setString(2, birthDate); // args[3]
            preparedStatement.setString(3, firstName); // args[0]
            preparedStatement.setString(4, lastName); // args[1]
            preparedStatement.setString(5, gender); // args[4]
            preparedStatement.executeUpdate();

            //adding to salaries table
            preparedStatement = conn.prepareStatement(ADD2);
            preparedStatement.setInt(1, newID); //getInt(MAX(emp_no))
            preparedStatement.setInt(2, salary); //args[5]
            preparedStatement.executeUpdate();

            //adding to dept_emp table
            preparedStatement = conn.prepareStatement(ADD3);
            preparedStatement.setInt(1, newID); // getInt(MAX(emp_no))
            preparedStatement.setString(2, deptName); // args[2]
            preparedStatement.executeUpdate();

            preparedStatement.close();
            System.out.printf("Employee %s %s added!\r\n", firstName, lastName);
        }
        resultSet.close();
        statement.close();
    }

    /*
     *  Delete removes a user from the database by first deleting their entry from the employees table,
     * then the salary table, and finally the dept_emp table.
     * */
    public static void delete(final String[] args, final Connection conn) throws SQLException, NumberFormatException {
        final int empid = Integer.parseInt(args[2]);
        final Statement statement = conn.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM employees WHERE emp_no = " + empid + ";");

        //result.next() returns a boolean, true if it contains values, false if not.
        if (resultSet.next() == false) {
            System.out.printf("Employee with id %d does not exist.\r\n", empid);
            resultSet.close();
            statement.close();
            return;
        } else {
            PreparedStatement preparedStatement = null;
            final String[] dbList = {"employees", "salaries", "dept_emp"};
            for (int i = 0; i < 3; i++) {
                final String updateString = "DELETE FROM " + dbList[i] + " WHERE emp_no= ? ;";
                preparedStatement = conn.prepareStatement(updateString);
                preparedStatement.setInt(1, empid);
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
        }
        System.out.printf("Employee %s %s deleted!\r\n", resultSet.getString("first_name"), resultSet.getString("last_name"));
        statement.close();
        resultSet.close();
    }

    public static void show( final String[] input, final Connection connect) throws SQLException {
        final Statement statement = connect.createStatement();
        final String attribute = input[1];
        String deptName = input[3];
        if(input.length > 4) {
            deptName = input[3] + " " + input[4];
        }

        final String queryStatement = "SELECT " + attribute +".emp_no, first_name, last_name, "
                + "dept_name FROM "+ attribute + ", dept_emp, departments WHERE "
                + attribute + ".emp_no = dept_emp.emp_no AND dept_emp.dept_no = "
                + "departments.dept_no AND dept_name = \'" + deptName +  "\';";
        final ResultSet queryResult = statement.executeQuery(queryStatement);
        writeResultSet(queryResult);
        statement.close();
        queryResult.close();
    }

    public static void sum(final Connection connect) throws SQLException{
        final Statement statement = connect.createStatement();
        final ResultSet queryResult = statement.executeQuery(QUERYSTATEMENT);
        if(queryResult.next()) {
            System.out.println("$" + queryResult.getString(1));
        }
        statement.close();
        queryResult.close();
    }

    public static void writeResultSet(final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData meta = resultSet.getMetaData();
        final int columnsNumber = meta.getColumnCount();
        while (resultSet.next()) {
            for(int i = 1; i < columnsNumber; i++){
                System.out.print(resultSet.getString(i) + " ");
            }
            System.out.println();
        }
    }

}
