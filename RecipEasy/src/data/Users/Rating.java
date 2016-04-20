package data.Users;

public class Rating implements Comparable<Rating>
{
	private int recipeID;
	private int liked;
	private int ease;
	private int cost;
	
	public Rating(int recipeID)
	{
		this.recipeID = recipeID;
	}
	
	public int getrecipeID()
	{
		return recipeID;
	}
	
	public void setRatings(int liked, int ease, int cost)
	{
		this.liked = liked;
		this.ease = ease;
		this.cost = cost;
	}
	
	public int getLiked()
	{
		return liked;
	}
	
	public int getEase()
	{
		return ease;
	}
	
	public int getCost()
	{
		return cost;
	}

	@Override
	public int compareTo(Rating other) 
	{		
		return other.getLiked() - this.liked;
	}
}
