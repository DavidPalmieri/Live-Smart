package front;



 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
 
public class frontPageController {
    @FXML private Text actionTarget;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actionTarget.setText("");
        
    }

}
