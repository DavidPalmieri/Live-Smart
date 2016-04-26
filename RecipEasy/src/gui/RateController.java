
package gui;
 
import data.DataGrabber;
import data.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * The RateController is the code behind the RatingPage.
 * It contains the user and recipe IDs as well as the 
 * JavaFX controls that will be accessed by the controller
 * It also has handlers for closing the window with and 
 * without rating the recipe
 */
public class RateController {
	//gives the controller access to the JavaFX controls
    @FXML private Text actiontarget;
    @FXML ComboBox<String> satRate;
    @FXML ComboBox<String> costRate;
    @FXML ComboBox<String> easeRate;
	@FXML private GridPane gp;
	
	//stores the user and recipe IDs
	private int userID;
	private Recipe recipe;
	private RecipeController controller;
	
    /*
     * Handles the close button being clicked
     * Closes the current window, with no interaction to the database
     */
    @FXML protected void handleCloseButtonAction(ActionEvent event) {
        
    	try {
            Stage current = (Stage) gp.getScene().getWindow();
            current.close();
            
    } catch(Exception e) {
       e.printStackTrace();
      }
    }
    /*
     * Handles the submit button action
     * The method contains the ratings for satisfaction, cost, and ease
     * It also calls a DataGrabber, which is then used to apply the ratings
     * 
     * It then closes the window 
     */
@FXML protected void handleRateButtonAction(ActionEvent event) {
		//Creates the DataGrabber which is used to access the database
		DataGrabber dg = new DataGrabber();
		
        //Get the input values by the user
        int satR = Integer.parseInt(satRate.getValue());
        int costR = Integer.parseInt(costRate.getValue());
        int easeR = Integer.parseInt(easeRate.getValue());
        
        System.out.printf("like: %d, ease: %d, cost: %d", satR,easeR,costR);
        
        //adds the ratings into the data base with the user and recipe IDs
        dg.rateRecipe(userID, recipe.getRecipeID(), satR,easeR, costR);
        recipe=dg.getRatings(recipe);
        recipe.getRating().setUserRating(satR, easeR, costR);
        controller.load(userID, recipe);
        
        dg.close();
        
        //closes the current window
        Stage current = (Stage) gp.getScene().getWindow();
        current.close();
    }

	/* 
	 * method to set the instance variables
	 * It is called in the RecipeController and
	 * passes the UserID and RecipeID to this controller 
	 */
	public void setVars(Recipe rec, int usr, RecipeController con){
		recipe=rec;
		userID = usr;
		controller=con;
	}
}