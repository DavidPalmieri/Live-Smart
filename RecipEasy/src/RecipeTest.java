import data.DatabaseInterface.DBRecipeIntf;
import data.Recipes.Recipe;

public class RecipeTest 
{
	public static void main (String[] args)
	{
		//First, create the object that queries the databse
		DBRecipeIntf queryDB = new DBRecipeIntf();
		//next, create a new recipe object using the recipeID returned from the randomRecipe method
		Recipe recipe = new Recipe(queryDB.randomRecipe());
		//The newly created recipe is only holding a minor amount of its information (Title, times, summary, etc.)
		//Next, use the setAllInfo to get the rest of the recipe info (Nutrition, ingredients, instructions, etc.)
		recipe.setAllInfo();
		//Now, you can use the toString method wherever you need it, or the basicInfo method for menus
		System.out.println(recipe.toString());
	}

}
