package data.Recipes;

import data.DatabaseInterface.DBIngredientIntf;

public class IngredientList implements RecipeInfo 
{
	String[] ingredients;
	
	public IngredientList(int recipeID)
	{
		queryDB(recipeID);
	}
	
	@Override
	public void queryDB(int recipeID) 
	{
		DBIngredientIntf queryDB = new DBIngredientIntf();
		ingredients = queryDB.getIngredients(recipeID);
		queryDB.close();
	}
	
	@Override
	public String toString()
	{
		int size = ingredients.length;
		
		String ing = "Ingredients:\n";
		
		for(int i = 0; i < size; i++)
		{
			ing += ingredients[i] + "\n";
		}
		
		return ing + "\n";
	}
}
