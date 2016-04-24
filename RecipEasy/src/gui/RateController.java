package gui;
 
import org.jasypt.util.password.StrongPasswordEncryptor;

import data.Recipe;
import data.DatabaseInterface.DBUsersIntf;
import data.Recipes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
 
public class RateController {
    @FXML private Text actiontarget;
    @FXML TextField satRate;
    @FXML TextField costRate;
    @FXML TextField easeRate;
	@FXML private GridPane gp;
	
	private Recipe recipe;
	int userId;
	
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
                
        System.out.printf("UserName: "+userId+"\nRecipe: "+recipe.getTitle()+"\n\n%d\n%d\n%d\n", satR, costR, easeR);
        int rateId = di.selectRecipe(userId, recipe);
        di.rateRecipe(rateId, satR, costR, easeR);
        System.out.printf("Done rating");
    }
	public void setRep(Recipe rec){
		recipe=rec;
	}
	public void setUID(int ID){
		userId=ID;
	}
}