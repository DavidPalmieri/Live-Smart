 ////////////////////////////////////////////////////////////////////////////////////////////////////////+
//																										//
//	Rating: Rating information class.  Stores rating details of a specific Recipe.						//	
//	Author: Chris Costa																					//
//																										//
//	Implements: Comparable - compare Ratings to sort multiple Recipe objects in a Collection			//
//																										//	
//	Nested Class: Rate - Stores an individual rating.													//
//																										//
//	<== Constructors ==>																				//
//																										//	
//		Rating()																						//
//			Creates a new Rating object, initializing all of the instance variables						//
//																										//
//	<== Mutators ==>																					//
//																										//
//		addRating(int liked, int ease, int cost)														//
//			Creates a new Rate object and adds it to the ArrayList of user Ratings for the Recipe		//
//																										//	
//		setUserRating(int liked, int ease, int cost)													//
//			Creates a new Rate representing the Ratings of the Recipe for the current User				//
//																										//
//		setAvgRating()																					//
//			Gets all of the ratings for the Recipe from the ArrayList, and averages them				//
//																										//
//	<== Accessors ==>																					//
//																										//
//		displayRating(): ArrayList<Integer>																//
//			returns either the user's rating of the Recipe if they have rated it, or the average rating	//
//																										//
//	Rate: Nested Class for storing individual ratings													//
//																										//
//	<== Constructors ==>																				//
//																										//
//		Rate(int liked, int ease, int cost)																//
//			Creates a new Rate object, setting the Ratings to the values of the parameters				//
//																										//
//	<== Accessors ==>																					//
//																										//
//		getLiked(): int																					//
//			returns the satisfactionn rating of the recipe (0-5)										//
//																										//
//		getEase(): int																					//
//			returns the ease rating of the recipe (0-5)													//
//																										//
//		getCost(): int																					//
//			returns the cost rating of the recipe (0-5)													//
//																										//
/////////////////////////////////////////////////////////////////////////////////////////////////////////

package data;

import java.util.ArrayList;

public class Rating implements Comparable<Rating>
{
	//An ArrayList of Rate objects representing all of the ratings of this recipe by users
	private ArrayList<Rate> ratings;
	
	//A Rate representing the current User's rating of the containing Recipe
	private Rate userRating;
	
	//A Rate representing the average rating of the containing Recipe
	private Rate avgRating;
	
	//Create a new Rating object, initializing all instance variables
	public Rating()
	{
		userRating = new Rate(0, 0, 0);
		avgRating = new Rate(0, 0, 0);
		ratings = new ArrayList<Rate>();
	}
	
	//Add a new Rate object to the list of all ratings for the Recipe
	public void addRating(int liked, int ease, int cost)
	{
		ratings.add(new Rate(liked,ease,cost));
	}
	
	//Sets the User's rating of the Recipe
	public void setUserRating(int liked, int ease, int cost)
	{
		userRating = new Rate(liked,ease,cost);
	}
	
	//Averages all of the ratings for the Recipe
	public void setAvgRating()
	{
		//Create variables to store information regarding the total amount of ratings for a recipe, and
		//sum of all of those ratings.  Formula: Sum / count = average
		int likedCount = 0;
		int likedSum = 0;
		int easeCount = 0;
		int easeSum = 0;
		int costCount = 0;
		int costSum = 0;
		
		//Increment through the list of Rates, adding to the variables for any value greater than 0
		//(A User is not required to rate all 3 ratings for a recipe.  The other values are therefore
		//left as 0, and not counted for averaging)
		for (Rate rating : ratings)
		{
			if (rating.getLiked() > 0)
			{
				likedCount++;
				likedSum += rating.getLiked();
			}
			
			if (rating.getEase() > 0)
			{
				easeCount++;
				easeSum += rating.getEase();
			}
			
			if (rating.getCost() > 0)
			{
				costCount++;
				costSum += rating.getCost();
			}
		}
		
		//Get the average of each individual rating
		int liked = (int) ((double)likedSum / (double)likedCount);
		int ease = (int) ((double)easeSum / (double)easeCount);
		int cost = (int) ((double)costSum / (double)costCount);
		
		//Set the avgRating for the Recipe
		avgRating = new Rate(liked, ease, cost);
	}
	
	//Returns an ArrayList containing the Ratings of the Recipe
	public ArrayList<Integer> displayRating()
	{
		//If available, use all of the ratings of the recipe to get the average
		setAvgRating();
		
		//Initialize the ArrayList which will hold the ratings
		ArrayList<Integer> displayRating = new ArrayList<Integer>();
		
		//Add the 3 Ratings from the average rating to the ArrayList
		displayRating.add(avgRating.getLiked());
		displayRating.add(avgRating.getEase());
		displayRating.add(avgRating.getCost());
		
		//If the User has rated the Recipe, display the User's rating instead.  If the User
		//has only rated 1 or 2 of details of the rating, then display the rating from the
		//average rating for the piece that the User did not rate
			
		int liked = userRating.getLiked();
		if (liked != 0) displayRating.set(0, liked);
			
		int ease = userRating.getEase();
		if (ease != 0) displayRating.set(1, ease);

		int cost = userRating.getCost();
		if (cost != 0) displayRating.set(2, cost);
		
		//Return the ArrayList containing the 3 ratings
		return displayRating;
	}
	
	//To sort Recipes of a Collection, compare the Ratings of each Recipe.  The Recipe
	//with the higher satisfaction (liked) rating will be placed ahead of the lower
	@Override
	public int compareTo(Rating otherRating) 
	{
		return otherRating.displayRating().get(0) - this.displayRating().get(0);	
	}
	
	//Rate class: stores individual ratings (one per user per recipe)
	private class Rate
	{
		//The three ratings
		int liked;
		int ease;
		int cost;
		
		//Create a new Rate, setting the instance variables to the values of the parameters
		private Rate(int liked, int ease, int cost)
		{
			this.liked = liked;
			this.ease = ease;
			this.cost = cost;
		}
		
		//Get the satisfaction rating
		private int getLiked()
		{
			return liked;
		}
		
		//get the ease rating
		private int getEase()
		{
			return ease;
		}
		
		//get the cost rating
		private int getCost()
		{
			return cost;
		}
	}
}
