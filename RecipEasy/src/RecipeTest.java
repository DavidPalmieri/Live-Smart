import data.DatabaseInterface.DBRecipeIntf;
import data.Recipes.Recipe;

public class RecipeTest 
{
	public static void main (String[] args)
	{
		DBRecipeIntf queryDB = new DBRecipeIntf();
		Recipe recipe = new Recipe(queryDB.randomRecipe());
		recipe.setAllInfo();
		System.out.println(recipe.toString());
	}

}
