//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBUsersInterface - Database Interface for Users Table							//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for accessing information in the Users	//
//	table of thedatabase.															//
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
//				record.																//
//			Returns: password - String used to store the encrypted password for the //
//				specific user.														//
//																					//
//																					//
//		newUser: Uses the database connection to insert a new row into the Users	//
//			table consisting of the username and password (table: Users, columns:	//
//			Username, Password).  These fields represent a new user of the app.		//
//			Parameters: username - String that holds the username of the new user.	//
//				Password - String that holds the encrypted password of the new user.//
//			Returns: nothing.														//
//																					//
//		close: closes the connection to the database, if it is open.				//
//			Parameters: none.														//
//			Returns: nothing.														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////


package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsersIntf 
{
	
	private Connection conn;

	public DBUsersIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		
	}
	
	public String getPassword(String username)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String password = "";
		
		try 
		{
			pstmt = conn.prepareStatement("Select Password from Users where Username = ?");
			pstmt.setString(1, username);
			res = pstmt.executeQuery();
            if(res.next())
            {
            	password = res.getString(1);	
            }
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return password;
	}
	
	public void newUser(String username, String password)
	{
		PreparedStatement pstmt = null;		
		try 
		{
			pstmt = conn.prepareStatement("insert into Users (username, password) values (?, ?)");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally
		{
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
	}
	
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
