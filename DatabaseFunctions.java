import java.sql.*;
import java.util.ArrayList;

/*
This class relates to the database functions necessary to operate the google sheets program as a whole in a semi-automatic manner.
Methods used here should only ever be called from the GoogleSheetsIntegration.java file, with the exception of the openConnection method or temporary debugging within the main class.
 */

public class DatabaseFunctions {

   
    //Opens a connection to the database, this can be accessed by running the method and storing the result in a Connection variable.
    public Connection openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection myConnection = DriverManager.getConnection(URl, user, password);
            System.out.println("Function Run: Connection Established.");
            return myConnection;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Gathers all required data from the MailDrop table and stores the result in a single MailDrop object, which is then passed into an array of objects of the same type.
    public ArrayList<MailDropObj> gatheredRequiredDataFromMailDropTable() throws SQLException {
        ArrayList<MailDropObj> mailDropObjs = new ArrayList<>();

        String SQL = "REDACTED";
        Statement statement = openConnection().prepareStatement(SQL);
        ResultSet rs = statement.executeQuery(SQL);

        while (rs.next()) {
            //Create new MailDrop object.
            MailDropObj mailDropObj = new MailDropObj();

            //Gather data to create object.
            REDACTED

            //Add MailDrop object to ArrayList.
            mailDropObjs.add(mailDropObj);

        }
        return mailDropObjs;
    }

    public void setIsPassedToGoogleSheetsTrue(String dataTable) throws SQLException
    {
        String SQL = "REDACTED";
        Statement statement = openConnection().prepareStatement(SQL);

        int rs = statement.executeUpdate(SQL);

        System.out.println("Query Executed Successfully, existing records now reflect Google Sheets parity, total rows affected = " + statement.getUpdateCount());

    }


//The main function should be empty in the final release, only used for debug purposes. An example of debug which prints out one piece of data from a gathered object list is as follows;
    /*
     ArrayList<MailDropObj> mailDropObjs = new DatabaseFunctions().gatheredRequiredDataFromMailDropTable();

    for (int i = 0; i < mailDropObjs.size(); i++)
    {
        System.out.println(mailDropObjs.get(i).getMailDropBusinessName());
    }
     */
    public static void main(String[] args) throws SQLException {


    }
}
