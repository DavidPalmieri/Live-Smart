package modifydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import data.Recipe;

public class QueryDBTest 
{

	public static void main(String[] args)
	{
		
		
		//queryAllRecipes();
		buildAllRecipes();
		//queryAllCategories();
	}
	
	private static void queryAllRecipes()
	{
		try
	    {
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        Connection conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
	        
	        PreparedStatement pstmt = conn.prepareStatement("Select * from Recipe");
		    ResultSet results = pstmt.executeQuery();
		   
		    while (results.next())
		    {
		    	System.out.println("RecipeID: " + results.getInt(1) + " Recipe: " + results.getString(2) + " URL:    " + results.getString(3));
		    }
		    
		    //pstmt = conn.prepareStatement("Select * from Instruction");
		    //results = pstmt.executeQuery();
		    
		    //while(results.next())
		    //{
		    	//System.out.println("ID: " + results.getInt(1) + " " + results.getString(2));
		    //}
	    }
	    catch (Exception sqlExcept)
	    {
	        sqlExcept.printStackTrace();
	    }
	}
	
	private static void buildAllRecipes()
	{
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		 
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
            
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from Recipe");
            
           
            
            while (results.next())
            {
                int recipeID = results.getInt(1);

                System.out.println("RecipeID: " + recipeID);
                Recipe recipe = new Recipe(results.getString(3));
                recipe.setDetails(results.getString(2), results.getString(4), results.getString(5), results.getString(6), results.getString(7));
                recipe.setNutritionInfo(results.getString(8), results.getString(9), results.getString(10), results.getString(11), results.getString(12),
                        results.getString(13), results.getString(14), results.getString(15), results.getString(16), results.getString(17), results.getString(18),
                        results.getString(19), results.getString(20), results.getString(21), results.getString(22), results.getString(23));
                
                Statement stmt2 = conn.createStatement();
                ResultSet results2 = stmt2.executeQuery("select IngredientID from RecipeIngredient where RecipeID = " + recipeID);
            
                PreparedStatement pstmt = null;
                
                while (results2.next())
                {
                    int ingredientID = results2.getInt(1);
                    pstmt = conn.prepareStatement("Select Ingredient from Ingredient where ingredientID = ?");
                    pstmt.setInt(1, ingredientID);
                    ResultSet results3 = pstmt.executeQuery();
                    results3.next();
                    String ingredient = results3.getString(1);
                    recipe.addIngredient(ingredient);
                }
                
                Statement stmt3 = conn.createStatement();
                ResultSet results4 = stmt3.executeQuery("select TipID from RecipeTip where RecipeID = " + recipeID);
                
                while (results4.next())
                {
                    int tipID = results4.getInt(1);
                    pstmt = conn.prepareStatement("Select Tip from Tip where TipID = ?");
                    pstmt.setInt(1, tipID);
                    ResultSet results5 = pstmt.executeQuery();
                    results5.next();
                    String tip = results5.getString(1);
                    recipe.addTip(tip);
                }
                
                Statement stmt4 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet results6 = stmt4.executeQuery("select RecInstStepID from RecipeInstruction where RecipeID = " + recipeID);
                
                ArrayList<String> instructions = new ArrayList<String>();
                
                
                results6.last();
                int totalRows = results6.getRow();
                results6.beforeFirst();
                
                for (int i =0; i <= totalRows; i++)
                {
                    instructions.add("");
                }
                
                while (results6.next())
                {
                    int recInstStepID = results6.getInt(1);
                    Statement stmt5 = conn.createStatement();
                    ResultSet results7 = stmt5.executeQuery("Select StepID, InstructionID from RecipeInstructionStep where RecInstStepID = " + recInstStepID);
                    results7.next();
                    int stepID = results7.getInt(1);
                    int instructionID = results7.getInt(2);
                    Statement stmt6 = conn.createStatement();
                    ResultSet results8 = stmt6.executeQuery("Select Instruction from Instruction where InstructionID = " + instructionID);
                    results8.next();
                    String instruction = results8.getString(1);
                    instructions.set(stepID, instruction);
                }
                
                recipe.setDirections(instructions);
                
                recipes.add(recipe);
            }
        }
        catch (Exception sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        
        recipes.get(1000).printRecipe();
        recipes.get(2000).printRecipe();
        recipes.get(3000).printRecipe();
        recipes.get(4000).printRecipe();
        recipes.get(5000).printRecipe();
        recipes.get(6000).printRecipe();
        recipes.get(7000).printRecipe();
        recipes.get(8000).printRecipe();
        recipes.get(9000).printRecipe();
        recipes.get(10000).printRecipe();
        recipes.get(11000).printRecipe();
        recipes.get(12000).printRecipe();
        recipes.get(13000).printRecipe();
        recipes.get(14000).printRecipe();
        recipes.get(15000).printRecipe();        
	}

	private static void queryAllCategories()
	{
		try
	    {
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        Connection conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
	        
	        PreparedStatement pstmt = conn.prepareStatement("Select * from RecipeCategory Where RecipeID = ?");
		    pstmt.setInt(1, 2500);
	        ResultSet results = pstmt.executeQuery();
		   
		    while (results.next())
		    {
		    	System.out.println("RecCatID: " + results.getInt(1) + " RecipeID: " + results.getInt(2) + " CategoryID:    " + results.getInt(3));
		    }
		  
	    }
	    catch (Exception sqlExcept)
	    {
	        sqlExcept.printStackTrace();
	    }
	}
}
