package data;

public class Rating 
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
}
