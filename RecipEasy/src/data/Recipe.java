package data;

public class Recipe 
{
	private String title;
	private String address;
	private String category;
	private String picAddress;
	private int prepTime;
	private int totalTime;
	private int servings;
	private String summary;
	private String author;
	private String servingSize;
	private int calories;
	private int calFat;
	private int totFat;
	private int satFat;
	private int transFat;
	private int cholesterol;
	private int sodium;
	private int carbs;
	private int fiber;
	private int sugar;
	private int protein;
	
	public Recipe(String a, String n)
	{
		address = a;
		title = n;
	}
}
