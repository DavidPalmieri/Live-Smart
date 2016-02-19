package modifydb;

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
		
	}

}
