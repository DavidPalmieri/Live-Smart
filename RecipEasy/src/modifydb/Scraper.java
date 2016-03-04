package modifydb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import data.Recipe;

/*Scraper class
 * takes a web address (betty crocker website -> category), and parses out the web
 * address of all recipes contained within.  Each recipe address is then converted to
 * a file and parsed for required data.  After parsing, the user confirms the data and
 * commits to the database.
 */
public class Scraper 
{
	
	/*main method
	 * args:[in final draft] web address linking to webpage containing categorized recipe links.
	 * This method takes the input address and creates an array of recipe addresses
	 * to loop through to create each recipe object. 
	 */
	public static void main(String[] args) throws IOException
	{
		URLGrabber linkGrabber = new URLGrabber("Apple.txt");
		
		//Start an ArrayList of categories, with the category that this recipe currently falls under
		String category = linkGrabber.getCategory();
		ArrayList<String> categories = new ArrayList<String>();
		categories.add(category);
		
		ArrayList<String> urls = linkGrabber.getLinks();
				
		//An ArrayList of recipes used for testing this class
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		//placeholder URL to be updated with the URL of the recipe
		String url = "";
		
		//Loop through the URLs, creating Recipe objects
		for (int i = 0; i < urls.size(); i++)
		{
			url = urls.get(i);
			
			//Create a new HtmlParserObject to search the HTML
			HtmlParser parser = new HtmlParser(url);
			
			//Get all data of the recipe
			String title = parser.getTitle();
			String summary = parser.getSummary();
			String prepTime = parser.getPrepTime();
			String totalTime = parser.getTotalTime();
			String servings = parser.getServings();
			String[] nutrition = parser.getNutrition();
			ArrayList<String> ingredients = parser.getIngredients();
			ArrayList<String> directions = parser.getDirections();
			ArrayList<String> tips = parser.getTips();		
			
			//Download the recipe image to the package directory "RecipePictures"
			parser.getPicture(title);
			
			//Create the Recipe object with the information found and add to the ArrayList
			Recipe recipe = new Recipe(url, title);
			recipe.setDetails(prepTime, totalTime, servings, summary);
			recipe.setCategories(categories);
			recipe.setTips(tips);
			recipe.setDirections(directions);
			recipe.setIngredients(ingredients);
			recipe.setNutritionInfo(nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5],
									nutrition[6], nutrition[7], nutrition[8], nutrition[9], nutrition[10], nutrition[11], 
									nutrition[12], nutrition[13], nutrition[14], nutrition[15]);
			recipes.add(recipe);
			recipe.printRecipe();
		}
		
		//Send data to be serialized for use in the user interface
		serialize(recipes);
	}
	
	//serialize the ArrayList of recipes for use in the interface
	private static void serialize(ArrayList<Recipe> recipes)
	{
		//Temporary folder for files to be held on the computer that this is run on
		String tmpDir = System.getProperty("java.io.tmpdir");
		
		//Loop through the ArrayList of Recipes
		for (int i = 1; i <= recipes.size(); i++)
		{
			//Set the filename of the serialized Recipe object
			String filename = tmpDir + "recipe" + i + ".ser";
			
			//Serialize the Recipe, and output the file location
			try 
			{
				FileOutputStream fileOut = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(recipes.get(i-1));
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved in " + filename);
			} 
			catch (IOException j) 
			{
				j.printStackTrace();
			}
			//Serialization is complete for this Recipe
			
			//To convert from serialized data file to usable object, follow these steps:
			
			//String filename = (local directory address and name of file)
			FileInputStream fis = null;
			ObjectInputStream in = null;
			
			try 
			{
				fis = new FileInputStream(filename);
				in = new ObjectInputStream(fis);
				recipes.set(i-1, (Recipe) in.readObject());
				in.close();

			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}
		
	}
}
