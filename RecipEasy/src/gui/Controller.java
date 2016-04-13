package gui;

import java.util.Random;

import org.jasypt.util.password.StrongPasswordEncryptor;

import data.DBUsersIntf;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {	
	
	@FXML TextField user;
	@FXML PasswordField pw;
	@FXML TextField search;

    public void loginButtonClicked(){
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
    		}
    		else {
    			//fail, the passwords do not match
    			System.out.println("unsuccessful password match\n");
    			AlertBox.display("Invalid Password", "Password does not match!");
    		}	
        } else { // empty password string = username not found in Users table
        	System.out.println("username not found\n");
        	AlertBox.display("Error", "User not found!");
        }
        
    }

    public void randomButtonClicked(){
        System.out.println("Displaying random recipe...\n");
        DBUsersIntf dbLookup = new DBUsersIntf();
        Random random = new Random();        
        System.out.printf("%d\n", random.nextInt());
        dbLookup.close();
    }
    
    public void searchButtonClicked(){
        System.out.printf("Searching for %s...\n", search.getText());
    }
    
    public void aboutWindowGo()	{
    	System.out.println("Loading about page...");
    	AlertBox.display("About", "Awesome Inc. 2016");
    }

}
