package data;

import java.util.ArrayList;

public class Recipe 
{
	private String title;
	private String address;
	private String category;
	private String picAddress;
	private String prepTime;
	private String totalTime;
	private String servings;
	private String summary;
	private String author;
	private String servingSize;
	private int calories;
	private int calFat;
	private int totFat;
	private int satFat;
	private int transFat;
	private int cholesterol;
	private int sodium;
	private int carbs;
	private int fiber;
	private int sugar;
	private int protein;
	private int vitA;
	private int vitC;
	private int calcium;
	private int iron;
	private ArrayList<String> ingredients;
	private ArrayList<String> directions;
	private ArrayList<String> tips;
	
	public Recipe(String a, String n)
	{
		address = a;
		title = n;
	}
	
	public String[] getDetails() 
	{
		String[] details = {title, address, category, picAddress,
			prepTime, totalTime, servings, summary, author};
		return details;
	}

	public void setDetails(String title, String address, String category,
			String picAddress, String prepTime, String totalTime, String 
			servings, String summary, String author) 
	{
		this.title = title;
		this.address = address;
		this.category = category;
		this.picAddress = picAddress;
		this.prepTime = prepTime;
		this.totalTime = totalTime;
		this.servings = servings;
		this.summary = summary;
		this.author = author;
	}

	public int[] getNutrition() 
	{
		int[] nutrition = {calories, calFat, totFat, satFat, transFat, 
			cholesterol, sodium, carbs, fiber, sugar, protein, vitA, 
			vitC, calcium, iron};
		return nutrition;
	}

	public void setNutrition(int calories, int calFat, int totFat, int satFat, 
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

	public String getServingSize() 
	{
		return servingSize;
	}

	public void setServingSize(String servingSize) 
	{
		this.servingSize = servingSize;
	}

	
	public ArrayList<String> getIngredients() 
	{
		return ingredients;
	}

	public void setIngredients(ArrayList<String> ingredients)
	{
		this.ingredients = ingredients;
	}

	public ArrayList<String> getDirections()
	{
		return directions;
	}

	public void setDirections(ArrayList<String> directions) 
	{
		this.directions = directions;
	}

	public ArrayList<String> getTips() 
	{
		return tips;
	}

	public void setTips(ArrayList<String> tips) 
	{
		this.tips = tips;
	}
	
	public void addIngredient(String ingredient)
	{
		ingredients.add(ingredient);
	}
	
	public void addIngredient(String amt, String amtType, String ingredient)
	{
		String ingred = amt + " " + amtType + " " + ingredient;
		addIngredient(ingred);
	}
	
	public void addDirection(String direction)
	{
		directions.add(direction);
	}
	
	public void addTip(String tip)
	{
		tips.add(tip);
	}
}
