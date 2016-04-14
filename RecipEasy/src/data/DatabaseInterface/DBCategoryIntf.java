//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBCategoryIntf - Database Interface for Category Table							//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for interacting with  information in 	//
//	the Category table of the database.												//
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
//		getCategories: Uses the database connection to create a SQL query that 		//
//			returns an array of all categories that a recipe is a part of (tables: 	//
//			RecipeCategory, Category, columns: Category, RecipeID, join: CategoryID)//
//			Parameters: recipeID - int used to query the database for all related	//
//			records.																//
//			Returns: categories -Array of Strings used to store the name of each 	//
//			category relating to the recipe.										//																					
//																					//
//		getRecipes: Uses the database connection to create a SQL query that returns	//
//			an array of all recipes that a category contains (table: RecipeCategory	//
//			columns: CategoryID, RecipeID)											//
//			Parameters: categoryID - int used to query the database for all related	//
//			records.																//
//			Returns: recipes -Array of integers used to store the recipeID of each 	//
//			recipe contained in the category.										//
//																					//
//		search: Uses the database connection to create a SQL query that returns an	//
//			array of all categories that contain the search term in their name 		//
//			(table: Category, Columns: Category, CategoryID).						//
//			Parameters: searchTerm - A String representing the text that a user is	//
//			searching for in the list fo categories.								//
//			Returns: categories - an array of integers representing categoryIDs		//
//			of Categories that match the search term,								//
//																					//
//		randomCategory: Uses the database connection to create a SQL query that		//
//			returns an array of all CategoryIDs (table: Category, Column: 			//
//			CategoryID).  The array's size is used as a range (0 - max size) to 	//
//			generate a random int,  that integer is used as an index to the array,	//
//			and whichever CategoryID is contained at that index is returned as the 	//
//			random category.														//
//			Parameters: None.														//
//			Returns: categories[index] - An integer representing a CategoryID.		//
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

public class DBCategoryIntf //Database Interface for Category Table.
{
	//Database connection used throughout this class.
	private Connection conn;
	
	//Attempt to connect to the database. Always call the close method when finished with the DBUCategoryIntf object.
	public DBCategoryIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e) { e.printStackTrace(); }
		
	}
	
	//Attempt to return an array of all Category names that a recipe (using RecipeID) is a part of.
	public String[] getCategories(int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String[] categories = {""}; 
		
		try //Attempt to query the RecipeCategory table for CategoryIDs relating to the recipeID, and join the Category 
		{	//table to get the category name of that category ID.
			pstmt = conn.prepareStatement("Select Category.Category from Category Inner Join RecipeCategory "
					+ "on Category.CategoryID = RecipeCategory.CategoryID where RecipeCategory.RecipeID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			categories = new String[res.getRow()]; //Instantiate the array using the row number of the last row.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the String of the array to the Category in the ResultSet
			{
				categories[res.getRow()-1] = res.getString(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return categories; 
	}
	
	//Attempt to get an Array containing all RecipeIDs that fall under the specified CategoryID.
	public int[] getRecipes(int categoryID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int[] recipes = {}; 
		
		try //Attempt to query the RecipeCategory table for RecipeIDs relating to the CategoryID. 
		{
			pstmt = conn.prepareStatement("Select RecipeID from RecipeCategory where CategoryID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setInt(1, categoryID);
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			recipes = new int[res.getRow()]; //Instantiate the array using the row number of the last row.
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
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return recipes; 
	}
	
	//Attempt to search through all of the categories for a specific String
	public int[] search(String searchTerm)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int[] categories = {}; 
		
		try //Attempt to query the Category table for CategoryIDs relating to the search term. 
		{
			pstmt = conn.prepareStatement("Select CategoryID from Category where Category Like ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			categories = new int[res.getRow()]; //Instantiate the array using the row number of the last row.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the integer of the array to the CategoryID in the ResultSet.
			{
				categories[res.getRow()-1] = res.getInt(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return categories; 
	}
	
	//Attempt to get an array of all Categories, and select one at random.
	public int randomCategory()
	{
		ResultSet res = null;
		int[] categories = {};
		int results = 0;
		
		//First, get an array of all categories, along with a size to set the range for a random number generator.
		try //Attempt to query the Category table for all CategoryIDs. 
		{
			res = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("Select CategoryID from Category");
			
			//Get the amount of records returned in the ResultSet to create an array of CategoryIDs.
			res.last(); //Set the ResultSet pointer to the last row.
			results = res.getRow(); //Get the amount of returned results using the row number of the last row.
			categories = new int[results]; //Instantiate the array using the results integer.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the integer of the array to the CategoryID in the ResultSet.
			{
				categories[res.getRow()-1] = res.getInt(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		//Next, use the size of the array to create a new Random number generator, and select a random index of the array as our random categoryID.
		Random random = new Random();
		double rndNum = results * random.nextDouble();
	       
		return categories[(int)rndNum]; 
	}
	
	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
