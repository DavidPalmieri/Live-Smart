

package login;
 
import org.jasypt.util.password.StrongPasswordEncryptor;

import data.DataGrabber;
import gui.Controller;
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
 
public class registrationController {
    @FXML private Text actiontarget;
    @FXML TextField user;
	@FXML PasswordField passwordField;
	@FXML PasswordField passwordField1;
	@FXML GridPane gp;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        
        DataGrabber dg = new DataGrabber();
        
      //Get the fields input by the user
        String username = user.getText();
        String password = passwordField.getText();
        String passwordCheck = passwordField1.getText();
		
		int userID = dg.getUserID(username);
		if (userID != 0)
		{
			actiontarget.setText("Username already taken");
		}
		else
		{
			if (!password.contentEquals(passwordCheck))
			{
				actiontarget.setText("Passwords do not match");
			}
			else
			{
				StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
				String encryptedPassword = passwordEncryptor.encryptPassword(password);	
				dg.newUser(username, encryptedPassword);
				
				System.out.println("Account successfully created");

				userID = dg.getUserID(username);
				try {
					dg.close();
        			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/homePage.fxml"));
    	            Parent root1 = (Parent) fxmlLoader.load();
    	            Controller controller = fxmlLoader.<Controller>getController();
    	            controller.setUser(userID, username);
    	            controller.setUserText(username);
    	            Stage stage = new Stage();
    	            stage.setScene(new Scene(root1));  
    	            stage.show();
    	            Stage current = (Stage) gp.getScene().getWindow();
    	            current.hide();
	                
	        } catch(Exception e) {
	           e.printStackTrace();
	        	}
			}
			
		}
    }
   
    @FXML protected void handleCancelButtonAction(ActionEvent event) {
	 
    	try {
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_login.fxml"));
	                Parent root1 = (Parent) fxmlLoader.load();
	                Stage stage = new Stage();
	                stage.setTitle("RecipEasy");
	                stage.setScene(new Scene(root1,300,300));  
	                stage.show();
	                Stage current = (Stage) gp.getScene().getWindow();
	                current.hide();
	        } catch(Exception e) {
	           e.printStackTrace();
	          }
        
        
	}
	
}

