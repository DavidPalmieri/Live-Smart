package data.Recipes;

import java.util.ArrayList;

import data.DatabaseInterface.DBCategoryIntf;

public class CategoryList implements RecipeInfo
{
	ArrayList<Category> categories;
	
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
		
		int size = categories.size();
		
		if (size == 1) return "Category: " + categories.get(0) + "\n\n";
		else
		{
			String cat = "Categories: ";
			
			for (int i = 0; i < size; i++)
			{
				cat += categories.get(i);
				
				if (i < size - 1) cat += ", ";
			}
			return cat + "\n\n";
		}
	}
}
