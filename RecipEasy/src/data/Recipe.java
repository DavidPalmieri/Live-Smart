package data;

import java.util.ArrayList;

public class Recipe 
{
	//The ID number of the recipe in the database
	private Integer recipeID;
	
	//Recipe Details
	private String title;
	private String address;
	private String category;
	private String picAddress;
	private String prepTime;
	private String totalTime;
	private String servings;
	private String summary;
	private String trademark;
	
	//Serving Size
	private String servingSize;
	
	//Nutrition Information
	//	Primitives not used to allow for Null values
	private Integer calories;
	private Integer calFat;
	private Integer totFat;
	private Integer satFat;
	private Integer transFat;
	private Integer cholesterol;
	private Integer sodium;
	private Integer carbs;
	private Integer fiber;
	private Integer sugar;
	private Integer protein;
	private Integer vitA;
	private Integer vitC;
	private Integer calcium;
	private Integer iron;
	
	//Ingredients, Directions, and Tips
	private ArrayList<String> ingredients;
	private ArrayList<String> directions;
	private ArrayList<String> tips;
	
	/*Recipe object
	 * Initialize a new Recipe object using only the name of the recipe,
	 * and the web address at which it was discovered.  All other data is
	 * initialized.
	 */
	public Recipe(String address, String title)
	{
		//Set name and location of recipe
		this.address = address;
		this.title = title;

		//Initialize all other information
		recipeID = null;
		category = "";
		picAddress = "";
		prepTime = "";
		totalTime = "";
		servings = "";
		summary = "";
		trademark = "";
		servingSize = "";
		calories = null;
		calFat = null;
		totFat = null;
		satFat = null;
		transFat = null;
		cholesterol = null;
		sodium = null;
		carbs = null;
		fiber = null;
		sugar = null;
		protein = null;
		vitA = null;
		vitC = null;
		calcium = null;
		iron = null;
		
		//Initialize ArrayLists
		ingredients = new ArrayList<String>();
		directions = new ArrayList<String>();
		tips = new ArrayList<String>();
	}
	
	//Set the recipeID to whatever is stored in the database.
	public void setID(int recipeID)
	{
		this.recipeID = recipeID;
	}
	
	//Get the recipeID for quick database querying.
		public Integer getID()
		{
			return recipeID;
		}
	
	//Recipe information stored as Strings.
	public String[] getDetails() 
	{
		String[] details = {title, address, category, picAddress,
			prepTime, totalTime, servings, summary, trademark, servingSize};
		return details;
	}

	/*Setting the information stored as Strings.
	 *Null values are acceptable.
	 */
	public void setDetails(String title, String address, String category,
			String picAddress, String prepTime, String totalTime, String 
			servings, String summary, String trademark, String servingSize) 
	{
		this.title = title;
		this.address = address;
		this.category = category;
		this.picAddress = picAddress;
		this.prepTime = prepTime;
		this.totalTime = totalTime;
		this.servings = servings;
		this.summary = summary;
		this.trademark = trademark;
		this.servingSize = servingSize;
	}

	//Information stored as Integers.
	public int[] getNutritionInfo() 
	{
		int[] nutrition = {calories, calFat, totFat, satFat, transFat, 
			cholesterol, sodium, carbs, fiber, sugar, protein, vitA, 
			vitC, calcium, iron};
		return nutrition;
	}

	/*Setting the information stored as Integers.
	 *Null values are acceptable.
	 */
	public void setNutritionInfo(int calories, int calFat, int totFat, int satFat, 
			int transFat, int cholesterol, int sodium, int carbs, int fiber,
			int sugar, int protein, int vitA, int vitC, int calcium, int iron) 
	{
		this.calories = calories;
		this.calFat = calFat;
		this.totFat = totFat;
		this.satFat = satFat;
		this.transFat = transFat;
		this.cholesterol = cholesterol;
		this.sodium = sodium;
		this.carbs = carbs;
		this.fiber = fiber;
		this.sugar = sugar;
		this.protein = protein;
		this.vitA = vitA;
		this.vitC = vitC;
		this.calcium = calcium;
		this.iron = iron;
	}

	//An ArrayList of all ingredients in the recipe
	public ArrayList<String> getIngredients() 
	{
		return ingredients;
	}

	//Set the ArrayList of ingredients used in the recipe.
	public void setIngredients(ArrayList<String> ingredients)
	{
		this.ingredients = ingredients;
	}

	/*Add an ingredient to the end of the ingredients ArrayList.
	 *This String should already have the concatenated amount, amount 
	 *type, and ingredient.
	 */
	public void addIngredient(String ingredient)
	{
		ingredients.add(ingredient);
	}
	
	/*Add an ingredient to the end of the ingredients ArrayList.
	 *The String is built by concatenating the amount, amount 
	 *type, and ingredient.
	 */
	public void addIngredient(String amt, String amtType, String ingredient)
	{
		String ingred = amt + " " + amtType + " " + ingredient;
		addIngredient(ingred);
	}
	
	//An ArrayList of all steps of the directions to be followed for the recipe.
	public ArrayList<String> getDirections()
	{
		return directions;
	}

	//Set the ArrayList of directions for the recipe.
	public void setDirections(ArrayList<String> directions) 
	{
		this.directions = directions;
	}

	//Add a step to the end of the directions ArrayList.
	public void addDirection(String direction)
	{
		directions.add(direction);
	}
	
	//An ArrayList of all "expert tips" for the recipe.
	public ArrayList<String> getTips() 
	{
		return tips;
	}

	//Set the ArrayList of tips.
	public void setTips(ArrayList<String> tips) 
	{
		this.tips = tips;
	}

	//Add a tip to the end of the tips ArrayList
	public void addTip(String tip)
	{
		tips.add(tip);
	}
}
