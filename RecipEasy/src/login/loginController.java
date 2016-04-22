

package login;
 
import org.jasypt.util.password.StrongPasswordEncryptor;

import data.DatabaseInterface.DBUsersIntf;
import gui.Controller;
import gui.DataInterface;
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
 
public class loginController {
    @FXML private Text actiontarget;
    @FXML private TextField user;
	@FXML private PasswordField pw;
	@FXML private GridPane gp;
	
	DataInterface di = new DataInterface();
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        
        
        System.out.printf("%s logged in...\n", user.getText());  	//user: test
        System.out.printf("password %s\n", pw.getText());			//password: password
        
        //Get the fields input by the user
        String username = user.getText();
        String password = pw.getText();
        
        //Open a connection to the DB and get the encrypted password for the given username
        String encryptedPassword = di.getPassword(username);
        
        int userID = di.getUserID(username); //If username is not found in database, userID of 0 is returned
        if (userID != 0){ 
        	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    		if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
    			//success, the input password matches the password on file
    			System.out.println("Successful password match\n");
    			
    			
    			try {
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
    		else {
    			//fail, the passwords do not match
    			System.out.println("invalid password\n");
    			actiontarget.setText("invalid password");
    		}	
        } else { // empty password string = username not found in Users table
        	System.out.println("username not found\n");
        	actiontarget.setText("username not found");
        }
    }
    
@FXML protected void handleRegistrationButtonAction(ActionEvent event) {
	 
	
	
	try {
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_register.fxml"));
	                Parent root1 = (Parent) fxmlLoader.load();
	                Stage stage = new Stage();
	                stage.setTitle("RecipEasy Registration");
	                stage.setScene(new Scene(root1,300,300));  
	                stage.show();
	                Stage current = (Stage) gp.getScene().getWindow();
	                current.hide();
	        } catch(Exception e) {
	           e.printStackTrace();
	          }
        
        
	}
}
