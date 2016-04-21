//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBIngredientIntf - Database Interface for Ingredient Table						//
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
//		getIngredients: Uses the database connection to create a SQL query that		//
//			returns an Array of all Ingredients related to a recipe (tables: 		//
//			Ingredient, RecipeIngredient, columns: Ingredient, RecipeID, join: 		//
//			IngredientID).															//											
//			Parameters: recipeID - the id of the recipe to be used for query.		//
//			Returns: ingredients - Array of Strings used to store the text of each 	// 	
//			ingredient relating to the recipe.										//																								
//																					//
//		search: Uses the database connection to create a SQL query returns an Array //
//			of all recipeIDs that contain an ingredient which has text that matches	//
//			the given search term (tables: Ingredient, RecipeIngredient, columns:	//
//			RecipeID, Ingredient, join: IngredientID).  The query uses a unique 	//
//			constraint in order to avoid repeat recipes in the returned Array.		//
//			Parameters: searchTerm - A String consisting of a search term that each //
//			ingredient in the database will be checked to determine whether it is	//
//			contained in the full ingredient String.								//
//			Returns: recipes - an Array of integers that relate to specific 		//
//			RecipeIDs that contain ingredients in which the search term matched.	//
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
import java.util.ArrayList;

import data.Recipes.Recipe;

public class DBIngredientIntf  //Database Interface for Ingredient Table.
{
	//Database connection used throughout this class.
	private Connection conn;
	
	//Attempt to connect to the database. Always call the close method when finished with the DBUCategoryIntf object.
	public DBIngredientIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e) { e.printStackTrace(); }
		
	}
	
	//Attempt to return an array of all ingredients that are contained in a recipe (using RecipeID).
	public String[] getIngredients(int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String[] ingredients = {""}; 
		
		try //Attempt to query the RecipeIngredient table for IngredientIDs relating to the recipeID, and join the 
		{	//Ingredient table to get the Ingredient String of that IngredientID.
			pstmt = conn.prepareStatement("Select Ingredient.Ingredient from Ingredient Inner Join RecipeIngredient "
					+ "on Ingredient.IngredientID = RecipeIngredient.IngredientID where RecipeIngredient.RecipeID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			ingredients = new String[res.getRow()]; //Instantiate the array using the row number of the last row.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the String of the array to the Ingredient in the ResultSet
			{
				ingredients[res.getRow()-1] = res.getString(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return ingredients; 
	}
	
	//Attempt to search through all of the Ingredients for a specific String and return a list of recipes containing that iingredient.
	public ArrayList<Recipe> search(String searchTerm)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		try //Attempt to query the Ingredient table for IngredientIDs relating to the search term. Join the RecipeIngredient
		{   //table to get the RecipeID of each ingredient.  Distinct constraint makes sure to disclude repeat RecipeIDs.
			
			pstmt = conn.prepareStatement("Select Distinct RecipeIngredient.RecipeID from RecipeIngredient Inner Join Ingredient "
					+ "on RecipeIngredient.IngredientID = Ingredient.IngredientID where Upper(Ingredient.Ingredient) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			while (res.next())
			{
				recipes.add(new Recipe(res.getInt(1)));
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

	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
