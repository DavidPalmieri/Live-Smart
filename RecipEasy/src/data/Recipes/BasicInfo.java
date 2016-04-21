package data.Recipes;

import data.DatabaseInterface.DBRecipeIntf;

public class BasicInfo implements RecipeInfo
{
		private String title;
		private String summary;
		private String prepTime;
		private String totalTime;
		private String servings;
		private String address;

	public BasicInfo(int recipeID)
	{
		queryDB(recipeID);
	}
	
	public String getAddress()
	{
		return address + "\n";
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public String getPrepTime()
	{
		return prepTime;
	}

	public String getTotalTime()
	{
		return totalTime;
	}
	
	public String getServings()
	{
		return servings;
	}
	
	@Override
	public void queryDB(int recipeID) 
	{
		DBRecipeIntf queryDB = new DBRecipeIntf();
		String[] basicInfo = queryDB.getBasicInfo(recipeID);
		
		title = basicInfo[0];
		summary = basicInfo[1];
		prepTime = basicInfo[2];
		totalTime = basicInfo[3];
		servings = basicInfo[4];
		address = basicInfo[5];
	}
}
