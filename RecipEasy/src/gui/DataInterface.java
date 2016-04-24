package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import data.Category;
import data.CategoryList;
import data.Rating;
import data.Recipe;
import data.User;
import data.DatabaseInterface.DBCategoryIntf;
import data.DatabaseInterface.DBIngredientIntf;
import data.DatabaseInterface.DBRatingIntf;
import data.DatabaseInterface.DBRecipeIntf;
import data.DatabaseInterface.DBUsersIntf;

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
	
	public ArrayList<Rating> getFavoriteRatings(User user)
	{
		ArrayList<Rating> ratings = user.getRatings();
		Collections.sort(ratings);
		
		return ratings;
	}
	
	public ArrayList<Recipe> getFavoriteRecipes(ArrayList<Rating> ratings)
	{
		ArrayList<Recipe> favorites = new ArrayList<Recipe>();
		
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
	
	public ArrayList<Recipe> getSuggestedRecipes(ArrayList<Rating> favorites)
	{
		ArrayList<Recipe> topFavorites = new ArrayList<Recipe>();
		
		for (Rating r : favorites)
		{
			if (r.getLiked() >= 4)
			{
				int recipeID = r.getrecipeID();
				topFavorites.add(new Recipe(recipeID));	
			}
		}
		
		Map<Integer, Integer> countCategories = new HashMap<Integer, Integer>();
		
		for (Recipe r : topFavorites)
		{
			r.setCategories();
			CategoryList c = r.getCategoryList();
			ArrayList<Category> cats = c.getCategories();
			
			for (Category cat : cats)
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
		}
		
		 List<Entry<Integer, Integer>> top = findGreatest(countCategories, 5);
		 
		 ArrayList<Integer> top5 = new ArrayList<Integer>();
		 top5.add(top.get(0).getKey());
		 top5.add(top.get(1).getKey());
		 top5.add(top.get(2).getKey());
		 top5.add(top.get(3).getKey());
		 top5.add(top.get(4).getKey());
			
		 ArrayList<Recipe> topRecipes = new ArrayList<Recipe>();

		 DBCategoryIntf dbCat = new DBCategoryIntf();
		 
		 for (Integer catID : top5)
		 {
			 ArrayList<Recipe> tempRecipes = dbCat.getRecipes(catID);
			 topRecipes.add(tempRecipes.get(0));
			 topRecipes.add(tempRecipes.get(1));
			 topRecipes.add(tempRecipes.get(2));
			 topRecipes.add(tempRecipes.get(3));
			 topRecipes.add(tempRecipes.get(4));
		 }
		
		 dbCat.close();
		 
		 HashMap<Integer, Recipe> uniqueRecipes = new HashMap<Integer, Recipe>();
			
			for (Recipe recipe : topRecipes)
			{
				uniqueRecipes.put(recipe.getRecipeID(), recipe);
			}
			
			topRecipes = new ArrayList<Recipe>(uniqueRecipes.values());
			Collections.sort(topRecipes);
		 
		return topRecipes;
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
	
	
	public int selectRecipe(int userID, Recipe recipe)
	{
		DBRatingIntf dbRat = new DBRatingIntf();
		int ratingID = dbRat.createRating(userID, recipe.getRecipeID());
		dbRat.close();
		return ratingID;
	}
	
	public Recipe randomRecipe()
	{
		DBRecipeIntf queryDB = new DBRecipeIntf();
      	Recipe recipe = new Recipe(queryDB.randomRecipe());
      	queryDB.close();
      	return recipe;
	}
	
	public void rateRecipe(int ratingID, int liked, int cost, int ease)
	{
		DBRatingIntf dbRat = new DBRatingIntf();
		dbRat.updateRating(ratingID, liked, ease, cost);
		dbRat.close();
	}
	
	
}
