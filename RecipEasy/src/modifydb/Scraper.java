package modifydb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
	public static void main(String[] args)
	{
		//An ArrayList of test recipe URLs
		ArrayList<String> urls = new ArrayList<String>();		
		urls.add("http://www.bettycrocker.com/recipes/make-ahead-cheeseburger-lasagna/122c69cd-e318-406f-b5e7-67d93d899537");
		urls.add("http://www.bettycrocker.com/recipes/sweet-potato-coconut-and-gingerroot-soup/37e846bc-53cf-4615-9377-d2456511da5c");
		urls.add("http://www.bettycrocker.com/recipes/tropical-smoothie-bowls/8e5666f0-6796-4db1-9e56-022760a97d8c");
		urls.add("http://www.bettycrocker.com/recipes/gluten-free-best-ever-banana-bread/85ebf86a-972e-4768-b759-32191f5e8a4f");
		urls.add("http://www.bettycrocker.com/recipes/teenage-mutant-ninja-turtles-cupcakes/007bfa56-e876-4c26-ac3e-985a6b5ea466");
				
		//An ArrayList of recipes used for testing this class
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		//placeholder URL to be updated with the URL of the recipe
		String url = "";
		//placeholder jsoup Document to be updated with the html from the URL
		Document html = new Document("temp");
		
		//Loop through the URLs, creating Recipe objects
		for (int i = 0; i < urls.size(); i++)
		{
			url = urls.get(i);
			
			//Attempt to download the html from the URL using jsoup
			try 
			{
	            //Connect to website
	            html = Jsoup.connect(url).get();
	        } 
			catch (IOException e) 
			{
	            e.printStackTrace();
	        }
			
			//Get the title of the recipe from the html
			String title = head(html);
			
			//Get the summary of the recipe
			String summary = summary(html);
			
			//Create the Recipe object with the information found and add to the ArrayList
			Recipe recipe = new Recipe(url, title);
			recipes.add(recipe);
			
			//Output the data to check for consistency
			System.out.println(recipes.get(i).getDetails()[0]);
		}
		
		//Send data to be serialized for use in the user interface
		serialize(recipes);
	}
	
	//Parse the html to get the title as a String
	private static String head(Document html)
	{
		String head = html.head().text();
		String[] trimmed = head.split(" recipe from Betty Crocker");
		return trimmed[0];
	}
	
	//Parse the html to get the summary as a String
	private static String summary(Document html)
	{
		Elements e = html.getAllElements();
		for (int i = 0; i < e.size(); i++)
		{
			if(e.get(i).className().equalsIgnoreCase("recipePartDescriptionText"))
			{
				return e.get(i).text();
			}
		}
		return null;
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
