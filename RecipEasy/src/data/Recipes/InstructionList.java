package data.Recipes;

public class InstructionList implements RecipeInfo 
{
	String[] instructions;
	
	public InstructionList(int recipeID)
	{
		queryDB(recipeID);
	}
	
	@Override
	public void queryDB(int recipeID) 
	{
		//queryDB
		instructions = null;
	}
	
	@Override
	public String toString()
	{
		int size = instructions.length;
		
		String inst = "Instructions:\n";
		
		for(int i = 0; i < size; i++)
		{
			inst += "Step " + (i+1)+ ": " + instructions[i] + "\n";
		}
		
		return inst + "\n";
	}
}
