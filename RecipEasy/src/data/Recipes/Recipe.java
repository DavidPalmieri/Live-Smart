package data.Recipes;

public class Recipe
{
	//The ID number of the recipe in the database
	private int recipeID;
	
	//Recipe information
	private BasicInfo basicInfo;
	private Nutrition nutrition;
	private CategoryList categories;
	private IngredientList ingredients;
	private InstructionList instructions;
	private TipList tips;

	public Recipe(int recipeID)
	{
		this.recipeID = recipeID;
		setBasicInfo();
		
		nutrition = null;
		categories = null;
		ingredients = null;
		instructions = null;
	}

	public int getRecipeID()
	{
		return recipeID;
	}

	public void setBasicInfo() 
	{
		basicInfo = new BasicInfo(recipeID);			
	}
	
	public void setAllInfo()
	{
		tips = new TipList(recipeID);
		nutrition = new Nutrition(recipeID);
		categories = new CategoryList(recipeID);
		ingredients = new IngredientList(recipeID);
		instructions = new InstructionList(recipeID);
	}
	
	public String basicInfo()
	{
		return basicInfo.condensedInfo();
	}

	@Override
	public String toString() 
	{
		String recipe = "";
		
		recipe += basicInfo.toString() + "\n";
		recipe += categories.toString();
		recipe += ingredients.toString();
		//recipe += instructions.toString();
		recipe += tips.toString();
		recipe += nutrition.toString();
		recipe += basicInfo.getAddress();
		recipe += "© 2015 ®/TM General Mills All Rights Reserved";
			
		return  recipe;
	}
	
}
