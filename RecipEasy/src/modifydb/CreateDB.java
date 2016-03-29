package modifydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDB 
{
	
	private static String dbURL = "jdbc:derby:RecipeDB;create=true;";
    private static Connection conn = null;
	
    public static void main(String[] args) throws SQLException
    {

        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(dbURL);
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }

        clearDB();
        createTables();
        
    }
    
    private static void createTables() throws SQLException
    {
        conn.createStatement().execute("create table Recipe "
                + "(RecipeID  int not null generated always as identity (start with 1, increment by 1), "
                + "Title varchar(100) not null, "
                + "Url varchar(200) not null,  "
                + "PrepTime varchar(23) , "
                + "TotalTime varchar(23), "
                + "Servings varchar(3), "
                + "Summary varchar(500), "
                + "ServingSize varchar(75), "
                + "Calories varchar(10), "
                + "CalFat Varchar(10), "
                + "TotFat varchar(10), "
                + "SatFat varchar(10), "
                + "TransFat varchar(10), "
                + "Cholesterol varchar(10), "
                + "Sodium varchar(10), "
                + "Carbs Varchar(10), "
                + "Fiber varchar(10), "
                + "Sugar varchar(10), "
                + "Protein varchar(10), "
                + "VitA varchar(10), "
                + "VitC varchar(10), "
                + "Calcium varchar(10), "
                + "Iron varchar(10))");
        
        conn.createStatement().execute("create table Tip "
                + "(TipID int not null generated always as identity (start with 1, increment by 1),"
                + " Tip varchar(2000) not null)");
        
        conn.createStatement().execute("create table Instruction "
                + "(InstructionID int not null generated always as identity (start with 1, increment by 1), "
                + "Instruction varchar(2000) not null)");
        
        conn.createStatement().execute("create table Rating "
                + "(RatingID int not null generated always as identity (start with 1, increment by 1),"
                + " UserID int not null, "
                + "RecipeID int not null, "
                + "Cost int, "
                + "Ease int, "
                + "Liked int)");
        
        conn.createStatement().execute("create table Ingredient "
                + "(IngredientID int not null generated always as identity (start with 1, increment by 1), "
                + "Ingredient varchar(250) not null)");
        
        conn.createStatement().execute("create table Category "
                + "(CategoryID int not null generated always as identity (start with 1, increment by 1), "
                + "category varchar(100) not null)");
        
        conn.createStatement().execute("create table RecipeTip "
                + "(RecTipID int not null generated always as identity (start with 1, increment by 1), "
                + "RecipeID int not null, "
                + "TipID int not null)");
        
        conn.createStatement().execute("create table RecipeInstruction "
                + "(RecInstID int not null generated always as identity (start with 1, increment by 1), "
                + "RecipeID int not null, "
                + "RecInstStepID int not null)");
        
        conn.createStatement().execute("create table RecipeInstructionStep "
                + "(RecInstStepID int not null generated always as identity (start with 1, increment by 1), "
                + "StepID int not null, "
                + "InstructionID int not null)");
        
        conn.createStatement().execute("create table RecipeIngredient "
                + "(RecIngID int not null generated always as identity (start with 1, increment by 1), "
                + "RecipeID int not null, "
                + "IngredientID int not null)");
       
        conn.createStatement().execute("create table RecipeCategory "
                + "(RecCatID int not null generated always as identity (start with 1, increment by 1),"
                + " RecipeID int not null, "
                + "CategoryID int not null)");
        
        conn.createStatement().execute("create table Users "
                + "(UserID int not null generated always as identity (start with 1, increment by 1), "
                + "Username varchar(50) not null, "
                + "Password varchar(16) not null)");
        
        conn.createStatement().execute("Alter table Users "
                + "add constraint PK_UserID Primary Key (UserID)");
        
        conn.createStatement().execute("Alter table RecipeCategory "
                + "add constraint PK_RecCatID Primary Key (RecCatID)");
        
        conn.createStatement().execute("Alter table RecipeIngredient "
                + "add constraint PK_RecIngID Primary Key (RecIngID)");
        
        conn.createStatement().execute("Alter table RecipeInstructionStep "
                + "add constraint PK_RecInstStepID Primary Key (RecInstStepID)");
        
        conn.createStatement().execute("Alter table RecipeInstruction "
                + "add constraint PK_RecInstID Primary Key (RecInstID)");
        
        conn.createStatement().execute("Alter table RecipeTip "
                + "add constraint PK_RecIntID Primary Key (RecTipID)");
        
        conn.createStatement().execute("Alter table Category "
                + "add constraint PK_CategoryID Primary Key (CategoryID)");
        
        conn.createStatement().execute("Alter table Rating "
                + "add constraint PK_RatingID Primary Key (RatingID)");
        
        conn.createStatement().execute("Alter table Ingredient "
                + "add constraint PK_IngredientID Primary Key (IngredientID)");
        
        conn.createStatement().execute("Alter table Instruction "
                + "add constraint PK_InstructionID Primary Key (InstructionID)");
        
        conn.createStatement().execute("Alter table Tip "
                + "add constraint PK_TipID Primary Key (TipID)");
        
        conn.createStatement().execute("Alter table Recipe "
                + "Add Constraint PK_RecipeID Primary Key (RecipeID)");
        
        
        conn.createStatement().execute("Alter table RecipeCategory "
                + "add constraint FK_CatRecID Foreign Key (RecipeID) References Recipe (RecipeID)");
        
        conn.createStatement().execute("Alter table RecipeCategory "
                + "add constraint FK_CategoryID Foreign Key (CategoryID) References Category (CategoryID)");
        
        conn.createStatement().execute("Alter table RecipeIngredient "
                + "add constraint FK_IngRecID Foreign Key (RecipeID) references Recipe (RecipeID)");
        
        conn.createStatement().execute("Alter table RecipeIngredient "
                + "add constraint FK_IngredientID Foreign Key (IngredientID) references Ingredient (IngredientID)");
        
        conn.createStatement().execute("Alter table RecipeInstructionStep "
                + "add constraint FK_InstructionID Foreign Key (InstructionID)  References Instruction (InstructionID)");
        
        conn.createStatement().execute("Alter table RecipeInstruction "
                + "add constraint FK_InstRecID Foreign Key (RecipeID) References Recipe (RecipeID)");
        
        conn.createStatement().execute("Alter table RecipeInstruction "
                + "add constraint FK_RecInstStepID Foreign Key (RecInstStepID) References RecipeInstructionStep (RecInstStepID)");
        
        conn.createStatement().execute("Alter table RecipeTip "
                + "add constraint FK_TipRecID Foreign Key (RecipeID) References Recipe (RecipeID)");
        
        conn.createStatement().execute("Alter table RecipeTip "
                + "add constraint FK_TipID Foreign Key (TipID) references Tip (TipID)");
        
        conn.createStatement().execute("Alter table Rating "
                + "add constraint FK_RatRecID Foreign Key (RecipeID) References Recipe (RecipeID)");
        
        conn.createStatement().execute("Alter table Rating "
                + "add constraint FK_UserID Foreign Key (UserID) References Users (UserID)");
    }
    
    public static void clearDB() throws SQLException
    {
Statement stmt = conn.createStatement();
    	
    	conn.createStatement().execute("Alter table RecipeCategory drop constraint FK_CatRecID");        
    	conn.createStatement().execute("Alter table RecipeCategory drop constraint FK_CategoryID");      
        conn.createStatement().execute("Alter table RecipeIngredient drop constraint FK_IngRecID");       
        conn.createStatement().execute("Alter table RecipeIngredient drop constraint FK_IngredientID");       
        conn.createStatement().execute("Alter table RecipeInstructionStep drop constraint FK_InstructionID");       
        conn.createStatement().execute("Alter table RecipeInstruction drop constraint FK_InstRecID");       
        conn.createStatement().execute("Alter table RecipeInstruction drop constraint FK_RecInstStepID");       
        conn.createStatement().execute("Alter table RecipeTip drop constraint FK_TipRecID");       
        conn.createStatement().execute("Alter table RecipeTip drop constraint FK_TipID");       
        conn.createStatement().execute("Alter table Rating drop constraint FK_RatRecID");       
        conn.createStatement().execute("Alter table Rating drop constraint FK_UserID");
    	
    	stmt.execute("Delete from recipetip");
        stmt.execute("Delete from recipeinstruction");
        stmt.execute("Delete from recipeinstructionstep");
        stmt.execute("Delete from recipeingredient");
        stmt.execute("Delete from tip");
        stmt.execute("Delete from instruction");
        stmt.execute("Delete from ingredient");
        stmt.execute("Delete from Recipe");
        
        stmt.execute("Drop Table Recipe");
        stmt.execute("Drop Table Tip");
        stmt.execute("Drop Table Instruction");
        stmt.execute("Drop Table Rating");
        stmt.execute("Drop Table Ingredient");
        stmt.execute("Drop Table Category");
        stmt.execute("Drop Table RecipeTip");
        stmt.execute("Drop Table RecipeInstruction");
        stmt.execute("Drop Table RecipeIngredient");
        stmt.execute("Drop Table RecipeInstructionStep");
        stmt.execute("Drop Table RecipeCategory");
        stmt.execute("Drop Table Users");
        
    }
}


