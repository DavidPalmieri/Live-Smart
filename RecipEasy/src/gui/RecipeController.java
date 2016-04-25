package gui;


import java.util.ArrayList;

import data.Category;
import data.DataGrabber;
import data.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RecipeController {	
	
	//give the controller access to graphic controls
	@FXML Label lTitle;
	@FXML Label lCatVal;
	@FXML Label lRateVal;
	@FXML Label lBasicInfo;
	@FXML TextArea lNutriInfo;
	@FXML TextArea taInstructions;
	@FXML TextArea taIngredients;
	@FXML ImageView imgPic;
	@FXML Button butRate;
	@FXML Button butBack;
	
	@FXML private AnchorPane aPane;
	
	private Recipe recipe;
	private int uID;	
	
	//pop up the rating page to allow for rating recipes
	public void ratingButClick(){
		try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RatingPage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            RateController controller = fxmlLoader.<RateController>getController();
            controller.setVars(recipe.getRecipeID(), uID);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
		
            
    } 
		catch(Exception e) {
       e.printStackTrace();
      }
	} 
	
	// closes the window
	public void closeButClick(){
		try {
            Stage current = (Stage) aPane.getScene().getWindow();
            current.close();
            
    } catch(Exception e) {
       e.printStackTrace();
      }
	} 
	
	
	public void load(int uID, Recipe r){
		DataGrabber dg = new DataGrabber();
		this.uID=uID;
		recipe = r;
      	recipe = dg.getRecipeDetails(recipe);
      	dg.close();
      	setText(recipe);
      	
	}
	

   //generate a random recipe and display the info for testing.
    public void randomButtonClicked(){
    	DataGrabber dg = new DataGrabber();
    	System.out.println("Displaying random recipe...");      

        //First, create the object that queries the database
      	
      	//next, create a new recipe object using the recipeID returned from the randomRecipe method
      	int recipeID = dg.getRandomRecipe();
      	Recipe recipe = new Recipe(recipeID);
      	//The newly created recipe is only holding a minor amount of its information (Title, times, summary, etc.)
      	//Next, use the setAllInfo to get the rest of the recipe info (Nutrition, ingredients, instructions, etc.)
      	recipe = dg.getRecipe(recipe);
      	recipe = dg.getRatings(recipe);
      	recipe = dg.getRecipeDetails(recipe);
      	//Now, you can use the toString method wherever you need it, or the basicInfo method for menus
      	System.out.printf("%d\n", recipe.getRecipeID()); 

      	dg.close();

      	setText(recipe);
    	
    	
        
    }
    
    public void setText(Recipe recipe)
    {
    	DataGrabber dg = new DataGrabber();
//  	until picture getting method is made
//  	Image pic=recipe.Picture;
    	Image pic=new Image("gui/NoImage.jpg", 285, 230, false, false);
      	
      	int liked = recipe.getRating().displayRating().get(0);
      	int ease = recipe.getRating().displayRating().get(1);
      	int cost = recipe.getRating().displayRating().get(2);    	
      	
      	//set all the values in the recipe page
      	lTitle.setText(recipe.getTitle());
      	lCatVal.setText(recipe.getCategories());
    	lRateVal.setText("Ratings: Satisfaction: "+liked+" | Ease: "+ease+" | Cost: "+cost);
    	lBasicInfo.setText("Prep Time: "+recipe.getPrepTime()+"\nTotal Time:"+recipe.getTotalTime()+"\n\nServings: "+recipe.getServings()+"\nSummary: "+recipe.getSummary());
      	lNutriInfo.setText(recipe.getNutrition());
    	taInstructions.setText(recipe.getInstructions()+"\n\n"+recipe.getTips());
    	taIngredients.setText(recipe.getIngredients());
    	imgPic.setImage(pic);
    	dg.close();
    }
}