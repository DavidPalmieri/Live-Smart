package gui;


import java.io.File;
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

/**
 * The RecipeController is the code behind the RecipePage.
 * It contains the user and recipe IDs as well as the 
 * JavaFX controls that will be accessed by the controller
 * It also has handlers for closing the window as well as 
 * opening a window for Rating the recipe and cresting a 
 * new random recipe.
 */
public class RecipeController {	
	
	//Give the controller access to graphic controls
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
	
	//stores the user and recipe IDs
	private Recipe recipe;
	private int uID;	
	
	/*
	 * Pops up the rating page to allow for rating recipes 
	 * as well as set the userID and the recipeID in the rate controller
	 */
	public void ratingButClick(){
		try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RatingPage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            RateController controller = fxmlLoader.<RateController>getController();
            controller.setVars(recipe, uID,this);
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
	
	/*
	 * Loads the recipe page as well sets the instance variables
	 * 
	 * Contains a DataGrabber to interact with the database
	 * Then it sets the instance variables and gets the recipe
	 * information. Then it calls the setText method to display
	 * the info.
	 */
	public void load(int uID, Recipe r)
	{
		//Creates the DataGrabber which is used to access the database
		DataGrabber dg = new DataGrabber();
		//set the instance variables
		this.uID=uID;
		recipe = r;
      	recipe = dg.getRecipeDetails(recipe);
      	//Close the DataGrabber
      	dg.close();
      	//Calls the setText method to display the info to the page
      	setText(recipe);
	}
	

   //generate a random recipe and display the info for testing.
    public void randomButtonClicked(){
    	DataGrabber dg = new DataGrabber();
    	System.out.println("Displaying random recipe...");     
    	
      	//generates a random recipe id and makes a new recipe with it
      	int recipeID = dg.getRandomRecipe();
      	recipe = new Recipe(recipeID);
      	
      	//set the details for the recipe
      	recipe = dg.getRecipe(recipe);
      	recipe = dg.getRatings(recipe);
      	recipe = dg.getRecipeDetails(recipe);

      	dg.close();

      	//Calls the setText method to display recipe to the page
      	setText(recipe);      
    }
    
    //Displays all of the recipe info
    public void setText(Recipe recipe)
    {
    	
    	//Finds and displays the image into an imageview
    	Image img = null;
    	File imgPath = new File("RecipePictures/" + recipe.getTitle() + ".jpg");
    	if (imgPath.isFile())
    	{
    		img = new Image(imgPath.toURI().toString());
    	}
    	else
    	{
    		//If the file is not found set it to be the NoImage
    		img = new Image(new File("RecipePictures/NoImage.jpg").toURI().toString());
    	}
    	
    	//gets the ratings for the recipe
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
    	imgPic.setImage(img);
    }
}