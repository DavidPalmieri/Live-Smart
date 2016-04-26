 ////////////////////////////////////////////////////////////////////////////////////////////////////////+
//																										//
//	Category: Category information class																//	
//	Author: Chris Costa																					//
//																										//
//	<== Constructors ==>																				//
//																										//	
//		Category(String name, int ID)																	//
//			Creates a new category object, setting the name and id to the values of the parameters		//
//																										//
//	<== Accessors ==>																					//
//																										//
//		getName(): String																				//
//			returns the value of the name variable of the category										//
//																										//
//		getID(): int																					//
//			returns the value of the id variable of the category										//
//																										//
/////////////////////////////////////////////////////////////////////////////////////////////////////////

package data;

public class Category
{
	//Naem of the category
	String name;
	
	//ID number of the category
	int id;
	
	//Create a new category object, setting the name and ID
	public Category(String name, int id)
	{
		this.name = name;
		this.id = id;
	}
	
	//Get the name of the category
	public String getName()
	{
		return name;
	}
	
	//Get the ID of the category
	public int getID()
	{
		return id;
	}
}
