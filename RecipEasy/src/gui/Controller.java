package gui;


import java.io.File;
import java.util.ArrayList;

import data.DataGrabber;
import data.Recipe;
import data.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
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
    	recipe = null;
        selectedRecipeChanged();
    	DataGrabber dg = new DataGrabber();
    	populateList(dg.getFavorites(usr.getUserID()));
    	dg.close();
    }
    
    public void suggestionsClicked()
    {
    	recipe = null;
        selectedRecipeChanged();
    	DataGrabber dg = new DataGrabber();
    	populateList(dg.getSuggestions(usr.getUserID()));
    	dg.close();
    }

    public void randomButtonClicked(){
    	recipe = null;
        selectedRecipeChanged(); 
    	DataGrabber dg = new DataGrabber();
      	int recipeID = dg.getRandomRecipe();
      	recipe = new Recipe(recipeID);
      	recipe = dg.getRecipe(recipe);
      	recipe = dg.getRatings(recipe);

        selectedRecipeChanged();
        
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
    	recipe = null;
        selectedRecipeChanged();
    	DataGrabber dg = new DataGrabber();
    	String searchTerm = search.getText();
    	
    	if (!searchTerm.equalsIgnoreCase(""))
    	{
    		searchTerm = searchTerm.replaceAll(" ", "%");
    		ArrayList<Recipe> recipes = dg.search(searchTerm);
    		populateList(recipes);
    	}
        
    	dg.close();	
    }
    
    
    public void populateList(ArrayList<Recipe> recipeList){
    	imgPic.setImage(null);
    	DataGrabber dg = new DataGrabber();
    	ObservableList<String> recipes = FXCollections.observableArrayList();
    	for (Recipe r : recipeList){
    		r = dg.getRecipe(r);
    		recipes.add(r.getTitle());
    	}
    	
    	
    	listView.setItems(recipes);
    	
    	listView.getSelectionModel().selectedItemProperty()
    	.addListener(new ChangeListener<String>() {
        	public void changed(ObservableValue<? extends String> ov,
              String old_val, String new_val) {
            int num = listView.getSelectionModel().getSelectedIndex();
            if (num != -1)
            {
            	recipe = recipeList.get(num);
            	
            }
          	
            selectedRecipeChanged();
          }
        });
    	dg.close(); 
    }
    
    public void selectedRecipeChanged()
    {
    	if (recipe == null)
    	{
    		lTitle.setText("");
          	taInfo.setText("");
          	taSum.setText("");
    	}
    	else
    	{
    		Image img = null;

        	File imgPath = new File("RecipePictures/" + recipe.getTitle() + ".jpg");
        	if (imgPath.isFile())
        	{
        		img = new Image(imgPath.toURI().toString());
        	}
        	else
        	{
        		img = new Image(new File("RecipePictures/NoImage.jpg").toURI().toString());
        	}
    		
    		
    		int liked = recipe.getRating().displayRating().get(0);
          	int ease = recipe.getRating().displayRating().get(1);
          	int cost = recipe.getRating().displayRating().get(2);

          	lTitle.setText(recipe.getTitle());
          	taInfo.setText("Rating:\nSatisfaction: "+ liked +" | Ease: "+ ease +" | Cost: "+ cost +
          			"\n\nPrep Time: "+ recipe.getPrepTime() +"\nTotal Time:"+ recipe.getTotalTime()+"\nServings: "+ recipe.getServings());
          	taSum.setText(recipe.getCategories() +"\n"+ recipe.getSummary());
          	
          	imgPic.setImage(img);
          	
    	}
    }
}