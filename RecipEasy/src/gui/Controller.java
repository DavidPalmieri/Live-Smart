package gui;


import java.net.URL;
import java.util.ArrayList;

import org.jasypt.util.password.StrongPasswordEncryptor;

import data.Category;
import data.DataGrabber;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller {	
	
	@FXML Label user;
	@FXML Label lTitle;
	@FXML TextField search;
	@FXML TextArea taInfo;
	@FXML TextArea taSum;
	@FXML ImageView imgPic;
	@FXML TreeView<String> tree;
	@FXML TableView<String> tableView;
	@FXML ListView<String> listView;
	@FXML BorderPane bp;
	
	private User usr;
	private Recipe recipe;

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
    	DataGrabber dg = new DataGrabber();
    	populateList(dg.getFavorites(usr.getUserID()));
    	dg.close();
    }
    
    public void suggestionsClicked()
    {
    	DataGrabber dg = new DataGrabber();
    	populateList(dg.getSuggestions(usr.getUserID()));
    	dg.close();
    }

    public void randomButtonClicked(){ 
    	DataGrabber dg = new DataGrabber();
      	int recipeID = dg.getRandomRecipe();
      	recipe = new Recipe(recipeID);
      	recipe = dg.getRecipe(recipe);
      	recipe = dg.getRatings(recipe);
      	
      	Image pic=new Image("gui/NoImage.jpg", 674, 320, false, false);
      	
      	int liked = recipe.getRating().displayRating().get(0);
      	int ease = recipe.getRating().displayRating().get(1);
      	int cost = recipe.getRating().displayRating().get(2);
      	

      	ArrayList<Category> categories = recipe.getCategories();
      	String categoryText = "";
      	
      	for (int i = 0; i < categories.size(); i++)
      	{
      		if (i < categories.size() - 1)
      		{
      			categoryText += categories.get(i).getName() + ", ";
      		}
      		else
      		{
      			categoryText += categories.get(i).getName();
      		}
      	}
      	
      	
      	lTitle.setText(recipe.getTitle());
      	taInfo.setText("Rating:\nSatisfaction: "+ liked +" | Ease: "+ ease +" | Cost: "+ cost +
      			"\n\nPrep Time: "+ recipe.getPrepTime() +"\nTotal Time:"+ recipe.getTotalTime()+"\nServings: "+ recipe.getServings());
      	taSum.setText(categoryText +"\n"+ recipe.getSummary());
      	
      	imgPic.setImage(pic);
    	dg.close();
    }
    
    public void newRecipe(){
        try {
        	
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RecipePage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            RecipeController controller = fxmlLoader.< RecipeController>getController();
            controller.load(usr.getUserID(),recipe);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
            
    } catch(Exception e) {
       e.printStackTrace();
      }
    }
    
    public void searchButtonClicked(){
    	DataGrabber dg = new DataGrabber();
    	String searchTerm = search.getText();
        searchTerm = searchTerm.replaceAll(" ", "%");
		ArrayList<Recipe> recipes = dg.search(searchTerm);
		populateList(recipes);
    	dg.close();	
    }
    
    public void aboutWindowGo()	{
    	System.out.println("Loading about page...");
    	AlertBox.display("About", "Awesome Inc. 2016");
    }
    
    public void populateList(ArrayList<Recipe> recipeList){
    	DataGrabber dg = new DataGrabber();
    	ObservableList<String> recipes = FXCollections.observableArrayList();
    	for (Recipe r : recipeList){
    		r = dg.getRecipe(r);
    		recipes.add(r.getTitle());
    	}
    	listView.setItems(recipes);
    	dg.close();   
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