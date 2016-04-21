

package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class login extends Application {
    
    public static void main(String[] args) {
        Application.launch(login.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml_login.fxml"));
        
        stage.setTitle("RecipEasy");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();
    }
}
