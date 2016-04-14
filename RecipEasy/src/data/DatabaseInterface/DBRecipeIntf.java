//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBRecipeIntf - Database Interface for Recipe Table								//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for interacting with information in 	//
//	the Recipe table of the database.												//
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
//		getBasicInfo: Uses the database connection to create a SQL query that 		//
//			returns the basic info of a recipe (Table: Recipe, columns: Title,		//
//			Summary, PrepTime, TotalTime, Servings Url) using the recipeID.  The  	//
//			returned row is usedto create an Array of Strings that is returned.		//
//			Parameters: recipeID - int used to query the database for the related	//
//				record.																//
//			Returns: basicInfo - Array of Strings used to store the details of a 	//
//				BasicInfo object used by the RecipeObject.							//
//																					//
//		getNutrition: Uses the database connection to create a SQL query that 		//
//			returns the nutritional info of a recipe (Table: Recipe, columns: 		//
//			ServingSize, Calories, CalFat, TotFat, SatFat, TransFat, Cholesterol,	// 
//			Sodium, Carbs, Fiber, Sugar, Protein, VitA, VitC, Calcium, Iron) using  //	
//			the recipeID.  The returned row is usedto create an Array of Strings 	//
//			that is returned.														//
//			Parameters: recipeID - int used to query the database for the related	//
//				record.																//
//			Returns: basicInfo - Array of Strings used to store the details of a 	//
//				Nutrition object used by the RecipeObject.							//
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
import java.util.Random;

public class DBRecipeIntf // Database Interface for Recipe Table.
{
	// Database connection used throughout this class.
	private Connection conn;

	// Attempt to connect to the database. Always call the close method when finished with the DBUsersIntf object.
	public DBRecipeIntf() 
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e) { e.printStackTrace(); }
		
	}

	// Attempt to return an Array of Strings consisting of the basic information of the specified recipe.
	public String[] getBasicInfo(int recipeID) 
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String[] basicInfo = new String[6];

		try // Attempt to query the Recipe table for the basic recipe information relating to the specified recipeID.
		{
			pstmt = conn.prepareStatement(
					"Select Title, Summary, PrepTime, TotalTime, Servings, Url from Recipe where RecipeID = ?");
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();

			if (res.next()) // Use the returned row of the ResultSet to build the Array of information.
			{// easily increment through both the array, and returned row of results.
				for (int i = 0; i < 6; i++) basicInfo[i] = res.getString(i + 1);
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}

		return basicInfo;
	}

	// Attempt to return an Array of Strings consisting of the nutrition information of the specified recipe.
	public String[] getNutrition(int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String[] nutrition = new String[16]; 
		
		try //Attempt to query the Recipe table for the nutritional information relating to the specified recipeID. 
		{
			pstmt = conn.prepareStatement("Select ServingSize, Calories, CalFat, TotFat, SatFat, TransFat, Cholesterol, "
					+ "Sodium, Carbs, Fiber, Sugar, Protein, VitA, VitC, Calcium, Iron from Recipe where RecipeID = ?");
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
				
			if (res.next()) //Use the returned row of the ResultSet to build the Array of information.
			{//easily increment through both the array, and returned row of results.
				for (int i = 0; i < 16; i++) nutrition[i] = res.getString(i + 1);
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return nutrition; 
	}
	
	//Attempt to get an array of all Recipes, and select one at random.
		public int randomRecipe()
		{
			ResultSet res = null;
			int[] recipes = {};
			int results = 0;
			
			//First, get an array of all recipes, along with a size to set the range for a random number generator.
			try //Attempt to query the Recipe table for all RecipeIDs. 
			{
				res = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("Select RecipeID from Recipe");
				
				//Get the amount of records returned in the ResultSet to create an array of RecipeIDs.
				res.last(); //Set the ResultSet pointer to the last row.
				results = res.getRow(); //Get the amount of returned results using the row number of the last row.
				recipes = new int[results]; //Instantiate the array using the results integer.
				res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
					
				while (res.next()) //increment through the ResultSet, setting the integer of the array to the RecipeID in the ResultSet.
				{
					recipes[res.getRow()-1] = res.getInt(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
				}
			} 
			catch (SQLException e) { e.printStackTrace(); }
			finally //Close all database objects.
			{
				try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
			}
			
			//Next, use the size of the array to create a new Random number generator, and select a random index of the array as our random recipeID.
			Random random = new Random();
			double rndNum = results * random.nextDouble();
		       
			return recipes[(int)rndNum]; 
		}

	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
