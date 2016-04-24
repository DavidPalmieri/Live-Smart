package data;

import java.util.ArrayList;

public class Rating implements Comparable<Rating>
{
	private ArrayList<Rate> ratings;
	private Rate userRating;
	private Rate avgRating;
	
	public Rating()
	{
		userRating = new Rate(0, 0, 0);
		avgRating = new Rate(0, 0, 0);
		ratings = new ArrayList<Rate>();
	}
	
	public void addRating(int liked, int ease, int cost)
	{
		ratings.add(new Rate(liked,ease,cost));
	}
	
	public void setUserRating(int liked, int ease, int cost)
	{
		userRating = new Rate(liked,ease,cost);
	}
	
	public void setAvgRating()
	{
		int likedCount = 0;
		int likedSum = 0;
		int easeCount = 0;
		int easeSum = 0;
		int costCount = 0;
		int costSum = 0;
		
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
		
		int liked = (int) ((double)likedSum / (double)likedCount);
		int ease = (int) ((double)easeSum / (double)easeCount);
		int cost = (int) ((double)costSum / (double)costCount);
		
		avgRating = new Rate(liked, ease, cost);
	}
	
	public ArrayList<Integer> displayRating()
	{
		setAvgRating();
		
		ArrayList<Integer> displayRating = new ArrayList<Integer>();
		
		displayRating.add(avgRating.getLiked());
		displayRating.add(avgRating.getEase());
		displayRating.add(avgRating.getCost());
		
		if (userRating != null)
		{		
			int liked = userRating.getLiked();
			if (liked != 0) displayRating.set(0, liked);
			
			int ease = userRating.getEase();
			if (ease != 0) displayRating.set(1, ease);

			int cost = userRating.getCost();
			if (cost != 0) displayRating.set(2, cost);
		}	
		
		return displayRating;
	}
	
	
	@Override
	public int compareTo(Rating otherRating) 
	{
		return otherRating.displayRating().get(0) - this.displayRating().get(0);	
	}
	
	private class Rate
	{
		int liked;
		int ease;
		int cost;
		
		private Rate(int liked, int ease, int cost)
		{
			this.liked = liked;
			this.ease = ease;
			this.cost = cost;
		}
		
		private int getLiked()
		{
			return liked;
		}
		
		private int getEase()
		{
			return ease;
		}
		
		private int getCost()
		{
			return cost;
		}
	}
}
