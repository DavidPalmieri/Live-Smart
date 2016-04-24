
package gui;
 
import data.DataGrabber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
 
public class RateController {
    @FXML private Text actiontarget;
    @FXML TextField satRate;
    @FXML TextField costRate;
    @FXML TextField easeRate;
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
@FXML protected void handleSubmitButtonAction(ActionEvent event) {
		DataGrabber dg = new DataGrabber();
        //Get the fields input by the user
        int satR = Integer.parseInt(satRate.getText());
        int costR = Integer.parseInt(costRate.getText());
        int easeR = Integer.parseInt(easeRate.getText());

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