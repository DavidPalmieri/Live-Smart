//////////////////////////////////////////////////////////////////////////////////////
//																					//
//	DBInstructionIntf - Database Interface for Instruction Table					//
//	Author - Chris Costa															//
//																					//
//	Description: This class provides methods for interacting with information in 	//
//	the Tip table of the database.													//
//																					//
//																					//
//	Instance Variables:																//
//																					//
//		conn: A private object of type Connection (java.sql) used to direct access 	//
//			to the database.  Initialized in the constructor.						//
//																					//
//																					//
//	Constructors																	//
//																					//
//		Constructor: Creates a connection to the embedded database that all 		//
//			subsequent methods use.													//
//			Parameters: none.														//
//																					//
//																					//
//	Public Methods																	//
//																					//
//		getInstructions: Returns an array of Instructions, in order, for the recipe //
//			that pertains to the RecipeID given.  This method contains a complex 	//
//			join of 3 tables that makes sure that the Instructions are in order;	//
//			RecipeInstructionStep table lists the steps in correct order to due the //
//			order that the recipe instructions were inserted into the database, and //
//			the fact that the RecipeInstructionStep table contains no duplicate 	//
//			information.  The Instructions can be placed in any order in the 		//
//			Instruction table since the relation between the RecipeID and the 		//
//			InstructionID are held in a different table. (tables: RecipeInstruction,//
//			RecipeInstructionStep, Instruction, columns: RecipeID, Instruction,		//
//			joins: RecInstStepID, InstructionID)									//
//			Parameters: recipeID - the id of the recipe to be used for query.		//
//			Returns: instructions - Array of Strings used to store the text of each //
//			instruction relating to the recipe in the correct order.				//																					
//																					//
//		close: closes the connection to the database, if it is open.				//
//			Parameters: none.														//
//			Returns: nothing.														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////

package data.DatabaseInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBInstructionIntf //Database Interface for Instruction Table.
{
	//Database connection used throughout this class.
	private Connection conn;
	
	//Attempt to connect to the database. Always call the close method when finished with the DBUCategoryIntf object.
	public DBInstructionIntf()
	{
		try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:RecipeDB;create=false;");
        }
        catch (Exception e) { e.printStackTrace(); }
		
	}
	
	//Attempt to return an array of all instructions that are contained in a recipe (using RecipeID).
	public String[] getInstructions(int recipeID)
	{
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String[] instructions = {""}; 
		
		try //Attempt to query the RecipeInstruction table for RecInstStepIDs (a special table that lists the steps in correct order) 
		{	//relating to the recipeID, and join the RecipeInstructionStep table to get the InstructionID of the step. Finally, join the
			//Instruction table to get the Instruction using that InstructionID.
			pstmt = conn.prepareStatement("Select Instruction.Instruction " 
					+ "From Instruction Inner Join RecipeInstructionStep "
					+ "On Instruction.InstructionID = RecipeInstructionStep.InstructionID Join RecipeInstruction "
					+ "On RecipeInstructionStep.RecInstStepID = RecipeInstruction.RecInstStepID "
					+ "Where RecipeInstruction.RecipeID = ?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt.setInt(1, recipeID);
			res = pstmt.executeQuery();
			
			//Get the amount of records returned in the ResultSet to create the array.
			res.last(); //Set the ResultSet pointer to the last row.
			instructions = new String[res.getRow()]; //Instantiate the array using the row number of the last row.
			res.beforeFirst(); //Return the ResultSet pointer to the beginning (before it).
				
			while (res.next()) //increment through the ResultSet, setting the String of the array to the Ingredient in the ResultSet
			{
				instructions[res.getRow()-1] = res.getString(1);  //ResultSet row numbering starts at 1, arrays at 0.  Offset Required.
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally //Close all database objects.
		{
			try { if (res != null) res.close(); } catch (Exception e) { e.printStackTrace(); };
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); };
		}
		
		return instructions; 
	}
	
	//Attempt to close the database connection.  This method should always be called when finished with the object.
		public void close()
		{
			try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); };
		}
}
