package gui;


import java.util.Random;

import org.jasypt.util.password.StrongPasswordEncryptor;

import data.DatabaseInterface.DBRecipeIntf;
import data.DatabaseInterface.DBUsersIntf;
import data.Recipes.Recipe;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class Controller {	
	
	@FXML TextField user;
	@FXML PasswordField pw;
	@FXML TextField search;
	@FXML TextArea textArea;
	@FXML TreeView<String> tree;
	@FXML TableView<String> tableView;
	@FXML ListView<String> listView;

    public void loginButtonClicked(){
        System.out.printf("%s logged in...\n", user.getText());  	//user: test
        System.out.printf("password %s\n", pw.getText());			//password: password
        
        //Get the fields input by the user
        String username = user.getText();
        String password = pw.getText();
        
        //Open a connection to the DB and get the encrypted password for the given username
        DBUsersIntf dbLookup = new DBUsersIntf();
        String encryptedPassword = dbLookup.getPassword(username);
        dbLookup.close();
        
        //Use Jasypt's password encryptor to verify whether the plain-text and encrypted passwords match
        if (!encryptedPassword.equalsIgnoreCase("")){
        	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    		if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
    			//success, the input password matches the password on file
    			System.out.println("Successful password match\n");
    		}
    		else {
    			//fail, the passwords do not match
    			System.out.println("unsuccessful password match\n");
    			AlertBox.display("Invalid Password", "Password does not match!");
    		}	
        } else { // empty password string = username not found in Users table
        	System.out.println("username not found\n");
        	AlertBox.display("Error", "User not found!");
        }
        
    }

    public void randomButtonClicked(){
        System.out.println("Displaying random recipe...");      

        //First, create the object that queries the databse
      	DBRecipeIntf queryDB = new DBRecipeIntf();
      	//next, create a new recipe object using the recipeID returned from the randomRecipe method
      	Recipe recipe = new Recipe(queryDB.randomRecipe());
      	//Now close the Database object
      	queryDB.close();
      	//The newly created recipe is only holding a minor amount of its information (Title, times, summary, etc.)
      	//Next, use the setAllInfo to get the rest of the recipe info (Nutrition, ingredients, instructions, etc.)
      	recipe.setAllInfo();
      	//Now, you can use the toString method wherever you need it, or the basicInfo method for menus
      	System.out.printf("%d\n", recipe.getRecipeID());   
        textArea.setText(recipe.toString());        
        
        //TODO: Make list of recipes appear here
        listView.setItems(null);   
                
        
    }
    
    public void searchButtonClicked(){
        System.out.printf("Searching for %s...\n", search.getText());
    }
    
    public void aboutWindowGo()	{
    	System.out.println("Loading about page...");
    	AlertBox.display("About", "Awesome Inc. 2016");
    }

}
