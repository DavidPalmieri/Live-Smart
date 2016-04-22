package gui;

import java.util.List;
import java.util.Random;

import data.DatabaseInterface.DBRecipeIntf;
import data.DatabaseInterface.DBUsersIntf;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import data.Recipes.BasicInfo;
import data.Recipes.Recipe;
import data.Users.Rating;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;

public class RecipeController {	
	
	@FXML Label lTitle;
	@FXML Label lCatVal;
	@FXML Label lRateVal;
	@FXML Label lBasicInfo;
	@FXML TextArea lNutriInfo;
	@FXML TextArea taInstructions;
	@FXML TextArea taIngredients;
	@FXML ImageView imgPic;
	@FXML Button butRate;
	@FXML Button butBack;

   
    public void randomButtonClicked(){
    	System.out.println("Displaying random recipe...");      

        //First, create the object that queries the database
      	DBRecipeIntf queryDB = new DBRecipeIntf();
      	//next, create a new recipe object using the recipeID returned from the randomRecipe method
      	Recipe recipe = new Recipe(queryDB.randomRecipe());
      	//Now close the Database object
      	queryDB.close();
      	//The newly created recipe is only holding a minor amount of its information (Title, times, summary, etc.)
      	//Next, use the setAllInfo to get the rest of the recipe info (Nutrition, ingredients, instructions, etc.)
      	recipe.setAllInfo();
      	//Now, you can use the toString method wherever you need it, or the basicInfo method for menus
      	System.out.printf("%d\n", recipe.getRecipeID());         	   
      	
     	Rating rate=recipe.getAvgRating();
      	BasicInfo info=recipe.getBasicInfo();
      	
//      	until picture getting method is made
//      	Image pic=recipe.Picture;
      	Image pic=new Image("gui/Noimage.jpg");
      	
      	
      	//set all the values in the recipe page
      	lTitle.setText(info.getTitle());
      	lCatVal.setText(recipe.getCategories());
    	lRateVal.setText("Rating: "+Integer.toString(rate.getLiked()));
    	lBasicInfo.setText("Prep Time: "+info.getPrepTime()+"\nTotal Time:"+info.getTotalTime()+"\n\nServings"+info.getServings()+"\nSummary:"+info.getSummary());
      	lNutriInfo.setText(recipe.getNutrition());
    	taInstructions.setText(recipe.getInstructions()+"\n\n"+recipe.getTips());;
    	taIngredients.setText(recipe.getIngredients());
    	imgPic.setImage(pic);
        
    }
}