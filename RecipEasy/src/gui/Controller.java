package gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {	
	
	@FXML TextField user;
	@FXML PasswordField pw;

    public void loginButtonClicked(){
        System.out.printf("%s logged in...\n", user.getText());  
        System.out.printf("password %s\n", pw.getText());
    }

    public void settingsButtonClicked(){
        System.out.println("Going to Settings Menu...");
    }
    
    public void aboutWindowGo()	{
    	System.out.println("Loading about page...");
    	AlertBox.display("About", "Awesome Inc. 2016");
    }

}
