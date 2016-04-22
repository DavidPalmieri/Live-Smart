package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import data.DatabaseInterface.DBCategoryIntf;
import data.DatabaseInterface.DBIngredientIntf;
import data.DatabaseInterface.DBRatingIntf;
import data.DatabaseInterface.DBRecipeIntf;
import data.DatabaseInterface.DBUsersIntf;
import data.Recipes.Category;
import data.Recipes.Recipe;
import data.Users.Rating;
import data.Users.User;

public class DataInterface 
{
	
	public DataInterface()
	{
		
	}
	
	public int getUserID(String username)
	{
		DBUsersIntf db = new DBUsersIntf();
		int userID = db.getUserID(username);
		db.close();
		return userID;  // returns 0 if no user found
	}
	
	public String getPassword(String username)
	{
		DBUsersIntf db = new DBUsersIntf();
		String password = db.getPassword(username);
		db.close();
		return password;
	}
	
	public User getUser(int userID, String username)
	{
		User user = new User(userID, username);
		return user;
				
	}
	
	public void registerAccount(String username, String encryptedPassword)
	{
		DBUsersIntf db = new DBUsersIntf();
		db.newUser(username, encryptedPassword);
		db.close();
	}
	
	public ArrayList<Recipe> getFavorites(User user)
	{
		ArrayList<Recipe> favorites = new ArrayList<Recipe>();
		ArrayList<Rating> ratings = user.getRatings();
		Collections.sort(ratings);
		
		for (Rating rating : ratings)
		{
			int recipeID = rating.getrecipeID();
			favorites.add(new Recipe(recipeID));		
		}
		
		return favorites;
	}
	
	public ArrayList<Recipe> simpleSearch(String searchTerm)
	{
		
		ArrayList<Recipe> allRecipes = new ArrayList<Recipe>();
		
		DBCategoryIntf dbCat = new DBCategoryIntf();
		allRecipes.addAll(dbCat.search(searchTerm));
		dbCat.close();
		
		DBIngredientIntf dbIng = new DBIngredientIntf();
		allRecipes.addAll(dbIng.search(searchTerm));
		dbIng.close();
		
		DBRecipeIntf dbRec = new DBRecipeIntf();
		allRecipes.addAll(dbRec.search(searchTerm));
		dbRec.close();
		
		HashMap<Integer, Recipe> uniqueRecipes = new HashMap<Integer, Recipe>();
		
		for (Recipe recipe : allRecipes)
		{
			uniqueRecipes.put(recipe.getRecipeID(), recipe);
		}
		
		ArrayList<Recipe> recipes = new ArrayList<Recipe>(uniqueRecipes.values());
		Collections.sort(recipes);
		
		return recipes;
	}
	
	public int selectRecipe(int userID, Recipe recipe)
	{
		DBRatingIntf dbRat = new DBRatingIntf();
		int ratingID = dbRat.createRating(userID, recipe.getRecipeID());
		dbRat.close();
		return ratingID;
	}
	
	public void rateRecipe(int userID, int recipeID, int ratingID, int liked, int cost, int ease)
	{
		DBRatingIntf dbRat = new DBRatingIntf();
		dbRat.updateRating(ratingID, liked, ease, cost);
		dbRat.close();
	}
}
