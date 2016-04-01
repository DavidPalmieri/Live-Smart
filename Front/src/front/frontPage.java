package front;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class frontPage extends Application {
    
    public static void main(String[] args) {
        Application.launch(frontPage.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml_frontPage.fxml"));
        
        stage.setTitle("RecipEasy");
        stage.setScene(new Scene(root, 300, 300));
        stage.show();
    }
}
