package data.Recipes;

import data.DatabaseInterface.DBTipIntf;

public class TipList implements RecipeInfo 
{
	String[] tips;
	
	public TipList(int recipeID)
	{
		queryDB(recipeID);
	}
	
	@Override
	public void queryDB(int recipeID) 
	{
		DBTipIntf queryDB = new DBTipIntf();
		tips = queryDB.getTips(recipeID);
		queryDB.close();	
	}
	
	@Override
	public String toString()
	{
		int size = tips.length;
		
		if (size<0) {
		String tip = "Tips:\n";
		
		for(int i = 0; i < size; i++)
		{
			tip += tips[i] + "\n";
		}
		return tip + "\n";
			
		}
		else {
			String tip = "Sorry there are no tips";
			return tip;
		}
	}
}
