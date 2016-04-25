package data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Map.Entry;



public class DataGrabber 
{
	
	Connection conn;
	
	
	public DataGrabber()
	{
		try
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
		}
		catch (Exception e) { e.printStackTrace(); }
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
	
	//Attempt to return the encrypted Password of the given Username, or a blank String if not found.
	public String getPassword(int userID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String password = ""; //If no password is found, the String stays blank for use in the calling method.
		
		try //Attempt to query the Users table for records relating to the Username .
		{
			pstmt = conn.prepareStatement("Select Password from Users where UserID = ?");
			pstmt.setInt(1, userID);
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
	
	//Attempt to inserts a new user (Users table: Username and Password (UserID is auto-generated)) into the database.
	public int newUser(String username, String password)
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
		
		return getUserID(username);
	}
	
	//Attempt to return a Recipe built with the basic information of the specified recipeID.
	public Recipe getRecipe(Recipe recipe) 
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int recipeID = recipe.getRecipeID();
		Rating rating = new Rating();
		ArrayList<Category> categories = new ArrayList<Category>();

		try // Attempt to query the Recipe and Category tables for the basic recipe information relating to the specified recipeID.
		{
			pstmt = conn.prepareStatement("Select Recipe.Title, Recipe.Summary, Recipe.PrepTime, Recipe.TotalTime, "
					+ "Recipe.Servings, Recipe.Url, Category.Category, Category.CategoryID, "
					+ "Rating.Liked, Rating.Ease, Rating.Cost "
					+ "from Recipe "
					+ "Left Join RecipeCategory On Recipe.RecipeID = RecipeCategory.RecipeID "
					+ "Left Join Category On RecipeCategory.CategoryID = Category.CategoryID "
					+ "Left Join Rating On Recipe.RecipeID = Rating.RecipeID "
					+ "where Recipe.RecipeID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();

			while (res.next()) // Use the returned rows of the ResultSet to build the Recipe object.
			{
				if(res.getRow() == 1)
				{
					recipe.setDetails(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
					categories.add(new Category(res.getString(7), res.getInt(8)));
					rating.setUserRating(res.getInt(9), res.getInt(10), res.getInt(11));
				}
				else
				{
					categories.add(new Category(res.getString(7), res.getInt(8)));					
				}
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}

		recipe.setRating(rating);
		recipe.setCategories(categories);
		return recipe;
	}
	
	
	public Recipe getRatings(Recipe recipe)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int recipeID = recipe.getRecipeID();
		Rating rating = recipe.getRating();

		try // Attempt to query the Rating table for all ratings related to the given recipeID.
		{
			pstmt = conn.prepareStatement("Select Liked, Ease, Cost from Rating where recipeID = ? ");
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
			
			while (res.next())
			{
				rating.addRating(res.getInt(1), res.getInt(2), res.getInt(3));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		recipe.setRating(rating);
		return recipe;	
	}
	
	//Attempt to return a Recipe built with the rest of the information needed for display.
	public Recipe getRecipeDetails(Recipe recipe) 
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int recipeID = recipe.getRecipeID();
		
		ArrayList<String> ingredients = new ArrayList<String>();
		ArrayList<String> instructions = new ArrayList<String>();
		ArrayList<String> tips = new ArrayList<String>();
		ArrayList<String> nutrition = new ArrayList<String>();

		try // Attempt to query the other Recipe tables for the rest of the recipe information relating to the specified recipeID.
		{
			pstmt = conn.prepareStatement("Select Ingredient.Ingredient, Instruction.Instruction, Tip.Tip, "
					+ "Recipe.ServingSize, Recipe.Calories, Recipe.CalFat, Recipe.TotFat, Recipe.SatFat, "
					+ "Recipe.TransFat, Recipe.Cholesterol, Recipe.Sodium, Recipe.Carbs, Recipe.Fiber, "
					+ "Recipe.Sugar, Recipe.Protein, Recipe.VitA, Recipe.VitC, Recipe.Calcium, Recipe.Iron "
					+ "from Recipe "
					+ "Left Join RecipeIngredient On Recipe.RecipeID = RecipeIngredient.RecipeID "
					+ "Left Join Ingredient On RecipeIngredient.IngredientID = Ingredient.IngredientID "
					+ "Left Join RecipeInstruction On Recipe.RecipeID = RecipeInstruction.RecipeID "
					+ "Left Join RecipeInstructionStep On RecipeInstruction.RecInstStepID = RecipeInstructionStep.RecInstStepID "
					+ "Left Join Instruction On RecipeInstructionStep.InstructionID = Instruction.InstructionID "
					+ "Left Join RecipeTip On Recipe.RecipeID = RecipeTip.RecipeID "
					+ "Left Join Tip On Tip.TipID = RecipeTip.TipID "
					+ "where Recipe.RecipeID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();

			while (res.next()) // Use the returned rows of the ResultSet to build the rest of the Recipe object.
			{
				//Add the Ingredients to the ArrayList
				if(res.getString(1) != null)
				{
					if(!(ingredients.contains(res.getString(1))))
					{
						ingredients.add(res.getString(1));
					}
				}
				
				//Add the Instructions to the ArrayList
				if(res.getString(2) != null)
				{
					if(!(instructions.contains(res.getString(2))))
					{
						instructions.add(res.getString(2));
					}
				}
				
				//Add the Tips to the ArrayList
				if(res.getString(3) != null)
				{
					if(!(tips.contains(res.getString(3))))
					{
						tips.add(res.getString(3));
					}
				}
				
				//Add the Nutrition information to the ArrayList
				if(res.getRow() == 1)
				{
					if(res.getString(4) != null) nutrition.add("Serving Size: " + res.getString(4)); 
					if(res.getString(5) != null) nutrition.add("Calories: " + res.getString(5)); 
					if(res.getString(6) != null) nutrition.add("Calories from Fat: " + res.getString(6));
					if(res.getString(7) != null) nutrition.add("Total Fat: " + res.getString(7));
					if(res.getString(8) != null) nutrition.add("Saturated Fat: " + res.getString(8));
					if(res.getString(9) != null) nutrition.add("Trans Fat: " + res.getString(9));
					if(res.getString(10) != null) nutrition.add("Cholesterol: " + res.getString(10));
					if(res.getString(11) != null) nutrition.add("Sodium: " + res.getString(11));
					if(res.getString(12) != null) nutrition.add("Carbohydrates: " + res.getString(12));
					if(res.getString(13) != null) nutrition.add("Fiber: " + res.getString(13));
					if(res.getString(14) != null) nutrition.add("Sugars: " + res.getString(14));
					if(res.getString(15) != null) nutrition.add("Protein: " + res.getString(15));
					if(res.getString(16) != null) nutrition.add("Vitamin A: " + res.getString(16));
					if(res.getString(17) != null) nutrition.add("Vitamin C: " + res.getString(17));
					if(res.getString(18) != null) nutrition.add("Calcium: " + res.getString(18));
					if(res.getString(19) != null) nutrition.add("Iron: " + res.getString(19));
				}
			}
			
			//The Betty Crocker Site Condensed their instruction steps to look smaller,
			//But they come across and jumbled and hard to follow.  Here, we take the
			//Instructions and spread them back out for easier reading.
			for (int i = 0; i < instructions.size(); i++)
			{
				instructions.set(i, instructions.get(i).replaceAll("\\. ", ".\n"));
			}
			
			//Set all of the relevent Recipe information to the newly created data
			recipe.setNutrition(nutrition);
			recipe.setIngredients(ingredients);
			recipe.setInstructions(instructions);
			recipe.setTips(tips);
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}

		return recipe;
	}
	
	public int getRandomRecipe()
	{
		ResultSet res = null;
		ArrayList<Integer> recipeIDs = new ArrayList<Integer>();
		
		//First, get an array of all recipes, along with a size to set the range for a random number generator.
		try //Attempt to query the Recipe table for all RecipeIDs. 
		{
			res = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("Select RecipeID from Recipe");
				
			while (res.next()) //increment through the ResultSet, setting the integer of the array to the RecipeID in the ResultSet.
			{
				recipeIDs.add(res.getInt(1));  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		//Next, use the size of the array to create a new Random number generator, and select a random index of the array as our random recipeID.
		Random random = new Random();
		double rndNum = recipeIDs.size() * random.nextDouble();
	       
		return recipeIDs.get((int)rndNum); 
	}
	
	//Returns an ArrayList of skeleton Recipes with just their User Rating set
	public ArrayList<Recipe> getFavorites(int userID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Recipe> recipes = new ArrayList<Recipe>(); //Returns an empty ArrayList if no ratings have been made.
		
		try //Attempt to query the Rating table for records relating to the Username.
		{
			pstmt = conn.prepareStatement("Select recipeID, liked, ease, cost from Rating where UserID = ?");
			pstmt.setInt(1, userID);
			res = pstmt.executeQuery();
            while(res.next()) //If records are found, use the fields to create a Rating object for each, and create a new Recipe to hold each one.
            {
            	//We only want to return recipes that the user has rated 3 or higher satisfaction as a favorite
            	if (res.getInt(2) >= 3)
            	{
            		Rating rating = new Rating();
                	Recipe recipe = new Recipe(res.getInt(1));
                	rating.setUserRating(res.getInt(2), res.getInt(3), res.getInt(4));
                	recipe.setRating(rating);
                	recipes.add(recipe);
            	}
            }
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		//Sort the recipes by rating
		Collections.sort(recipes);
		
		//trim the favorites list down to 100, if necessary
	    //if (recipes.size() >= 100) 
	    //{
	    //	 for (int i = recipes.size(); i <= 100; i--) 
	    //	 {
	 	//        recipes.remove(i);
	 	//    }
	    //}
	    
	   	   
		return recipes;
	}
	
	
	public ArrayList<Recipe> getSuggestions(int userID)
	{
		ArrayList<Recipe> favorites = getFavorites(userID);
		ArrayList<Category> categories = new ArrayList<Category>();
		
		for (Recipe recipe : favorites)
		{
			recipe = getRecipe(recipe);
			
			if (recipe.getRating().displayRating().get(0) >= 4)
			{
				categories.addAll(recipe.getCategoryList());
			}
		}
		
		Map<Integer, Integer> countCategories = new HashMap<Integer, Integer>();
		
		for (Category cat : categories)
		{
			int categoryID = cat.getID();
			
			if (countCategories.containsKey(categoryID))
			{
				countCategories.put(categoryID, countCategories.get(categoryID) + 1);
			}
			else
			{
				countCategories.put(categoryID, 1);
			}
		}
	
		List<Entry<Integer, Integer>> topFiveCategories = findGreatest(countCategories, 5);
		
		ArrayList<Integer> top5CateogryIDs = new ArrayList<Integer>();
		
		for (int i = 0; i < 5; i++)
		{
			top5CateogryIDs.add(topFiveCategories.get(i).getKey());
		}
		
		PreparedStatement pstmt = null;
		ResultSet res = null;
		HashSet<Integer> uniqueRecipeIDs = new HashSet<Integer>();
		
		try // Attempt to query the Rating table for all ratings related to the given recipeID.
		{
			for (Integer categoryID : top5CateogryIDs)
			{				
				pstmt = conn.prepareStatement("Select RecipeID from RecipeCategory where CategoryID = ?");
				pstmt.setInt(1, categoryID);
				res = pstmt.executeQuery();
	
				while (res.next()) 
				{
					uniqueRecipeIDs.add(res.getInt(1));
				}
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		ArrayList<Recipe> uniqueRecipes = new ArrayList<Recipe>();
		
		for (int recipeID : uniqueRecipeIDs)
		{
			Recipe recipe = new Recipe(recipeID);
			//recipe = getRecipe(recipe);
			recipe = getRatings(recipe);
			uniqueRecipes.add(recipe);
		}
		
		Collections.sort(uniqueRecipes);
		
		ArrayList<Recipe> suggestions = new ArrayList<Recipe>();
		
		for (int i = 0; i < 25; i++)
		{
			Recipe recipe = uniqueRecipes.get(i);
			recipe = getRecipe(recipe);
			suggestions.add(recipe);
		}
		
		return suggestions;
	}
	
	//This method credited to Marco13 of StackOverflow
	//http://stackoverflow.com/questions/21465821/how-to-get-5-highest-values-from-a-hashmap
	private static <K, V extends Comparable<? super V>> List<Entry<K, V>> findGreatest(Map<K, V> map, int n)
	{
    Comparator<? super Entry<K, V>> comparator = 
        new Comparator<Entry<K, V>>()
    {
        @Override
        public int compare(Entry<K, V> e0, Entry<K, V> e1)
        {
            V v0 = e0.getValue();
            V v1 = e1.getValue();
            return v0.compareTo(v1);
        }
    };
    PriorityQueue<Entry<K, V>> highest = 
        new PriorityQueue<Entry<K,V>>(n, comparator);
    for (Entry<K, V> entry : map.entrySet())
    {
        highest.offer(entry);
        while (highest.size() > n)
        {
            highest.poll();
        }
    }

    List<Entry<K, V>> result = new ArrayList<Map.Entry<K,V>>();
    while (highest.size() > 0)
    {
        result.add(highest.poll());
    }
    return result;
}
	
	public ArrayList<Recipe> search(String searchTerm)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Category> categories = new ArrayList<Category>();
		HashSet<Integer> uniqueRecipeIDs = new HashSet<Integer>();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		try 
		{
			//Attempt to query the Category table for CategoryIDs relating to the search term. 
			pstmt = conn.prepareStatement("Select CategoryID, Category from Category where Upper(Category) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			while (res.next()) 
			{
				categories.add(new Category(res.getString(2), res.getInt(1)));
			}
			
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
			
			for (Category category : categories)
			{				
				pstmt = conn.prepareStatement("Select RecipeID from RecipeCategory where CategoryID = ?");
				pstmt.setInt(1, category.getID());
				res = pstmt.executeQuery();
	
				while (res.next()) 
				{
					uniqueRecipeIDs.add(res.getInt(1));
				}
			} 
			
			//Attempt to query the Ingredient table for IngredientIDs relating to the search term. Join the RecipeIngredient
		    //table to get the RecipeID of each ingredient.  Distinct constraint makes sure to disclude repeat RecipeIDs.
			pstmt = conn.prepareStatement("Select Distinct RecipeIngredient.RecipeID from RecipeIngredient Inner Join Ingredient "
					+ "on RecipeIngredient.IngredientID = Ingredient.IngredientID where Upper(Ingredient.Ingredient) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();	
			
			while (res.next())
			{
				uniqueRecipeIDs.add(res.getInt(1));
			}
			
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
			
			//Attempt to queryt the Recipe table for Recipe titles that contain the search term
			pstmt = conn.prepareStatement("Select  RecipeID from Recipe Where Upper(Title) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			while (res.next())
			{
				uniqueRecipeIDs.add(res.getInt(1));
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		for (Integer recipeID : uniqueRecipeIDs)
		{
			Recipe recipe = new Recipe(recipeID);
			recipe = getRecipe(recipe);
			recipe = getRatings(recipe);
			recipes.add(recipe);
		}
		
		Collections.sort(recipes);
		
		//trim the search list down to 100, if necessary
	    //if (recipes.size() >= 100) 
	    //{
	    //	 for (int i = recipes.size(); i <= 100; i--) 
	    //	 {
	 	//        recipes.remove(i);
	 	//    }
	    //}
		
		return recipes; 
	}
	
	
	public void rateRecipe(int userID, int recipeID, int liked, int ease, int cost)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		int ratingID = 0;
		
		try //Attempt to query the Rating table for an existing RatingID for the relationship between the given UserID and RecipeID. 
		{
			pstmt = conn.prepareStatement("Select RatingID from Rating where recipeID = ? and userID = ?");
			pstmt.setInt(1, recipeID);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }; //Close the database object for reuse.
            
			 if(res.next()) //If the RatingID exists, update that row
            {
            	ratingID =  res.getInt(1);
            	
            	pstmt = conn.prepareStatement("Update Rating Set Liked = ?, Ease = ?, Cost = ?  where RatingID = ?");
    			pstmt.setInt(1, liked);
    			pstmt.setInt(2, ease);
    			pstmt.setInt(3, cost);
    			pstmt.setInt(4, ratingID);
    			pstmt.executeUpdate();
    			conn.commit();
            }
            else //If the RatingID does not exist, insert a new record into the table to generate a new one.
    		{
    			pstmt = conn.prepareStatement("Insert into Rating (UserID, RecipeID, Liked, Ease, Cost) values (?, ?, ?, ?, ?)");
    			pstmt.setInt(1, userID);
    			pstmt.setInt(2, recipeID);
    			pstmt.setInt(3, liked);
    			pstmt.setInt(4, ease);
    			pstmt.setInt(5, cost);
    			pstmt.executeUpdate();
    		}
		}	
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
	}
			
	//Attempt to close the database connection.  This method should always be called when finished with the object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}

}
