package data.Recipes;

import java.util.ArrayList;

import data.DatabaseInterface.DBRatingIntf;
import data.Users.Rating;

public class Recipe implements Comparable<Recipe>
{
	//The ID number of the recipe in the database
	private int recipeID;
	
	//Recipe information
	private BasicInfo basicInfo;
	private Nutrition nutrition;
	private CategoryList categories;
	private IngredientList ingredients;
	private InstructionList instructions;
	private TipList tips;
	private Rating userRating;
	private Rating avgRating;
	private Rating combinedRating;

	public Recipe(int recipeID)
	{
		this.recipeID = recipeID;
		setBasicInfo();
		setAvgRating();
		
		userRating = null;
		nutrition = null;
		categories = null;
		ingredients = null;
		instructions = null;
	}
	public BasicInfo getBasicInfo()
	{
		return basicInfo;
	}
	public int getRecipeID()
	{
		return recipeID;
	}
	
	public Rating getAvgRating()
	{
		return avgRating;
	}
	
	public String getTitle()
	{
		String fix=basicInfo.getTitle();
		return fix;
	}
	
	public String getSummary()
	{
		return basicInfo.getSummary();
	}
	
	public String getPrepTime()
	{
		return basicInfo.getPrepTime();
	}

	public String getTotalTime()
	{
		return basicInfo.getTotalTime();
	}
	
	public String getServings()
	{
		return basicInfo.getServings();
	}
	
	public String getCategories()
	{
		return categories.toString();
	}

	public String getIngredients()
	{
		return ingredients.toString();
	}

	public String getInstructions()
	{
		return instructions.toString();
	}

	public String getTips()
	{
		return tips.toString();
	}

	public String getNutrition()
	{
		return nutrition.toString();
	}
	
	
	public void setBasicInfo() 
	{
		basicInfo = new BasicInfo(recipeID);			
	}
	
	public void setUserRating(Rating rating)
	{
		userRating = rating;
	}
	
	public void setAvgRating()
	{
		
		DBRatingIntf db = new DBRatingIntf();
		ArrayList<Rating> ratings = db.getAllByRecipe(recipeID);
		db.close();
		
		int likedCount = 0;
		int likedSum = 0;
		int easeCount = 0;
		int easeSum = 0;
		int costCount = 0;
		int costSum = 0;
		
		for (Rating rating : ratings)
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
		
		avgRating = new Rating(recipeID);
		avgRating.setRatings(liked, ease, cost);
	}
	
	public void setAllInfo()
	{
		tips = new TipList(recipeID);
		nutrition = new Nutrition(recipeID);
		categories = new CategoryList(recipeID);
		ingredients = new IngredientList(recipeID);
		instructions = new InstructionList(recipeID);
	}
	
	public void setCombinedRating()
	{		
		if (userRating == null)
		{
			combinedRating = avgRating;
		}
		else
		{			
			int liked = userRating.getLiked();
			int ease = userRating.getEase();
			int cost = userRating.getCost();
			
			if (liked == 0)
			{
				liked = avgRating.getLiked();
			}
			
			if (ease == 0)
			{
				ease = avgRating.getEase();
			}
			
			if (cost == 0)
			{
				cost = avgRating.getCost();
			}
			
			combinedRating = new Rating(recipeID);
			combinedRating.setRatings(liked, ease, cost);			
		}
	}

	@Override
	public int compareTo(Recipe other) 
	{
		return this.avgRating.compareTo(other.getAvgRating());
	}
	
	
	@Override
	public boolean equals(Object other) 
	{
		if ( ! (other instanceof Recipe))
		{
			return false;
		}
		
		if (((Recipe) other).getRecipeID() == this.getRecipeID())
		{
			return true;
		}
		
		return false;
		
	}

	@Override
	public String toString() 
	{
		String recipe = "";
		
		recipe += basicInfo.toString() + "\n";
		recipe += categories.toString();
		recipe += ingredients.toString();
		recipe += instructions.toString();
		recipe += tips.toString();
		recipe += nutrition.toString();
		recipe += basicInfo.getAddress();
		recipe += "© 2015 ®/TM General Mills All Rights Reserved";
			
		return  recipe;
	}
	
}
