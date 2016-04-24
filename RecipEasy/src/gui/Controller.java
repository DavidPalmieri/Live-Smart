package gui;


import java.util.ArrayList;

import org.jasypt.util.password.StrongPasswordEncryptor;

import data.Recipe;
import data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller {	
	
	@FXML Label user;
	@FXML PasswordField pw;
	@FXML TextField search;
	@FXML TextArea textArea;
	@FXML TreeView<String> tree;
	@FXML TableView<String> tableView;
	@FXML ListView<String> listView;
	@FXML BorderPane bp;
	
	private User usr;
	private DataInterface di = new DataInterface();

    public void logoutButtonClicked(){
        Stage current = (Stage) bp.getScene().getWindow();
        current.hide();
        try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login/fxml_login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
            
    } catch(Exception e) {
       e.printStackTrace();
      }
    }
    
    public void setUser(int userID, String userName)
    {
    	usr = new User(userID, userName);
    }
    
    public void setUserText(String userName)
    {
    	user.setText("    Hello, " + userName);
    }
    
    public void favoritesClicked()
    {
    	populateList(di.getFavoriteRecipes(di.getFavoriteRatings(usr)));
    }
    
    public void suggestionsClicked()
    {
    	populateList(di.getSuggestedRecipes(di.getFavoriteRatings(usr)));
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
    	for (Recipe r : recipeList){
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
