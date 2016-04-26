
package gui;
 
import data.DataGrabber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
 
public class RateController {
    @FXML private Text actiontarget;
    @FXML ComboBox<String> satRate;
    @FXML ComboBox<String> costRate;
    @FXML ComboBox<String> easeRate;
	@FXML private GridPane gp;
	
	int userID;
	int recipeID;
	
    
    @FXML protected void handleCloseButtonAction(ActionEvent event) {
        
    	try {
            Stage current = (Stage) gp.getScene().getWindow();
            current.close();
            
    } catch(Exception e) {
       e.printStackTrace();
      }
    }
@FXML protected void handleRateButtonAction(ActionEvent event) {
		DataGrabber dg = new DataGrabber();
        //Get the fields input by the user
        int satR = Integer.parseInt(satRate.getValue());
        int costR = Integer.parseInt(costRate.getValue());
        int easeR = Integer.parseInt(easeRate.getValue());

        dg.rateRecipe(userID, recipeID, satR, costR, easeR);
        dg.close();

        Stage current = (Stage) gp.getScene().getWindow();
        current.close();
    }
	public void setVars(int rec, int usr){
		recipeID=rec;
		userID = usr;
	}
}