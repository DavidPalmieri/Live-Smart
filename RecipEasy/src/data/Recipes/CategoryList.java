package data.Recipes;

import data.DatabaseInterface.DBCategoryIntf;

public class CategoryList implements RecipeInfo
{
	String[] categories;
	
	public CategoryList(int recipeID)
	{
		queryDB(recipeID);
	}
	
	@Override
	public void queryDB(int recipeID) 
	{
		DBCategoryIntf queryDB = new DBCategoryIntf();
		categories = queryDB.getCategories(recipeID);
		queryDB.close();
	}
	
	@Override
	public String toString()
	{
		
		int size = categories.length;
		
		if (size == 1) return "Category: " + categories[0] + "\n\n";
		else
		{
			String cat = "Categories: ";
			
			for (int i = 0; i < size; i++)
			{
				cat += categories[i];
				
				if (i < size - 1) cat += ", ";
			}
			return cat + "\n\n";
		}
	}
}
