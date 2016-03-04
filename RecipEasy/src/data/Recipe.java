package data;

import java.util.ArrayList;
import java.io.Serializable;

public class Recipe implements Serializable
{
	//The ID number of the recipe in the database
	private Integer recipeID;
	
	//Recipe Details
	private String title;
	private String address;
	private String prepTime;
	private String totalTime;
	private String servings;
	private String summary;
	private Boolean hasImage;

	//Nutrition Information
	private String servingSize;
	private String calories;
	private String calFat;
	private String totFat;
	private String satFat;
	private String transFat;
	private String cholesterol;
	private String sodium;
	private String carbs;
	private String fiber;
	private String sugar;
	private String protein;
	private String vitA;
	private String vitC;
	private String calcium;
	private String iron;
	
	//Categories, Ingredients, Directions, and Tips
	private ArrayList<String> categories;
	private ArrayList<String> ingredients;
	private ArrayList<String> directions;
	private ArrayList<String> tips;
	
	/*Recipe object
	 * Initialize a new Recipe object using only the name of the recipe,
	 * and the web address at which it was discovered.  All other data is
	 * initialized.
	 */
	public Recipe(String address)
	{
		//Set location of recipe
		this.address = address;

		//Initialize all other information
		recipeID = null;
		hasImage = false;
		title = "";
		prepTime = "";
		totalTime = "";
		servings = "";
		summary = "";
		servingSize = "";
		calories = "";
		calFat = "";
		totFat = "";
		satFat = "";
		transFat = "";
		cholesterol = "";
		sodium = "";
		carbs = "";
		fiber = "";
		sugar = "";
		protein = "";
		vitA = "";
		vitC = "";
		calcium = "";
		iron = "";
		
		//Initialize ArrayLists
		categories = new ArrayList<String>();
		ingredients = new ArrayList<String>();
		directions = new ArrayList<String>();
		tips = new ArrayList<String>();
	}
	
	//Set the recipeID to whatever is stored in the database.
	public void setID(int recipeID)
	{
		this.recipeID = recipeID;
	}
	
	//If the recipe has an image, it can be found using the recipe title
	public void hasImage()
	{
		hasImage = true;
	}
	
	//Get the recipeID for quick database querying.
		public int getID()
		{
			return recipeID;
		}
	
	//Recipe information stored as Strings.
	public String[] getDetails() 
	{
		String[] details = {title, address, prepTime, totalTime, servings, summary};
		return details;
	}

	/*Setting the information stored as Strings.
	 *Null values are acceptable.
	 */
	public void setDetails(String title, String prepTime, String totalTime, String servings, String summary) 
	{
		this.title = title;
		this.prepTime = prepTime;
		this.totalTime = totalTime;
		this.servings = servings;
		this.summary = summary;
	}

	//Information stored as Integers.
	public String[] getNutritionInfo() 
	{
		String[] nutrition = {servingSize, calories, calFat, totFat, satFat, transFat, 
							  cholesterol, sodium, carbs, fiber, sugar, protein, vitA, 
							  vitC, calcium, iron};
		return nutrition;
	}

	/*Setting the information stored as Integers.
	 *Null values are acceptable.
	 */
	public void setNutritionInfo(String servingSize, String calories, String calFat, String totFat, 
								 String satFat, String transFat, String cholesterol, String sodium, 
								 String carbs, String fiber, String sugar, String protein, String vitA, 
								 String vitC, String calcium, String iron) 
	{
		this.servingSize = servingSize;
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
	
	//An ArrayList of all categories in the recipe
	public ArrayList<String> getCategories() 
	{
		return categories;
	}

	//Set the ArrayList of ingredients used in the recipe.
	public void setCategories(ArrayList<String> categories)
	{
		this.categories = categories;
	}
	
	//Add a category to the ArrayList
	public void addCategory(String category)
	{
		categories.add(category);
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
	
	public void commitRecipe()
	{
		//commit the recipe to the database
	}
	
	public void printRecipe()
	{		
		System.out.println("Recipe: " + title);
		
		System.out.println();
		
		System.out.println("Description: " + summary);
		
		System.out.print("Categories: ");
		for(int i = 0; i < categories.size(); i++)
		{
			if (i == categories.size()-1)
			{
				System.out.print(categories.get(i) + " Recipes");
			}
			else
			{
				System.out.print(categories.get(i) + " Recipes,");
			}		
		}
		
		System.out.println();
		
		if (hasImage == true)
		{
			System.out.println("Image Name: " + title + ".jpg");
		}
		else
		{
			System.out.println("No Image Available, Using: NoImage.jpg");	
		}
		
		System.out.println();
		
		System.out.println("Prep Time: " + prepTime);
		System.out.println("Total Time: " + totalTime);
		System.out.println("Total Servings: " + servings);
		
		System.out.println();
		
		System.out.println("Ingredients:");
		for(int i = 0; i < ingredients.size(); i++)
		{
			System.out.println(ingredients.get(i));
		}
		
		System.out.println();
		
		System.out.println("Directions:");
		for(int i = 0; i < directions.size(); i++)
		{
			System.out.println(directions.get(i));
		}
		
		System.out.println();
		
		System.out.println("Serving Size: " + servingSize);
		System.out.println("Calories: " + calories + ", Calories from Fat: " + calFat + ", Total Fat: " + totFat + ", Saturated Fat: " + satFat + ",");
		System.out.println("Trans Fat: " + transFat + ", Cholesterol: " + cholesterol + ", Sodium: " + sodium + ", Total Carbohydrates: " + carbs + ",");
		System.out.println("Dietary Fiber: " + fiber + ", Sugars: " + sugar + ", Protein: " + protein + ", Vitamin A: " + vitA + ", Vitamin C: " + vitC + ",");
		System.out.println("Calcium: " + calcium + ", Iron: " + iron);
		
		System.out.println();
		
		System.out.println("Tips:");
		for(int i = 0; i < tips.size(); i++)
		{
			System.out.println(tips.get(i));
		}
		
		System.out.println();
		
		System.out.println("Web Address: " + address);
		System.out.println("\n==========================================================================================================================\n");
	}
}
