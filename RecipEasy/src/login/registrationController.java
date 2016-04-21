

package login;
 
import org.jasypt.util.password.StrongPasswordEncryptor;

import data.DatabaseInterface.DBUsersIntf;
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
	@FXML PasswordField pw;
	@FXML GridPane gp;
    
/*    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        
        
        System.out.printf("%s logged in...\n", user.getText());  	//user: test
        System.out.printf("password %s\n", pw.getText());			//password: password
        
        //Get the fields input by the user
        String username = user.getText();
        String password = pw.getText();
        
        //Open a connection to the DB and get the encrypted password for the given username
        DBUsersIntf dbLookup = new DBUsersIntf();
        String encryptedPassword = dbLookup.getPassword(username);
        dbLookup.close();
        
        //Use Jasypt's password encryptor to verify whether the plain-text and encrypted passwords match
        if (!encryptedPassword.equalsIgnoreCase("")){
        	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    		if (passwordEncryptor.checkPassword(password, encryptedPassword)) {
    			//success, the input password matches the password on file
    			System.out.println("Successful password match\n");
    			actiontarget.setText("Successful password match");
    		}
    		else {
    			//fail, the passwords do not match
    			System.out.println("unsuccessful password match\n");
    			actiontarget.setText("unsuccessful password match");
    		}	
        } else { // empty password string = username not found in Users table
        	System.out.println("username not found\n");
        	actiontarget.setText("username not found");
        }
    }
   */ 
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
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

