import java.util.ArrayList;
import java.util.Scanner;

import org.jasypt.util.password.StrongPasswordEncryptor;

import data.Recipes.Recipe;
import data.Users.Rating;
import data.Users.User;
import gui.DataInterface;

public class DataInterfaceTest 
{
	public static DataInterface di;
	public static User user;
	public static ArrayList<Recipe> favorites;
	public static Scanner cin;
	
	public static void main(String[] args)
	{
		cin = new Scanner(System.in);
		di = new DataInterface();
		
		loginOrRegister();
		
		favorites = di.getFavorites(user);
		
		search();
		
		cin.close();
	}
	
	public static void loginOrRegister()
	{
		System.out.println("Register new account? (Y / N): ");
		String selection = cin.nextLine();
		
		if (selection.equalsIgnoreCase("Y"))
		{
			register();
		}
		else if (selection.equalsIgnoreCase("N"))
		{
			login();
		}
		else
		{
			System.out.println("Please only enter 'Y' for yes, or 'N' for no");
			loginOrRegister();
		}
	}
	
	public static void login()
	{
		System.out.println("Enter username:");
		String username = cin.nextLine();
		
		int userID = di.getUserID(username);
		if (userID == 0)
		{
			System.out.println("Invalid username entered");
			login();
		}
		else
		{
			cin = new Scanner(System.in);
			System.out.println("Enter password:");
			String password = cin.nextLine();
			
			String encryptedPassword = di.getPassword(username);
			
			StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    		if (passwordEncryptor.checkPassword(password, encryptedPassword)) 
    		{
    			user = di.getUser(userID, username);
    			System.out.println("Successfully logged in");
    		}
    		else
    		{
    			System.out.println("passwords do not match");
    			login();
    		}
		}
	}
	
	public static void register()
	{
		System.out.println("Enter new username: ");
		String username = cin.nextLine();
		
		int userID = di.getUserID(username);
		if (userID != 0)
		{
			System.out.println("Username already taken, please enter a different username");
			register();
		}
		else
		{
			System.out.println("Please enter your desired password: ");
			String password = cin.nextLine();
			System.out.println("Please re-enter your desired password: ");
			String verifyPassword = cin.nextLine();
			
			if (!password.contentEquals(verifyPassword))
			{
				System.out.println("Passwords do not match");
				register();
			}
			else
			{
				StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
				String encryptedPassword = passwordEncryptor.encryptPassword(password);	
				di.registerAccount(username, encryptedPassword);
				
				System.out.println("Account successfully created");
				userID = di.getUserID(username);
				user = di.getUser(userID, username);
			}
			
		}
	}
	
	public static ArrayList<Recipe> search()
	{
		System.out.println("Enter search term: ");
		String searchTerm = cin.nextLine();
		searchTerm = searchTerm.replaceAll(" ", "%");
		
		ArrayList<Recipe> recipes = di.simpleSearch(searchTerm);
		
		for (Recipe recipe : recipes)
		{
			System.out.println("RecipeID: " + recipe.getRecipeID() + " " + recipe.getTitle() + "	Rating: " + recipe.getAvgRating().getLiked());
		}
		
		return recipes;
	}
	
	public static Recipe selectRecipe(Recipe recipe)
	{
		recipe.setAllInfo();
		int ratingID = di.selectRecipe(user.getUserID(), recipe);
		
		
		return recipe;
	}
	
	public static void rateRecipe(Recipe recipe, int ratingID)
	{
		int userID = user.getUserID();
		int recipeID = recipe.getRecipeID();
		int liked = 0;
		int ease = 0;
		int cost = 0;
		
		di.rateRecipe(userID, recipeID, ratingID, liked, cost, ease);
	}
}
