package data.Recipes;

import java.util.ArrayList;

import data.DatabaseInterface.DBCategoryIntf;

public class Category implements Comparable<Category>
{
	
	String name;
	int id;
	ArrayList<Recipe> recipes;
	
	public Category(String name, int id)
	{
		this.name = name;
		this.id = id;
		
		setRecipes();
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setRecipes()
	{
		DBCategoryIntf db = new DBCategoryIntf();
		recipes = db.getRecipes(id);
		db.close();
	}
	
	public ArrayList<Recipe> getRecipes()
	{
		return recipes;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int compareTo(Category other) 
	{
		return this.getID() - other.getID();
	}

}
