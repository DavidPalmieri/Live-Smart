package data;

import java.util.ArrayList;

public class Recipe implements Comparable<Recipe>
{
	//The ID number of the recipe in the database
	private int recipeID;
	
	//The general information of the recipe
	private String title;
	private String summary;
	private String prepTime;
	private String totalTime;
	private String servings;
	private String url;
	
	//Ratings object that holds all ratings for this recipe, including average rating and user rating
	private Rating rating;
	
	//Information about the Recipe in which their amount is fluid depending on the given Recipe
	private ArrayList<Category> categories;
	private ArrayList<String> instructions;
	private ArrayList<String> ingredients;
	private ArrayList<String> tips;
	private ArrayList<String> nutrition;
	
	//Set the recipeID and initialize the rest of the instance variables
	public Recipe(int recipeID)
	{
		this.recipeID = recipeID;
		rating = new Rating();
		categories = new ArrayList<Category>();
		instructions = new ArrayList<String>();
		ingredients = new ArrayList<String>();
		tips = new ArrayList<String>();
		nutrition = new ArrayList<String>();
	}
	
	//Set the basic info of the Recipe
	public void setDetails(String title, String summary, String prepTime, String totalTime, String servings, String url)
	{
		this.title = title;
		this.summary = summary;
		this.prepTime = prepTime;
		this.totalTime = totalTime;
		this.servings = servings;
		this.url = url;
	}
	
	
	public void setRating(Rating rating)
	{
		this.rating = rating;
	}
	
	public void setCategories(ArrayList<Category> categories)
	{
		this.categories = categories;
	}

	public void setInstructions(ArrayList<String>instructions)
	{
		this.instructions = instructions;
	}

	public void setIngredients(ArrayList<String> ingredients)
	{
		this.ingredients = ingredients;
	}

	public void setTips(ArrayList<String> tips)
	{
		this.tips = tips;
	}
	
	public void setNutrition(ArrayList<String> nutrition)
	{
		this.nutrition = nutrition;
	}
	
	public int getRecipeID()
	{
		return recipeID;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public String getPrepTime()
	{
		return prepTime;
	}
	
	public String getTotalTime()
	{
		return totalTime;
	}
	
	public String getServings()
	{
		return servings;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public Rating getRating()
	{
		return rating;
	}
	
	public ArrayList<Category> getCategories()
	{
		return categories;
	}
	
	public ArrayList<String> getIngredients()
	{
		return ingredients;
	}
	
	public ArrayList<String> getInstructions()
	{
		return instructions;
	}
	
	public ArrayList<String> getTips()
	{
		return tips;
	}
	
	public ArrayList<String> getNutrition()
	{
		return nutrition;
	}

	@Override
	public int compareTo(Recipe otherRecipe) 
	{
		return this.getRating().compareTo(otherRecipe.getRating());
	}
}