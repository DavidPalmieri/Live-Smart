package gui;

public class Controller {	

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
