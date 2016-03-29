package modifydb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryDB 
{
	private String dbURL = "jdbc:derby:RecipeDB;create=false;";
    private Connection conn = null;

	public QueryDB()
	{
		try
	    {
	        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	        conn = DriverManager.getConnection(dbURL);
	    }
	    catch (Exception sqlExcept)
	    {
	        sqlExcept.printStackTrace();
	    }  
	}
	
	public int getRecipeID(String url) throws SQLException
	{
		Integer recipeID = 99999;
	    PreparedStatement pstmt = conn.prepareStatement("Select RecipeID from Recipe Where Url = ?");
	    pstmt.setString(1, url);
	    ResultSet results = pstmt.executeQuery();
	   
	    while (results.next())
	    {
	    	recipeID = results.getInt(1);
	    }
	    
	    return recipeID;
	}
	
	public int getCategoryID(String category) throws SQLException
	{
		Integer categoryID = 99999;
	    PreparedStatement pstmt = conn.prepareStatement("Select CategoryID from Category Where Category = ?");
	    pstmt.setString(1, category);
	    ResultSet results = pstmt.executeQuery();
	   
	    while (results.next())
	    {
	    	categoryID = results.getInt(1);
	    }
	    
	    return categoryID;
	}
	
	
}
