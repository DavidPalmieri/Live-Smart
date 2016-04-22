package gui;


import java.util.ArrayList;

import org.jasypt.util.password.StrongPasswordEncryptor;

import data.Recipes.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {	
	
	@FXML TextField user;
	@FXML PasswordField pw;
	@FXML TextField search;
	@FXML TextArea textArea;
	@FXML TreeView<String> tree;
	@FXML TableView<String> tableView;
	@FXML ListView<String> listView;
	
	private DataInterface di = new DataInterface();

    public void loginButtonClicked(){
        System.out.printf("%s logged in...\n", user.getText());  	//user: test
        System.out.printf("password %s\n", pw.getText());			//password: password
        
        //Get the fields input by the user
        String username = user.getText();
        String password = pw.getText();
        
        //Open a connection to the DB and get the encrypted password for the given username
        String encryptedPassword = di.getPassword(username);
        
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
      	Recipe recipe = di.randomRecipe();
      	recipe.setAllInfo();    	
        textArea.setText(recipe.toString());        
    }
    
    public void searchButtonClicked(){
    	String searchTerm = search.getText();
        searchTerm = searchTerm.replaceAll(" ", "%");
		ArrayList<Recipe> recipes = di.simpleSearch(searchTerm);
		populateList(recipes);	
    }
    
    public void aboutWindowGo()	{
    	System.out.println("Loading about page...");
    	AlertBox.display("About", "Awesome Inc. 2016");
    }
    
    public void populateList(ArrayList<Recipe> recipeList){
    	ObservableList<String> recipes = FXCollections.observableArrayList();
    	for (Recipe r : recipeList)
    	{
    		recipes.add(r.getTitle());
    	}
    	listView.setItems(recipes);   
    }
    
    public void populateTable(){
    	System.out.println("populating table...");  
    	
    	//TODO: make an array of recipe objects
    	ObservableList<String> recipes = FXCollections.observableArrayList("sample1", "sample2");
    	tableView.setItems(recipes);
    	
    	//TODO: Make list of recipes from array   	
    	TableColumn<String, String> recipeNamesCol = new TableColumn<>("NameTest");
    	recipeNamesCol.setCellValueFactory(new PropertyValueFactory("basicInfo"));    	
    	TableColumn<String, String> recipeDescsCol = new TableColumn<>("DescTest");
    	recipeNamesCol.setCellValueFactory(new PropertyValueFactory("basicInfo"));   
    	
    	tableView.getColumns().setAll(recipeNamesCol, recipeDescsCol);
    }

}
