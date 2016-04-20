package gui;

import java.util.ArrayList;
import java.util.Collections;

import data.DatabaseInterface.DBUsersIntf;
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
	

	

}
