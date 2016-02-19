package data;

import java.util.ArrayList;

public class Recipe 
{
	private String title;
	private String address;
	private String category;
	private String picAddress;
	private int prepTime;
	private int totalTime;
	private int servings;
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
	
	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getAddress() 
	{
		return address;
	}

	public void setAddress(String address) 
	{
		this.address = address;
	}

	public String getCategory() 
	{
		return category;
	}

	public void setCategory(String category) 
	{
		this.category = category;
	}

	public String getPicAddress() 
	{
		return picAddress;
	}

	public void setPicAddress(String picAddress) 
	{
		this.picAddress = picAddress;
	}

	public int getPrepTime() 
	{
		return prepTime;
	}

	public void setPrepTime(int prepTime) 
	{
		this.prepTime = prepTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) 
	{
		this.totalTime = totalTime;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) 
	{
		this.servings = servings;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) 
	{
		this.summary = summary;
	}

	public String getAuthor() 
	{
		return author;
	}

	public void setAuthor(String author) 
	{
		this.author = author;
	}

	public String getServingSize() 
	{
		return servingSize;
	}

	public void setServingSize(String servingSize) 
	{
		this.servingSize = servingSize;
	}

	public int getCalories() 
	{
		return calories;
	}

	public void setCalories(int calories) 
	{
		this.calories = calories;
	}

	public int getCalFat() {
		return calFat;
	}

	public void setCalFat(int calFat) 
	{
		this.calFat = calFat;
	}

	public int getTotFat() 
	{
		return totFat;
	}

	public void setTotFat(int totFat) 
	{
		this.totFat = totFat;
	}

	public int getSatFat() 
	{
		return satFat;
	}

	public void setSatFat(int satFat) 
	{
		this.satFat = satFat;
	}

	public int getTransFat() 
	{
		return transFat;
	}

	public void setTransFat(int transFat) 
	{
		this.transFat = transFat;
	}

	public int getCholesterol() 
	{
		return cholesterol;
	}

	public void setCholesterol(int cholesterol) 
	{
		this.cholesterol = cholesterol;
	}

	public int getSodium() 
	{
		return sodium;
	}

	public void setSodium(int sodium) 
	{
		this.sodium = sodium;
	}

	public int getCarbs() 
	{
		return carbs;
	}

	public void setCarbs(int carbs) 
	{
		this.carbs = carbs;
	}

	public int getFiber() 
	{
		return fiber;
	}

	public void setFiber(int fiber) 
	{
		this.fiber = fiber;
	}

	public int getSugar() 
	{
		return sugar;
	}

	public void setSugar(int sugar)
	{
		this.sugar = sugar;
	}

	public int getProtein() 
	{
		return protein;
	}

	public void setProtein(int protein) 
	{
		this.protein = protein;
	}

	public int getVitA() 
	{
		return vitA;
	}

	public void setVitA(int vitA) 
	{
		this.vitA = vitA;
	}

	public int getVitC() 
	{
		return vitC;
	}

	public void setVitC(int vitC) 
	{
		this.vitC = vitC;
	}

	public int getCalcium() 
	{
		return calcium;
	}

	public void setCalcium(int calcium) 
	{
		this.calcium = calcium;
	}

	public int getIron() 
	{
		return iron;
	}

	public void setIron(int iron) 
	{
		this.iron = iron;
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
		
	}
	
	public void addIngredient(String amt, String amtType, String ingredient)
	{
		
	}
	
	public void addDirection(String direction)
	{
		
	}
	
	public void addTip(String tip)
	{
		
	}
}
