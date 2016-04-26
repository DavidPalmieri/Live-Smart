 ////////////////////////////////////////////////////////////////////////////////////////////////////////+
//																										//
//	Recipe: Recipe information Class.  Stores all details of a specific Recipe.							//	
//	Author: Chris Costa																					//
//																										//
//	Implements: Comparable - compare Ratings of the Recipes to sort multiple Recipe objects in a 		//
//		Collection																						//
//																										//	
//	<== Constructors ==>																				//
//																										//
//		Recipe(int recipeID)																			//
//			Creattes a new Recipe object, setting the recipeID to the value of the parameter, and 		//
//			initializing all of the other instance variables											//
//																										//
//	<== Mutators ==>																					//
//																										//
//		setDetails(String title, String summary, String prepTime, String totalTime, String servings, 	//
//			String url)																					//
//			Sets the basic details of the recipe, used for displaying the information useful for when	//
//			the User is still deciding on which recipe to choose										//
//																										//
//		setRating(Rating rating)																		//
//			Sets the Ratings object for this recipe.  Could contain either the Ratings of the current 	//
//			User, the average Ratings of the Recipe, or both.  Ratings object is used to buil favorite	//
//			Recipes list, Suggestions list, and is used for supporting Recipes in a Collection, sorting //
//			by providing the highest rated first														//
//																										//
//		setCategories(ArrayList<Category> categories)													//
//			Sets the Categories of the Recipe.  Most Recipes fall under several Categories, which are 	//
//			used to loosely relate Recipes with one another when building the Suggestions list			//
//																										//
//		setInstructions(ArrayList<String>instructions)													//
//			Sets the list of instructions for the recipe												//
//																										//
//		setIngredients(ArrayList<String> ingredients)													//
//			Sets the list of ingredients for the Recipe													//
//																										//
//		setTips(ArrayList<String> tips)																	//
//			Sets the list of tips for the Recipe														//
//																										//
//		setNutrition(ArrayList<String> nutrition)														//
//			Sets the list of nutrition information for the Recipe										//
//																										//
//	<== Accessors ==>																					//
//																										//
//		getRecipeID(): int																				//
//			Returns the ID of the Recipe used in the database											//
//																										//
//		 getTitle(): String																				//
//			returns the title of the Recipe																//
//																										//
//		getSummary(): String																			//
//			returns the summary of the Recipe															//
//																										//
//		getPrepTime(): String																			//
//			returns the prep time of the recipe in form ## Hours and ## Minutes							//
//																										//
//		getTotalTime(): String																			//
//			returns the total time of the recipe in form ## Hours and ## Minutes						//
//																										//
//		getServings(): String																			//
//			returns the total servings of the Recipe													//
//																										//
//		getUrl(): String																				//
//			returns the exact URL of which all of this Recipe information was gathered from				//
//																										//
//		getRating(): Rating																				//
//			returns the Rating object containing ratings made for this Recipe							//
//																										//
//		getCategoryList(): ArrayList<Category>															//
//			returns an ArrayList of the Categories, used when building the Suggestions list				//
//																										//
//		getCategories(): String																			//
//			returns a concatenated String containing all of the Category names for display				//
//																										//
//		getIngredients(): String																		//
//			returns a concatenated String containing all of the ingredients for display					//
//																										//
//		getInstructions(): String																		//
//			returns a concatenated String containing all of the instructions for display				//
//																										//
//		getTips(): String																				//
//			returns a concatenated String containing all of the tips for display						//
//																										//
//		getNutrition(): String																			//
//			returns a concatenated String containing all of the nutrition information for display		//
//																										//
/////////////////////////////////////////////////////////////////////////////////////////////////////////


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
	
	//Set the Rating of the recipe. could contain the user's rating, average rating, or both
	public void setRating(Rating rating)
	{
		this.rating = rating;
	}
	
	//Set the categories of the recipe.  Most Recipes fall under several different categories
	public void setCategories(ArrayList<Category> categories)
	{
		this.categories = categories;
	}

	//Sets the list of instructions for the Recipe
	public void setInstructions(ArrayList<String>instructions)
	{
		this.instructions = instructions;
	}

	//Sets the list of ingredients for the Recipe
	public void setIngredients(ArrayList<String> ingredients)
	{
		this.ingredients = ingredients;
	}

	//Sets the list of tips for the Recipe
	public void setTips(ArrayList<String> tips)
	{
		this.tips = tips;
	}
	
	//Sets the list of nutrition information for the Recipe
	public void setNutrition(ArrayList<String> nutrition)
	{
		this.nutrition = nutrition;
	}
	
	//Returns the ID of the Recipe used in the database	
	public int getRecipeID()
	{
		return recipeID;
	}
	
	//returns the title of the Recipe
	public String getTitle()
	{
		return title;
	}
	
	//returns the summary of the Recipe
	public String getSummary()
	{
		return summary;
	}
	
	//returns the prep time of the recipe in form ## Hours and ## Minutes	
	public String getPrepTime()
	{
		return prepTime;
	}
	
	//returns the total time of the recipe in form ## Hours and ## Minutes
	public String getTotalTime()
	{
		return totalTime;
	}
	
	//returns the total servings of the Recipe
	public String getServings()
	{
		return servings;
	}
	
	//returns the exact URL of which all of this Recipe information was gathered from
	public String getUrl()
	{
		return url;
	}
	
	//returns the Rating object containing ratings made for this Recipe
	public Rating getRating()
	{
		return rating;
	}
	
	//returns an ArrayList of the Categories, used when building the Suggestions list
	public ArrayList<Category> getCategoryList()
	{
		return categories;
	}
	
	//returns a concatenated String containing all of the Category names for display
	public String getCategories()
	{
		String categoryText = "Categories: ";
		
		for (int i = 0; i < categories.size(); i++)
      	{
      		if (i < categories.size() - 1)
      		{
      			categoryText += categories.get(i).getName() + ", ";
      		}
      		else
      		{
      			categoryText += categories.get(i).getName();
      		}
      	}
		
		return categoryText;
	}
	
	//returns a concatenated String containing all of the ingredients for display	
	public String getIngredients()
	{
      	String ingredientText = "";
      	
    	for (int i = 0; i < ingredients.size(); i++)
      	{
      		if (i < ingredients.size() - 1)
      		{
      			ingredientText += ingredients.get(i) + "\n";
      		}
      		else
      		{
      			ingredientText += ingredients.get(i);
      		}
      	}
    	
    	return ingredientText;
	}
	
	//returns a concatenated String containing all of the instructions for display
	public String getInstructions()
	{
		String instructionText = "";

    	for (int i = 0; i < instructions.size(); i++)
      	{
      		if (i < instructions.size() - 1)
      		{
      			instructionText += instructions.get(i) + "\n";
      		}
      		else
      		{
      			instructionText += instructions.get(i);
      		}
      	}
		
		return instructionText;
	}
	
	//returns a concatenated String containing all of the tips for display
	public String getTips()
	{
		String tipText = "";

    	for (int i = 0; i < tips.size(); i++)
      	{
      		if (i < instructions.size() - 1)
      		{
      			tipText += tips.get(i) + "\n";
      		}
      		else
      		{
      			tipText += tips.get(i);
      		}
      	}

		return tipText;
	}
	
	//returns a concatenated String containing all of the nutrition information for display
	public String getNutrition()
	{
		String nutritionText = "";
      	
    	for (int i = 0; i < nutrition.size(); i++)
      	{
      		if (i < nutrition.size() - 1)
      		{
      			nutritionText += nutrition.get(i) + "\n";
      		}
      		else
      		{
      			nutritionText += nutrition.get(i);
      		}
      	}
    	
    	return nutritionText;
	}

	@Override
	public int compareTo(Recipe otherRecipe) 
	{
		return this.getRating().compareTo(otherRecipe.getRating());
	}
}