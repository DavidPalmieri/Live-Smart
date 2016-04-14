package data.Recipes;

import data.DatabaseInterface.DBRecipeIntf;

public class Nutrition implements RecipeInfo
{
	
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
	
	public Nutrition(int recipeID)
	{
		queryDB(recipeID);
	}
	
	@Override
	public void queryDB(int recipeID)
	{
		DBRecipeIntf queryDB = new DBRecipeIntf();
		String[] nutrition = queryDB.getNutrition(recipeID);
		
				servingSize = nutrition[0];
				calories = nutrition[1];
				calFat = nutrition[2];
				totFat = nutrition[3];
				satFat = nutrition[4];
				transFat = nutrition[5];
				cholesterol = nutrition[6];
				sodium = nutrition[7];
				carbs = nutrition[8];
				fiber = nutrition[9];
				sugar = nutrition[10];
				protein = nutrition[11];
				vitA = nutrition[12];
				vitC = nutrition[13];
				calcium = nutrition[14];
				iron = nutrition[15];
	}
	
	@Override
	public String toString()
	{
		String nutrition = "Nutritional Information:";
		
		nutrition += "\nServing Size: " + servingSize;
		nutrition += "\nCalories " + calories;
		nutrition += "\nCalories from Fat: " + calFat;
		nutrition += "\nTotal Fat: " + totFat;
		nutrition += "\nSaturated Fat: " + satFat;
		nutrition += "\nTrans Fat: " + transFat;
		nutrition += "\nCholesterol: " + cholesterol;
		nutrition += "\nSodium: " + sodium;
		nutrition += "\nCarbohydrates: " + carbs;
		nutrition += "\nFiber: " + fiber;
		nutrition += "\nSugar: " + sugar;
		nutrition += "\nProtein: " + protein;
		nutrition += "\nVitamin A: " + vitA;
		nutrition += "\nVitamin C: " + vitC;
		nutrition += "\nCalcium: " + calcium;
		nutrition += "\nIron: " + iron;
		
		return nutrition + "\n\n";
	}
}
