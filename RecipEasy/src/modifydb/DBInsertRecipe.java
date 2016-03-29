package modifydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Recipe;

public class DBInsertRecipe 
{

	private String dbURL = "jdbc:derby:RecipeDB;create=false;";
    private  Connection conn = null;
    
    private  Recipe recipe;
    
    public DBInsertRecipe()throws SQLException
    {
    	try
    	{
    		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    		conn = DriverManager.getConnection(dbURL);
    	}
    	catch (Exception sqlExcept)
    	{
    		sqlExcept.printStackTrace();
    	}  
    	    
        //using the recipeID and categoryID, link them in the linking table
        
        //closeConn();
    }
    
    public Recipe insertRecipe(Recipe recipe) throws SQLException
    {
    	this.recipe = recipe;
        String[] details = recipe.getDetails();
        String[] nutrition = recipe.getNutritionInfo();
        
        PreparedStatement pstmt = conn.prepareStatement("insert into Recipe "
                + "(Title, URL, PrepTime, TotalTime, Servings, Summary, ServingSize, "
                + "Calories, CalFat, TotFat, SatFat, TransFat, Cholesterol, Sodium, "
                + "Carbs, Fiber, Sugar, Protein, VitA, VitC, Calcium, Iron) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        pstmt.setString(1, details[0]);
        pstmt.setString(2, details[1]);
        pstmt.setString(3, details[2]);
        pstmt.setString(4, details[3]);
        pstmt.setString(5, details[4]);
        pstmt.setString(6, details[5]);
        pstmt.setString(7, nutrition[0]);
        pstmt.setString(8, nutrition[1]);
        pstmt.setString(9, nutrition[2]);
        pstmt.setString(10, nutrition[3]);
        pstmt.setString(11, nutrition[4]);
        pstmt.setString(12, nutrition[5]);
        pstmt.setString(13, nutrition[6]);
        pstmt.setString(14, nutrition[7]);
        pstmt.setString(15, nutrition[8]);
        pstmt.setString(16, nutrition[9]);
        pstmt.setString(17, nutrition[10]);
        pstmt.setString(18, nutrition[11]);
        pstmt.setString(19, nutrition[12]);
        pstmt.setString(20, nutrition[13]);
        pstmt.setString(21, nutrition[14]);
        pstmt.setString(22, nutrition[15]);
        pstmt.executeUpdate();
        
        pstmt = conn.prepareStatement("Select RecipeID from Recipe where URL = ?");
        pstmt.setString(1, details[1]);
        ResultSet results = pstmt.executeQuery();
        
        if (results.next())
        {
        	recipe.setID(results.getInt(1));
        }
        
        insertDirections();
        insertTips();
        insertIngredients();
        
        return recipe;
    }
   
    private void insertDirections() throws SQLException
    {
    	
        
        ArrayList<String> directions = recipe.getDirections();
        for (int i = 0; i < directions.size(); i++)
        {
            String direction = directions.get(i);
            PreparedStatement pstmt = conn.prepareStatement("Select InstructionID from Instruction where Instruction = ?");
            pstmt.setString(1, direction);
            ResultSet results = pstmt.executeQuery();
            
            if (results.next())
            {
                int directionID = results.getInt(1);
                pstmt = conn.prepareStatement("Insert into RecipeInstructionStep (StepID, InstructionID) values (?,?)");
                pstmt.setInt(1, (i));
                pstmt.setInt(2, directionID);
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("Select RecInstStepID from RecipeInstructionStep where StepID = ? And InstructionID = ?");
                pstmt.setInt(1, (i));
                pstmt.setInt(2, directionID);
                results = pstmt.executeQuery();
                results.next();
                int recInstStepID = results.getInt(1);
                
                pstmt = conn.prepareStatement("Insert into RecipeInstruction (RecipeID, RecInstStepID) values (?, ?)");
                pstmt.setInt(1, recipe.getID());
                pstmt.setInt(2, recInstStepID);
                pstmt.executeUpdate();
            }
            else
            {
                pstmt = conn.prepareStatement("Insert into Instruction (Instruction) values (?)");
                pstmt.setString(1, direction);
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("Select InstructionID from Instruction where Instruction = ?");
                pstmt.setString(1, direction);
                results = pstmt.executeQuery();
                results.next();
                int directionID = results.getInt(1);
                
                pstmt = conn.prepareStatement("Insert into RecipeInstructionStep (StepID, InstructionID) values (?,?)");
                pstmt.setInt(1, (i+1));
                pstmt.setInt(2, directionID);
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("Select RecInstStepID from RecipeInstructionStep where StepID = ? And InstructionID = ?");
                pstmt.setInt(1, (i+1));
                pstmt.setInt(2, directionID);
                results = pstmt.executeQuery();
                results.next();
                int recInstStepID = results.getInt(1);
                
                pstmt = conn.prepareStatement("Insert into RecipeInstruction (RecipeID, RecInstStepID) values (?, ?)");
                pstmt.setInt(1, recipe.getID());
                pstmt.setInt(2, recInstStepID);
                pstmt.executeUpdate();
            }
        }    
    }
    
   private void insertTips() throws SQLException
   {       
       ArrayList<String> tips = recipe.getTips();
       for (int i = 0; i < tips.size(); i++)
       {
           String tip = tips.get(i);
           
           PreparedStatement pstmt = conn.prepareStatement("Select TipID from Tip where Tip = ?");
           pstmt.setString(1, tip);
           ResultSet results = pstmt.executeQuery();
           
           if (results.next())
           {
               int tipID = results.getInt(1);
               pstmt = conn.prepareStatement("Insert into RecipeTip (RecipeID, TipID) values (?, ?)");
               pstmt.setInt(1, recipe.getID());
               pstmt.setInt(2, tipID);
               pstmt.executeUpdate();
           }
           else
           {
               pstmt = conn.prepareStatement("Insert into Tip (Tip) values (?)");
               pstmt.setString(1, tip);
               pstmt.executeUpdate();
               
               pstmt = conn.prepareStatement("Select TipID from Tip where Tip = ?");
               pstmt.setString(1, tip);
               results = pstmt.executeQuery();
               results.next();
               int tipID = results.getInt(1);
               
               pstmt = conn.prepareStatement("Insert into RecipeTip (RecipeID, TipID) values (?, ?)");
               pstmt.setInt(1, recipe.getID());
               pstmt.setInt(2, tipID);
               pstmt.executeUpdate();
           }
       }
   }

   private void insertIngredients() throws SQLException
   {
       ArrayList<String> ingredients = recipe.getIngredients();
       for (int i = 0; i < ingredients.size(); i++)
       {
           String ingredient = ingredients.get(i);
           
           PreparedStatement pstmt = conn.prepareStatement("Select IngredientID from Ingredient where Ingredient = ?");
           pstmt.setString(1, ingredient);
           ResultSet results = pstmt.executeQuery();
           
           if (results.next())
           {
               int IngredientID = results.getInt(1);
               pstmt = conn.prepareStatement("Insert into RecipeIngredient (RecipeID, IngredientID) values (?, ?)");
               pstmt.setInt(1, recipe.getID());
               pstmt.setInt(2, IngredientID);
               pstmt.executeUpdate();
           }
           else
           {
               pstmt = conn.prepareStatement("Insert into Ingredient (Ingredient) values (?)");
               pstmt.setString(1, ingredient);
               pstmt.executeUpdate();
               
               pstmt = conn.prepareStatement("Select IngredientID from Ingredient where Ingredient = ?");
               pstmt.setString(1, ingredient);
               results = pstmt.executeQuery();
               results.next();
               int IngredientID = results.getInt(1);
               
               pstmt = conn.prepareStatement("Insert into RecipeIngredient (RecipeID, IngredientID) values (?, ?)");
               pstmt.setInt(1, recipe.getID());
               pstmt.setInt(2, IngredientID);
               pstmt.executeUpdate();
           }
       }        
   }
}
