package gui;

import data.Recipe;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Main extends Application {

	TableView<Recipe> table;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        primaryStage.setTitle("RecipEasy");
        
        //name column
        TableColumn<Recipe, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        //table
        table = new TableView<>();
        table.setItems(getRecipe());
        table.getColumns().addAll(nameColumn);
        
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    //Get all of the recipes
    public ObservableList<Recipe> getRecipe(){
		ObservableList<Recipe> recipes = FXCollections.observableArrayList();
		recipes.add(new Recipe("Hamburger"));
		recipes.add(new Recipe("Pizza"));
		recipes.add(new Recipe("Ice Cream"));
    	return recipes;    	
    }
}
