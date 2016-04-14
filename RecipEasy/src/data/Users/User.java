package data.Users;

import java.util.ArrayList;

import data.DatabaseInterface.DBRatingIntf;

public class User 
{
	private int userID;
	private String username;
	private ArrayList<Rating> ratings;
	
	
	public User(int userID, String username)
	{
		this.userID = userID;
		this.username = username;
		
		loadRatings();
	}
	
	public int getUserID()
	{
		return userID;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public ArrayList<Rating> getRatings()
	{
		return ratings;
	}
	
	public void loadRatings()
	{
		DBRatingIntf ratingDB = new DBRatingIntf();
		ratings = ratingDB.getAllByUser(userID);
		ratingDB.close();
	}
}
