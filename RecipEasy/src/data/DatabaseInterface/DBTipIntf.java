//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBCategoryIntf - Database Interface for Tip Table								//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for interacting with information in 	//
//	the Tip table of the database.													//
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
//		getTips: Uses the database connection to create a SQL query that returns an //
//			Array of all Tips related to a Recipe (tables: Tip, RecipeTip, columns: //
//			Tip, RecipeID, join: TipID)												//
//			Parameters: recipeID - the id of the recipe to be used for query.		//
//			Returns: tips -Array of Strings used to store the text of each tip	 	//
//			relating to the recipe.													//																					
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

public class DBTipIntf  //Database Interface for Tip Table.
{
	//Database connection used throughout this class.
	private Connection conn;
	
	//Attempt to connect to the database. Always call the close method when finished with the DBUCategoryIntf object.
	public DBTipIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e) { e.printStackTrace(); }
		
	}
	
	//Attempt to return an array of all tips that are contained in a recipe (using RecipeID).
	public String[] getTips(int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String[] tips = {""}; 
		
		try //Attempt to query the RecipeTip table for TipIDs relating to the recipeID, and join the Tip 
		{	//table to get the Tip String of that TipID.
			pstmt = conn.prepareStatement("Select Tip.Tip from Tip Inner Join RecipeTip on Tip.TipID "
					+ "= RecipeTip.TipID where RecipeTip.RecipeID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			tips = new String[res.getRow()]; //Instantiate the array using the row number of the last row.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the String of the array to the Tip in the ResultSet
			{
				tips[res.getRow()-1] = res.getString(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return tips; 
	}
	
	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
