package data;

import java.util.ArrayList;

public class RecipeList extends ArrayList<String>
{
	String category;
	
	public RecipeList(String category)
	{
		this.category = category;
	}
	
	public String getCategory()
	{
		return category;
	}
}
