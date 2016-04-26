 ////////////////////////////////////////////////////////////////////////////////////////////////////////+
//																										//
//	DataGrabber: Information retrieval Class. Used to access the database and get information, and to 	//
//		develop objects needed to display information for the User.  Contains algorithms to develop 	//
//		intelligent lists of Recipes																	//
//	Author: Chris Costa																					//
//																										//																								//	
//	<== Constructors ==>																				//
//																										//
//		DataGrabber()																					//
//			Initializes the Connection to the embedded Derby database									//
//																										//
//	<== Functions and Algorithms ==>																	//
//																										//
//		getUserID(String username): int																	//
//			return the UserID of the given username.													//
//																										//
//		getPassword(int userID): String																	//
//			return the encrypted Password of the given Username.										//
//																										//
//		newUser(String username, String password): int													//
//			inserts a new user into the database, and return the userID of the newly created user using //
//			the getUserID(String username) method														//
//																										//
//		getRecipe(Recipe recipe): Recipe																//
//			Take a Recipe in as a parameter, and return the Recipe built with the basic information of 	//
//			the specified recipeID (Title, Summary, Prep Time, Total Time, Servings, URL of the Recipe,	//
//			the Categories that the recipe falls under, and all ratings for the recipe.					//
//																										//
//		getRatings(Recipe recipe): Recipe																//
//			Take a Recipe in as a parameter, and gather all of that Recipe's Ratings					//
//																										//
//		getRecipeDetails(Recipe recipe): Recipe															//
//			Take a Recipe in as a parameter, and return the Recipe built with the rest of the 			//
//			information needed for display.																//
//																										//
//		getRandomRecipe(): int																			//
//			search through the database and find a random recipe, returning the recipeID to be used to 	//
//			create a Recipe for display for the user													//
//																										//
//		getFavorites(int userID): ArrayList<Recipe> 													//
//			return an ArrayList of skeleton Recipes with just their id and User Rating set.  Finds all 	//
//			recipes user rated satisfaction 3 or greater, to be considered their "favorite" Recipes.	//
//			The Recipes are sorted by that User's Rating, highest first									//
//																										//
//		 getSuggestions(int userID): ArrayList<Recipe> 													//
//			return an ArrayList of suggested recipes, based off of previous rating history				//
//			The suggestions algorithm is broken into two parts: categorical, and similar interest		//
//			The first part is the categorical suggestion algorithm;										//
//			It is based off of the idea that if a user likes more recipes from a specific category, 	//
//			they are more likely to like recipes from that same category								//
//																										//
//		search(String searchTerm): ArrayList<Recipe>													//
//			return an ArrayList of Recipes that contain the search term either in the title, in the 	//
//			category, or as an ingredient																//
//																										//
//		rateRecipe(int userID, int recipeID, int liked, int ease, int cost)								//
//			either create a rating for the recipe selected by the user, or update an existing rating 	//
//			between the two.																			//
//																										//
//		findGreatest(Map<K, V> map, int n): <K, V extends Comparable<? super V>> List<Entry<K, V>>		//
//			used by the getSuggestions(Recipe recipe) method to find the top 5 highest values from a 	//
//			Map which contains an occurence rate as the value, thereby returning a list of the top 5 	//
//			most often occuring keys of the map															//
//																										//
//		close()																							//
//			closes the database connection.  This method should always be called when finished with the //
//			DataGrabber object.																			//
//																										//
/////////////////////////////////////////////////////////////////////////////////////////////////////////

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
	//The SQL Connection object to provide a connection to the embedded database
	Connection conn;
	
	//The construction initializes the connection to the database
	public DataGrabber()
	{
		try  //Attempt to open the connection to the database
		{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	//return the UserID of the given username.
	public int getUserID(String username)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//userIDs in the base are any integer greater than 0.  Using 0 as a default allows for
		//error handling in the case that a value is not returned from the query
		int userID = 0;
		
		try //Attempt to query the Users table for records relating to the Username 
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
		
		return userID;  //If 0 is returned, it means no userID was found relating to the username given
	}
	
	//return the encrypted Password of the given Username.
	public String getPassword(int userID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//Initialize the String that will be returned containing the encrypted password
		String password = ""; 
		
		try //Attempt to query the Users table for records relating to the Username 
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
	
	//inserts a new user into the database, and return the userID of the newly created user.
	public int newUser(String username, String password)
	{
		PreparedStatement pstmt = null;
		
		try //Attempt to insert the information into the table
		{
			pstmt = conn.prepareStatement("insert into Users (username, password) values (?, ?)");
			pstmt.setString(1, username);
			pstmt.setString(2, password);  //This is an encrypted String that can be checked against a plaintext String later
			pstmt.executeUpdate();
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //close all database objects
		{
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		//use the getUserID method to return the newly generated userID.
		return getUserID(username);
	}
	
	//return a Recipe built with the basic information of the specified recipeID (Title, Summary, Prep Time, Total
	//Time, Servings, URL of the Recipe, the Categories that the recipe falls under, and all ratings for the recipe.
	public Recipe getRecipe(Recipe recipe) 
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//get the recipeID of the recipe being passed in
		int recipeID = recipe.getRecipeID();
		//Initialize a new rating object
		Rating rating = new Rating();
		//Initialize an ArrayList to hold all related categories
		ArrayList<Category> categories = new ArrayList<Category>();

		try // Attempt to query the database for the basic recipe information relating to the specified recipeID.
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
				if(res.getRow() == 1)  //Using the first row, fill in all of the standard information
				{
					//Get the information, and update the recipe
					recipe.setDetails(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
					rating.setUserRating(res.getInt(9), res.getInt(10), res.getInt(11));
					recipe.setRating(rating);
					
					//add the category to the ArrayList
					categories.add(new Category(res.getString(7), res.getInt(8)));
				}
				else  //For the rest of the rows, continue adding the categories
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

		//set the categories of the recipe, and then return the updated recipe
		recipe.setCategories(categories);
		return recipe;
	}
	
	//find all ratings for a given recipe, and use the rating object to store them
	public Recipe getRatings(Recipe recipe)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//get the recipeID of the recipe being passed in
		int recipeID = recipe.getRecipeID();
		//Get the rating from the recipe, which is either empty, or contains the user rating already implemented
		Rating rating = recipe.getRating();

		try // Attempt to query the Rating table for all ratings related to the given recipeID.
		{
			pstmt = conn.prepareStatement("Select Liked, Ease, Cost from Rating where recipeID = ? ");
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
			
			while (res.next())  //for each record, add the returned rating to the rating object
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
		
		//update the recipe with the updated rating object and return it
		recipe.setRating(rating);
		return recipe;	
	}
	
	//return a Recipe built with the rest of the information needed for display.
	public Recipe getRecipeDetails(Recipe recipe) 
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//get the recipeID of the recipe being passed in
		int recipeID = recipe.getRecipeID();
		
		//Initialize the ArrayLists that will hold the ingredients, instructions, tips, and nutrition information.
		ArrayList<String> ingredients = new ArrayList<String>();
		ArrayList<String> instructions = new ArrayList<String>();
		ArrayList<String> tips = new ArrayList<String>();
		ArrayList<String> nutrition = new ArrayList<String>();

		try // Attempt to query the database for the rest of the recipe information relating to the specified recipeID.
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
				if(res.getString(1) != null)  //If the record returned isn't null
				{
					if(!(ingredients.contains(res.getString(1))))  //And it's not a duplicate record
					{
						ingredients.add(res.getString(1));
					}
				}
				
				//Add the Instructions to the ArrayList
				if(res.getString(2) != null)  //If the record returned isn't null
				{
					if(!(instructions.contains(res.getString(2))))  //And it's not a duplicate record
					{
						instructions.add(res.getString(2));
					}
				}
				
				//Add the Tips to the ArrayList
				if(res.getString(3) != null)  //If the record returned isn't null
				{
					if(!(tips.contains(res.getString(3))))  //And it's not a duplicate record
					{
						tips.add(res.getString(3));
					}
				}
				
				//Add the Nutrition information to the ArrayList using the first row returned
				if(res.getRow() == 1)
				{
					//If the record is empty, do not add that value to the nutrition ArrayList
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
			
			//The Betty Crocker Site Condensed their instruction steps to seem smaller,
			//But they come across as jumbled and hard to follow.  Here, we take the
			//Instructions and spread them back out for easier reading.
			for (int i = 0; i < instructions.size(); i++)
			{
				instructions.set(i, instructions.get(i).replaceAll("\\. ", ".\n"));
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		//Set all of the relevent Recipe information to the newly created data and return the recipe
		recipe.setNutrition(nutrition);
		recipe.setIngredients(ingredients);
		recipe.setInstructions(instructions);
		recipe.setTips(tips);

		return recipe;
	}
	
	//search through the database and find a random recipe, returning it for the user
	public int getRandomRecipe()
	{
		ResultSet res = null;
		//Initialize an ArrayList that will hold all of the recipe's recipeIDs
		ArrayList<Integer> recipeIDs = new ArrayList<Integer>();
		
		try //Attempt to query the Recipe table for all RecipeIDs. 
		{
			res = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("Select RecipeID from Recipe");
				
			while (res.next()) //Add each recipeID to the ArrayList
			{
				recipeIDs.add(res.getInt(1));
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		//Next, generate a random number
		Random n = new Random();
		//Multiply the random number (0 <= n < 1) with the size of the ArrayList
		//The ArrayList size is 1 greater than the last index of the ArrayList
		double rndNum = recipeIDs.size() * n.nextDouble();
	    //The random number ends up being inclusive of the first index of the ArrayList, and exclusive of
		//anything larger than the last index of the ArrayList
		int rndRecipeID = recipeIDs.get((int)rndNum);
		//return the recipeID found at the ArrayList index equal to the random number
		return rndRecipeID; 
	}
	
	//return an ArrayList of skeleton Recipes with just their id and User Rating set.  Finds all recipes user rated satisfaction 3 or greater
	public ArrayList<Recipe> getFavorites(int userID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//Initialize an ArrayList that will hold the barebones recipes that the given user has rated satisfaction 3 or higher
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
            		//Create new rating and recipe objects for each favorite found
            		Rating rating = new Rating();
                	Recipe recipe = new Recipe(res.getInt(1));
                	//set the returned rating values as the user rating, and add it to the recipe
                	rating.setUserRating(res.getInt(2), res.getInt(3), res.getInt(4));
                	recipe.setRating(rating);
                	//add the recipe to the list of favorite recipes
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
		
		//sort the recipes, in order of rating, descending, and then return them
		Collections.sort(recipes); 
		return recipes;
	}
	
	//return an ArrayList of suggested recipes, based off of previous rating history
	public ArrayList<Recipe> getSuggestions(int userID)
	{
		//First, get the favorites of the user (recipes rated 3 or higher)
		ArrayList<Recipe> favorites = getFavorites(userID);
		//Initialize an ArrayList to hold categories
		ArrayList<Category> categories = new ArrayList<Category>();
		
		//Condense the favorites list down to just the highest rated recipes, satisfaction of 4 or higher
		for (Recipe recipe : favorites)
		{
			recipe = getRecipe(recipe);
			
			if (recipe.getRating().displayRating().get(0) >= 4)
			{
				//Add all of the categories of these recipes to the ArrayList of categories, duplicates included
				categories.addAll(recipe.getCategoryList());
			}
		}
		
		//Now create a HashMap for counting the occurence of each category in the ArrayList of categories
		Map<Integer, Integer> countCategories = new HashMap<Integer, Integer>();
		
		//Traverse the ArrayList of categories
		for (Category cat : categories)
		{
			int categoryID = cat.getID();
			
			//If the HashMap does not yet contain this category, add it and set its value (occurence) to 1
			if (!(countCategories.containsKey(categoryID)))
			{
				countCategories.put(categoryID, 1);
			}
			//Otherwise, get the previous occurence of that category, incrememnt it, and update it
			else
			{
				int occurence = countCategories.get(categoryID) + 1;
				countCategories.replace(categoryID, occurence);
			}
		}
	
		//Use the helper method to find the top 5 occuring categories of the HashMap
		List<Entry<Integer, Integer>> topFiveCategories = findGreatest(countCategories, 5);
		
		//Initialize an ArrayList that will hold the categoryIDs of the highest occuring categories
		ArrayList<Integer> top5CateogryIDs = new ArrayList<Integer>();
		
		//Add the categoryIDs to the new ArrayList
		for (int i = 0; i < 5; i++)
		{
			top5CateogryIDs.add(topFiveCategories.get(i).getKey());
		}
		
		//Now get a list of all of the recipes in each of the top 5 categories
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//Initialize a HashSet of recipeIDs that will act to disregard duplicates
		HashSet<Integer> uniqueRecipeIDs = new HashSet<Integer>();
		
		try  //Attempt to query the database for all recipes in each of the categories
		{
			for (Integer categoryID : top5CateogryIDs)
			{				
				pstmt = conn.prepareStatement("Select RecipeID from RecipeCategory where CategoryID = ?");
				pstmt.setInt(1, categoryID);
				res = pstmt.executeQuery();
	
				while (res.next()) //Add the recipes to the HashSet
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
		
		//Now, initialize an ArrayList that will hold all of the categorically searched recipes
		ArrayList<Recipe> uniqueRecipes = new ArrayList<Recipe>();
		
		//Increment through the recipeIDs, initializing new recipes with rating information relating to the
		//recipeIDs and add them to the list
		for (int recipeID : uniqueRecipeIDs)
		{
			Recipe recipe = new Recipe(recipeID);
			//recipe = getRecipe(recipe);
			recipe = getRatings(recipe);
			uniqueRecipes.add(recipe);
		}
		
		//Sort all of the recipe by rating
		Collections.sort(uniqueRecipes);
		
		//Now we initialize the ArrayList that will hold the highest rated categorically suggested recipes
		ArrayList<Recipe> suggestions = new ArrayList<Recipe>();
		
		//For the top 25 highest rated recipes, get their basic information, and add them to the final list
		for (int i = 0; i < 25; i++)
		{
			Recipe recipe = uniqueRecipes.get(i);
			recipe = getRecipe(recipe);
			suggestions.add(recipe);
		}
		
		
		
		//Return the ArrayList of suggested recipes
		return suggestions;
	}
	
	//return an ArrayList of Recipes that contain the search term either in the title, in the category, or as an ingredient
	public ArrayList<Recipe> search(String searchTerm)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//Initialize an ArrayList that will hold the categories that relate to the search term
		ArrayList<Category> categories = new ArrayList<Category>();
		//Initialize a HashSet which will hold recipeID Integers of each matching recipe, while discluding duplicates
		HashSet<Integer> uniqueRecipeIDs = new HashSet<Integer>();
		//Initialize the final ArrayList which will hold each recipe that relates to the search term
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		try 
		{
			//Attempt to query the Category table for CategoryIDs relating to the search term. 
			pstmt = conn.prepareStatement("Select CategoryID, Category from Category where Upper(Category) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			while (res.next())  //add each of the matching categories to an ArrayList 
			{
				categories.add(new Category(res.getString(2), res.getInt(1)));
			}
			
        	//Close the database objects for proper reusability
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
			
		    //for each category that was found to match the search term, add the recipes' recipeIDs that fall under that
		    //category to a HashSet containing unique Integers (recipeIDs)
			for (Category category : categories)
			{				
				pstmt = conn.prepareStatement("Select RecipeID from RecipeCategory where CategoryID = ?");
				pstmt.setInt(1, category.getID());
				res = pstmt.executeQuery();
	
				while (res.next())  //for each contained recipe, add the recipeID to the HashSet 
				{
					uniqueRecipeIDs.add(res.getInt(1));
				}
			} 
			
			//Attempt to query the Ingredient table for IngredientIDs relating to the search term.  Using the Distinct constraint
			//keeps from returning a second instance of a recipe which holds 2 ingredients that match the search term
			pstmt = conn.prepareStatement("Select Distinct RecipeIngredient.RecipeID from RecipeIngredient Inner Join Ingredient "
					+ "on RecipeIngredient.IngredientID = Ingredient.IngredientID where Upper(Ingredient.Ingredient) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();	
			
			while (res.next())  //for each recipe that contains an ingredient that matches the search term, add it's recipeID to the HashSet
			{
				uniqueRecipeIDs.add(res.getInt(1));
			}

        	//Close the database objects for proper reusability
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
			
			//Attempt to query the Recipe table for Recipe titles that contain the search term
			pstmt = conn.prepareStatement("Select  RecipeID from Recipe Where Upper(Title) Like Upper(?)");
			pstmt.setString(1, "%" + searchTerm + "%");
			res = pstmt.executeQuery();
			
			while (res.next())  //Add each matching recipe's recipeID to the HashSet
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
		
		//Now, for every recipeID held in the HashSet, create a recipe object, get and set the basic information for the recipe,
		//get and set the ratings for the recipe, and add the recipe to the ArrayList of matching recipes
		for (Integer recipeID : uniqueRecipeIDs)
		{
			Recipe recipe = new Recipe(recipeID);
			recipe = getRecipe(recipe);
			recipe = getRatings(recipe);
			recipes.add(recipe);
		}
		
		//sort the recipes, in order of rating, descending, and then return them
		Collections.sort(recipes);		
		return recipes; 
	}
	
	//either create a rating for the recipe selected by the user, or update an existing rating between the two.
	public void rateRecipe(int userID, int recipeID, int liked, int ease, int cost)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		//Initialize an integer that will contain the id of the specific rating of the recipe by the specified user
		int ratingID = 0;
		
		try //Attempt to query the Rating table for an existing RatingID for the relationship between the given UserID and RecipeID. 
		{
			pstmt = conn.prepareStatement("Select RatingID from Rating where recipeID = ? and userID = ?");
			pstmt.setInt(1, recipeID);
			pstmt.setInt(2, userID);
			res = pstmt.executeQuery();
			
			 if(res.next()) //If the RatingID exists, update that row with the user's ratings
			 {
				 //set the ratingID to be used in the following update statement
            	ratingID =  res.getInt(1);
            	
            	//Close the database objects for proper reusability
            	try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }; 
	            
            	//update the records with the new values
            	pstmt = conn.prepareStatement("Update Rating Set Liked = ?, Ease = ?, Cost = ?  where RatingID = ?");
    			pstmt.setInt(1, liked);
    			pstmt.setInt(2, ease);
    			pstmt.setInt(3, cost);
    			pstmt.setInt(4, ratingID);
    			pstmt.executeUpdate();
    			conn.commit();
            }
            else //If the RatingID does not exist, insert a new record into the table and set the rating's values.
    		{
            	//Close the database objects for proper reusability
            	try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
                
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
	
	//This method credited to Marco13 of StackOverflow
	//http://stackoverflow.com/questions/21465821/how-to-get-5-highest-values-from-a-hashmap
	//used in this application to find the top 5 highest values from a Map which contains an occurence rate as the value,
	//Thereby returning a list of the top 5 most often occuring keys of the map
	private  <K, V extends Comparable<? super V>> List<Entry<K, V>> findGreatest(Map<K, V> map, int n)
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
			
	//closes the database connection.  This method should always be called when finished with the DataGrabber object.
	public void close()
	{
		try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
	}
}
