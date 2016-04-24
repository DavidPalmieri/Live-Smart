

package gui;
 
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
	
	int rateId;
	
	DataInterface di = new DataInterface();
    
    @FXML protected void handleCloseButtonAction(ActionEvent event) {
        
    	try {
            Stage current = (Stage) gp.getScene().getWindow();
            current.close();
            
    } catch(Exception e) {
       e.printStackTrace();
      }
    }
@FXML protected void handleSubmitButtonAction(ActionEvent event) {
        DataInterface di=new DataInterface();
        //Get the fields input by the user
        int satR = Integer.parseInt(satRate.getText());
        int costR = Integer.parseInt(costRate.getText());
        int easeR = Integer.parseInt(easeRate.getText());

        di.rateRecipe(rateId, satR, costR, easeR);

        Stage current = (Stage) gp.getScene().getWindow();
        current.close();
    }
	public void setRtID(int r){
		rateId=r;
	}
}