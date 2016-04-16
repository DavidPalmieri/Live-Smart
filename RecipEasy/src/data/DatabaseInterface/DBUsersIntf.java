//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBUsersIntf - Database Interface for Users Table								//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for interacting with  information in 	//
//	the Useers table of the database.												//
//																					//
//																					//
//	Instance Variables:																//
//																					//
//		conn: A private object of type Connection (java.sql) used to direct access 	//
//			to the database.  Initialized in the constructor.						//
//																					//
//																					//
//	Constructors																	//
//																					//
//		Constructor: Creates a connection to the embedded database that all 		//
//			subsequent methods use.													//
//			Parameters: none.														//
//																					//
//																					//
//	Public Methods																	//
//																					//
//		getPassword: Uses the database connection to create a SQL query that 		//
//			returns the encrypted password field (table: Users, column: Password)	//
//			for the record that matches the given username (table: Users, column:	//
//			Username).																//
//			Parameters: username - String used to query the database for a specific	//
//			record.																	//
//			Returns: password - String used to store the encrypted password for the //
//			specific user.															//																					
//																					//
//		getUserID: Uses the database connection to create a SQL query that returns 	//
//			the userID of the specified username (table: Users, columns: UserID, 	//
//			Username).																//
//			Parameters: username - String that holds the username of the specified	//
//			user.																	//
//			Returns: userID - an integer containing the UserID of the specified user//
//																					//
//		newUser: Uses the database connection to insert a new row into the Users	//
//			table consisting of the username and password (table: Users, columns:	//
//			Username, Password).  These fields represent a new user of the app.		//
//			Parameters: username - String that holds the username of the new user.	//
//			password - String that holds the encrypted password of the new user.	//
//			Returns: nothing.														//
//																					//
//		close: closes the connection to the database, if it is open.				//
//			Parameters: none.														//
//			Returns: nothing.														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////


package data.DatabaseInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsersIntf //Database Interface for Users Table.
{
	//Database connection used throughout this class.
	private Connection conn;

	//Attempt to connect to the database. Always call the close method when finished with the DBUsersIntf object.
	public DBUsersIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e) { e.printStackTrace(); }
		
	}
	
	//Attempt to return the encrypted Password of the given Username, or a blank String if not found.
	public String getPassword(String username)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String password = ""; //If no password is found, the String stays blank for use in the calling method.
		
		try //Attempt to query the Users table for records relating to the Username .
		{
			pstmt = conn.prepareStatement("Select Password from Users where Username = ?");
			pstmt.setString(1, username);
			res = pstmt.executeQuery();
			
            if(res.next()) //If a record is found set the password to the first (only) record returned.
            {
            	password = res.getString(1);	
            }
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return password; //Return the empty String, or encrypted password String if found
	}
	
	//Attempt to return the UserID of the given username.
	public int getUserID(String username)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int userID = 0;
		
		try //Attempt to query the Users table for records relating to the Username .
		{
			pstmt = conn.prepareStatement("Select UserID from Users where Username = ?");
			pstmt.setString(1, username);
			res = pstmt.executeQuery();
			
            if(res.next()) //If a record is found set the userID to the first (only) record returned.
            {
            	userID = res.getInt(1);	
            }
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return userID;
	}
	
	//Attempt to inserts a new user (Users table: Username and Password (UserID is auto-generated)) into the database.
	public void newUser(String username, String password)
	{
		PreparedStatement pstmt = null;
		
		try //Attempt to insert the information into the table
		{
			pstmt = conn.prepareStatement("insert into Users (username, password) values (?, ?)");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //close all database objects
		{
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
	}
	
	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
