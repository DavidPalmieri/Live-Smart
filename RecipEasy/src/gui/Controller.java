package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class Controller {
	
	@FXML
	TreeView<String> tree;
	TreeItem<String> recipesView = null;

    public void loginButtonClicked(){
        System.out.println("User logged in...");
    }

    public void settingsButtonClicked(){
        System.out.println("Going to Settings Menu...");
    }
    
    public void aboutWindowGo()	{
    	System.out.println("Loading about page...");
    	AlertBox.display("About", "Awesome Inc. 2016");
    }

}
