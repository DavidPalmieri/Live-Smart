

import data.DatabaseInterface.DBCategoryIntf;

public class CategoryTest 
{

	public static void main (String[] args)
	{
		DBCategoryIntf db = new DBCategoryIntf();
		//String[] categories = db.getCategories(2000);
		//for (String cat : categories)
		//{
			//System.out.println(cat);
		//}
		int[] recipes = db.getRecipes(db.randomCategory());
		
		for (int id : recipes)
		{
			System.out.println(id);
		}
		db.close();
	}
}
