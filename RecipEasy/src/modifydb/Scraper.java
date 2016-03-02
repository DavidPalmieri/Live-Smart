package modifydb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
			
			//Get the set of html Elements that will be searched for data
			Elements elements = html.getAllElements();
			
			//Get the title of the recipe from the html
			String title = title(html);
			System.out.println(title);
			
			//Get the summary of the recipe
			String summary = summary(elements);
			System.out.println(summary);
			
			//Get the picture address (and later dl the image nd save address)
			String picture = picture(elements);
			System.out.println(picture);
			
			//Get the prep time, total time, and servings
			String[] timeServings = timeServings(elements);
			System.out.println("Prep Time: " + timeServings[0]);
			System.out.println("Total time: " + timeServings[1]);
			System.out.println("Servings: " + timeServings[2]);
			
			//Get all of the nutrition information
			String[] nutrition = nutrition(elements);
			
			//Get the copyright information
			String trademark = trademark(elements);
			System.out.println(trademark);
			
			
			//Create the Recipe object with the information found and add to the ArrayList
			Recipe recipe = new Recipe(url, title);
			recipes.add(recipe);
			
			System.out.println();
		}
		
		//Send data to be serialized for use in the user interface
		//serialize(recipes);
	}
	
	//Parse the html to get the title as a String
	private static String title(Document html)
	{
		String head = html.head().text();
		String[] trimmed = head.split(" recipe from Betty Crocker");
		return trimmed[0];
	}
	
	//Parse the html to get the summary as a String
	private static String summary(Elements e)
	{
		for (int i = 0; i < e.size(); i++)
		{
			if(e.get(i).className().equalsIgnoreCase("recipePartDescriptionText"))
			{
				return e.get(i).text();
			}
		}
		return null;
	}
	
	//Parse the html to get the prep time, total time, and total servings
	private static String[] timeServings(Elements e)
	{
		String[] output = new String[3];
		
		//prep time: find the element, parse the content, convert to our time format
		output[0] = convertTime(parseContent(tagSearch(e, "meta", "prepTime")));
		
		//total time: find the element, parse the content, convert to our time format
		output[1] = convertTime(parseContent(tagSearch(e, "meta", "totalTime")));
		
		//servings: find the element, parse the content
		output[2] = parseContent(tagSearch(e, "meta", "recipeYield"));
		
		return output;
	}
	
	//Parse the html to get the trademark information
	private static String trademark(Elements e)
	{
		for (int i = 0; i < e.size(); i++)
		{
			if(e.get(i).className().equalsIgnoreCase("recipePartCopyRightText"))
			{
				return e.get(i).text();
			}
		}
		return null;
	}
	
	//Parse the html for all of the nutrition information
	private static String[] nutrition(Elements e)
	{
		String[] output = new String[16];
		//serving size, calories, calFat, totFat, satFat, transFat, cholesterol,
		//sodium, carbs, fiber, sugar, protein, vitA, vitC, calcium, iron
		output[0] = parseContent(tagSearch(e, "meta", "servingSize"));
		output[1] = parseContent(tagSearch(e, "meta", "calories"));
		output[2] = parseContent(tagSearch(e, "meta", ""));
		output[3] = parseContent(tagSearch(e, "meta", ""));
		output[4] = parseContent(tagSearch(e, "meta", "saturatedFatContent"));
		output[5] = parseContent(tagSearch(e, "meta", "transFatContent"));
		output[6] = parseContent(tagSearch(e, "meta", "cholesterolContent"));
		output[7] = parseContent(tagSearch(e, "meta", "sodiumContent"));
		output[8] = parseContent(tagSearch(e, "meta", "carbohydrateContent"));
		output[9] = parseContent(tagSearch(e, "meta", "fiberContent"));
		output[10] = parseContent(tagSearch(e, "meta", "sugarContent"));
		output[11] = parseContent(tagSearch(e, "meta", "proteinContent"));
		output[12] = parseContent(tagSearch(e, "meta", ""));
		output[13] = parseContent(tagSearch(e, "meta", ""));
		output[14] = parseContent(tagSearch(e, "meta", ""));
		output[14] = parseContent(tagSearch(e, "meta", ""));
				
		return output;
	}
	
	//Parse the html to find the address of the picture and download it to the computer
	private static String picture(Elements e)
	{
		return parseContent(tagSearch(e, "meta", "image"));
	}
	
	//Search through the html for a specific tag type and tag contents
	private static String tagSearch(Elements e, String tag, String contents)
	{
		Elements matchElements = new Elements();
		
		for (int i = 0; i < e.size(); i++)
		{
			if (e.get(i).tag().toString().equalsIgnoreCase(tag))
			{
				matchElements.add(e.get(i));
			}
		}
		
		for (int i = 0; i < matchElements.size(); i++)
		{
			if (matchElements.get(i).toString().contains(contents))
			{
				return matchElements.get(i).toString();
			}
		}
		
		return null;	
	}
	
	//Parse the contents text out of a tag by splitting the input String into 3 pieces
	private static String parseContent(String input)
	{
		String[] piece = input.split("content=\"");
		piece = piece[1].split("\"");
		return piece[0];
	}
	
	//Convert the input String from form: PT#H##M to form: ## Hours ## Minutes
	private static String convertTime(String input)
	{
		String output = "";		
		String[] piece = input.split("H");
		int hours = Integer.parseInt(piece[0].substring(2));
		
		if (hours != 0)
		{
			output += hours;
			
			if (hours > 1)
			{
				output += " Hours ";
			}
			else
			{
				output += " Hour ";
			}
		}
		
		piece = piece[1].split("M");
		int min = Integer.parseInt(piece[0]);
		
		if (min != 0)
		{
			if(hours != 0 )
			{
				output += "and ";
			}
			output +=  + min;
			
			if (min > 1)
			{
				output += " Minutes";
			}
			else
			{
				output += " Minute";
			}
		}
		
		System.out.println("Time check: " + input + " = " + output);
		return output;
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
