package data.Recipes;

import java.util.ArrayList;

import data.DatabaseInterface.DBCategoryIntf;

public class Category 
{
	
	String name;
	int id;
	ArrayList<Recipe> recipes;
	
	public Category(String name, int id)
	{
		this.name = name;
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void getRecipes()
	{
		DBCategoryIntf db = new DBCategoryIntf();
		recipes = db.getRecipes(id);
		db.close();
	}
	
	@Override
	public String toString()
	{
		return name;
	}

}
