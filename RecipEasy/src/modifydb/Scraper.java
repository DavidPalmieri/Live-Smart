package modifydb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import data.Recipe;
import data.RecipeList;

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
		//Instantiate the MasterList.txt and ErrorList.txt files for use
		File masterList = new File("src/modifydb/CategoryLinks/MasterList.txt");
		File errorList = new File("src/modifydb/CategoryLinks/ErrorList.txt");
		//PrintWriter writer = new PrintWriter(masterList);
		//writer.print("");
		//writer.close();
		//writer = new PrintWriter(masterList);
		//writer.print("");
		//writer.close();
		
		//Condense all supporting text files to one master list
		//buildMasterList();
		
		//Use the master list to create an ArrayList of RecipeLists
		ArrayList<RecipeList> recipeLists = buildRecipeLists(masterList);
		
		//Use the RecipeLists to create individual Recipe objects
		for (RecipeList list : recipeLists)
		{
			if (!list.getCategory().equalsIgnoreCase("temp"))
			{
				readRecipeList(list);
			}
		}
		
		//Now loop back through for all of the recipe urls that did not download properly
		recipeLists = buildRecipeLists(errorList);
		
		//Use the RecipeLists to create individual Recipe objects
		for (RecipeList list : recipeLists)
		{
			if (!list.getCategory().equalsIgnoreCase("temp"))
			{
				readRecipeList(list);
			}
		}
		
		//Print out the remaining urls that had errored downloading twice
		Scanner sc = new Scanner(errorList);
		 
		String line = "";
		
		while (sc.hasNextLine())
		{	
			line = sc.nextLine();
			
			if (line.contains("http://"))
			{
				System.out.println(line);
			}
		}
	}	
			
	
	//Use all supprting text files to create one master list with all recipe URLs and categories
	private static void buildMasterList() throws IOException
	{
		//Get every text file containing links to recipes from the directory
		File catDirectory = new File("src/modifydb/CategoryLinks/");
		String[] allFileNames = catDirectory.list();
		
		//Work through each text file, creating all of the contained recipes
		for (String fileName : allFileNames)
		{
			//linkGrabber object takes a text file full of URLs and parses out recipe URLs
			URLGrabber linkGrabber = new URLGrabber(fileName);
			
			//Populate the MasterList.txt document with every recipe URL under each category
			if (!fileName.equalsIgnoreCase("MasterList.txt") && !fileName.equalsIgnoreCase("ErrorList.txt"))
			{
				linkGrabber.populateMasterList();		
			}
		}
	}
	
	//Create an ArrayList that holds all of the URLs of each category (stored as ArrayLists)
	private static ArrayList<RecipeList> buildRecipeLists(File masterList) throws IOException
	{
		ArrayList<RecipeList> recipeLists = new ArrayList<RecipeList>();
		
		Scanner sc = new Scanner(masterList);
	 
		String line = "";
		RecipeList list = new RecipeList("temp");
		
		while (sc.hasNextLine())
		{	
			line = sc.nextLine();
			
			if (!line.contains("http://"))
			{
				recipeLists.add(list);
				list = new RecipeList(line);
			}
			else
			{
				list.add(line);
			}
		}
		sc.close();
		
		//Re-initialize the error list (for second pass) to note all urls that errored twice
		File errorList = new File("src/modifydb/CategoryLinks/ErrorList.txt");
		PrintWriter writer = new PrintWriter(errorList);
		writer.print("");
		writer.close();
				
		return recipeLists;
	}
	
	//Iterate through the RecipeLists creating Recipe objects
	private static void readRecipeList(RecipeList list) throws IOException
	{		
		//placeholder URL to be updated with the URL of the recipe
		String url = "";
		
		//Loop through the URLs, creating Recipe objects
		for (int i = 0; i < list.size(); i++)
		{
			url = list.get(i);
			buildRecipe(url, list.getCategory());
		}
		
		//Send data to be serialized for use in the user interface
		//serialize(recipes, category);
	
	}
	
	private static void buildRecipe(String url, String category) throws IOException
	{
		
		//Check the database to make sure this recipe doesn't already exist.
		//If it does, add the category to the recipe and move on
		//Pseudocode:
		//if (databse contains url)
		//{
		//	  add category to database using recipeID
		//}
		//else
		//{
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
			boolean hasImage = parser.getPicture(title);
			
			//Check to make sure that the website did not timeout while pulling data
			if (title.equalsIgnoreCase(""))
			{
				//Save the url to be re-run at a later time
				File errorList = new File("src/modifydb/CategoryLinks/ErrorList.txt");
				PrintWriter writer = new PrintWriter(errorList);
				writer.print(category);
				writer.print(url);
				writer.close();
			}
			else
			{
				//Create the Recipe object with the information found and add to the ArrayList
				Recipe recipe = new Recipe(url);
				recipe.setDetails(title, prepTime, totalTime, servings, summary);
				recipe.addCategory(category);
				if (hasImage == true) 
				{ 
					recipe.hasImage(); 
				}
				recipe.setTips(tips);
				recipe.setDirections(directions);
				recipe.setIngredients(ingredients);
				recipe.setNutritionInfo(nutrition[0], nutrition[1], nutrition[2], nutrition[3], nutrition[4], nutrition[5],
										nutrition[6], nutrition[7], nutrition[8], nutrition[9], nutrition[10], nutrition[11], 
										nutrition[12], nutrition[13], nutrition[14], nutrition[15]);
				recipe.printRecipe();
				//recipe.commitRecipe();
			}
		//}	 
	}
	
	//serialize the ArrayList of recipes for use in the interface
	private static void serialize(ArrayList<Recipe> recipes, String category)
	{
		//Temporary folder for files to be held on the computer that this is run on
		String tmpDir = System.getProperty("java.io.tmpdir");
		
		//Loop through the ArrayList of Recipes
		//Set the filename of the serialized Recipe object
		String filename = tmpDir + category + "Recipes.ser";
		
		//Serialize the Recipe, and output the file location
		try 
		{
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(recipes);
			out.close();
			fileOut.close();
			System.out.println(recipes.size() + " recipes serialized and saved in " + filename);
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
			recipes =  (ArrayList<Recipe>)in.readObject();
			in.close();

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}	
			
		
		
	}
}
