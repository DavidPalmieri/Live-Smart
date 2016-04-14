package data.DatabaseInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	public int[] search(String searchTerm)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int[] recipes = {};
		
		try //Attempt to query the Ingredient table for IngredientIDs relating to the search term. Join the RecipeIngredient
		{   //table to get the RecipeID of each ingredient.  Unique constraint makes sure to disclude repeat RecipeIDs.
			
			pstmt = conn.prepareStatement("Select Unique RecipeIngredient.RecipeID from RecipeIngredient Inner Join Ingredient "
					+ "on RecipeIngredient.IngredientID = Ingredient.IngredientID where Ingredient.Ingredient Like =?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			recipes = new int[res.getRow()]; //Instantiate the array using the row number of the last row.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the integer of the array to the CategoryID in the ResultSet.
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

	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
