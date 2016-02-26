package modifydb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	//array of recipe objects to be acted on and commited to the database.
	private Recipe[] recipe;
	
	/*main method
	 * args: web address linking to webpage containing categorized recipe links.
	 * This method takes the input address and creates an array of recipe addresses
	 * to loop through to create each recipe object. 
	 */
	public static void main(String[] args)
	{
		//String url = args[0];
		ArrayList<String> urls = new ArrayList<String>();
			
				urls.add("http://www.bettycrocker.com/recipes/make-ahead-cheeseburger-lasagna/122c69cd-e318-406f-b5e7-67d93d899537");
				urls.add("http://www.bettycrocker.com/recipes/sweet-potato-coconut-and-gingerroot-soup/37e846bc-53cf-4615-9377-d2456511da5c");
				urls.add("http://www.bettycrocker.com/recipes/tropical-smoothie-bowls/8e5666f0-6796-4db1-9e56-022760a97d8c");
				urls.add("http://www.bettycrocker.com/recipes/gluten-free-best-ever-banana-bread/85ebf86a-972e-4768-b759-32191f5e8a4f");
				urls.add("http://www.bettycrocker.com/recipes/teenage-mutant-ninja-turtles-cupcakes/007bfa56-e876-4c26-ac3e-985a6b5ea466");
				
		String url = "";
		Document html = new Document("temp");
		
		for (int i = 0; i < urls.size(); i++)
		{
			url = urls.get(i);
			
			try 
			{
	            //Connect to website
	            html = Jsoup.connect(url).get();

	            if (html != null && html.toString().length() > 0) 
	            {
	               System.out.println("success for recipe number: " + (i + 1));
	            }
	        } 
			catch (IOException e) 
			{
	            e.printStackTrace();
	        }
			
			head(html);
			times(html);
			//System.out.println(html);
		}
	}
	
	private static void head(Document html)
	{
		String head = html.head().text();
		String[] trimmed = head.split(" recipe from Betty Crocker");
		System.out.println(trimmed[0]);
	}
	
	private static void times(Document html)
	{
		Elements e = html.getAllElements();
		for (int i = 0; i < e.size(); i++)
		{
			if(e.get(i).className().equalsIgnoreCase("recipePartDescriptionText"))
			{
				System.out.println(e.get(i).text());
			}
		}
		//String body = html.text();
		//String[] trimmed = body.split("<span class=\"attributeName\">Prep Time</span>");
		//trimmed = trimmed[1].split("</span>");
		//System.out.println("Prep time: " + trimmed[0]);
	}
}
