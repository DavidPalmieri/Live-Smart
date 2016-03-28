package modifydb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlParser 
{
	Elements elements;
	Document html;
	
	public HtmlParser(String url)
	{
		try 
		{
            //Connect to website using Jsoup 'browser' and get html as Jsoup Document
            html = Jsoup.connect(url).timeout(30000).get();
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        }
		
		//Get all of the elements of the html used for quickly searching the Document
		elements =html.getAllElements();
	}
	
	public String getTitle()
	{
		String head = html.head().text();
		String[] trimmed = head.split(" recipe from Betty Crocker");
		String title = trimmed[0].replaceAll("’", "").replaceAll("[^a-zA-Z&&[^- ]]", " ");
		title = title.replaceAll(" +", " ").trim();
		return title;
	}
	
	public String getSummary()
	{
		for (int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).className().equalsIgnoreCase("recipePartDescriptionText"))
			{
				return elements.get(i).text();
			}
		}
		
		return null;
	}
	
	public String getPrepTime()
	{
		return convertTime(parseContent(tagSearch("meta", "prepTime")));
	}
	
	public String getTotalTime()
	{
		return convertTime(parseContent(tagSearch("meta", "totalTime")));
	}
	
	public String getServings()
	{
		return parseContent(tagSearch("meta", "recipeYield"));
	}
	
	//Search through the html for a specific tag type and tag contents
	private String tagSearch(String tag, String contents)
	{
		Elements matchElements = new Elements();
		
		for (int i = 0; i < elements.size(); i++)
		{
			if (elements.get(i).tag().toString().equalsIgnoreCase(tag))
			{
				matchElements.add(elements.get(i));
			}
		}
		
		for (int i = 0; i < matchElements.size(); i++)
		{
			if (matchElements.get(i).toString().contains(contents))
			{
				return matchElements.get(i).toString();
			}
		}
				
		return null;	
	}
			
	//Parse the contents text out of a tag by splitting the input String into 3 pieces
	private String parseContent(String input)
	{
		String[] piece = input.split("content=\"");
		piece = piece[1].split("\"");
		return piece[0];
	}
			
	//Convert the input String from form: PT#H##M to form: ## Hours and ## Minutes
	private String convertTime(String input)
	{
		String output = "";		
		String[] piece = input.split("H");
		int hours = Integer.parseInt(piece[0].substring(2));
				
		if (hours != 0)
		{
			output += hours;
			
			if (hours > 1)
			{
				output += " Hours ";
			}
			else
			{
				output += " Hour ";
			}
		}
				
		piece = piece[1].split("M");
		int min = Integer.parseInt(piece[0]);
		
		if (min != 0)
		{
			if(hours != 0 )
			{
				output += "and ";
			}
			output +=  + min;
				
			if (min > 1)
			{
				output += " Minutes";
			}
			else
			{
				output += " Minute";
			}
		}
				
		return output;
	}	
	
	public String[] getNutrition()
	{
		String[] nutrition = new String[16];
		
		//serving size
		for (int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).className().equalsIgnoreCase("nutrition-serving-size"))
			{
				String[] text = elements.get(i).text().split("Serving Size: ");
				nutrition[0] = text[1];
			}
		}
		
		//calories, calFat, totFat, satFat, transFat, cholesterol,
		//sodium, carbs, fiber, sugar, protein, vitA, vitC, calcium, iron
		nutrition[1] = nutritionSearch("Calories");
		nutrition[2] = nutritionSearch("Calories from Fat");
		nutrition[3] = nutritionSearch("Total Fat");
		nutrition[4] = nutritionSearch("Saturated Fat");
		nutrition[5] = nutritionSearch("Trans Fat");
		nutrition[6] = nutritionSearch("Cholesterol");
		nutrition[7] = nutritionSearch("Sodium");
		nutrition[8] = nutritionSearch("Total Carbohydrate");
		nutrition[9] = nutritionSearch("Dietary Fiber");
		nutrition[10] = nutritionSearch("Sugars");
		nutrition[11] = nutritionSearch("Protein");
		nutrition[12] = nutritionSearch("Vitamin A");
		nutrition[13] = nutritionSearch("Vitamin C");
		nutrition[14] = nutritionSearch("Calcium");
		nutrition[15] = nutritionSearch("Iron");
				
		return nutrition;
	}
	
	private String nutritionSearch(String text)
	{
		for (int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).className().equalsIgnoreCase("nutrition-fact-title"))
			{
				if (elements.get(i).text().equalsIgnoreCase(text))
				{
					return elements.get(i + 1).text();
				}
			}
		}
		return null;
	}
	
	public boolean getPicture(String recipeTitle) throws IOException
	{
		String imagePath = "src/modifydb/RecipePictures/" + recipeTitle + ".jpg";
		String imageURL = parseContent(tagSearch("meta", "image"));
		
		if(!imageURL.contains("http://www.bettycrocker.com/Images/Icons/BCFacebookThumbnail.png") && !imageURL.contains("recipe-defaultbeautyshot.ashx") )
		{
			URL url = new URL(imageURL);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(new File(imagePath));

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) 
			{
				os.write(b, 0, length);
			}

			is.close();
			os.close();
			
			return true;
		}
		
		return false;
	}
	
	//Parse the html and get all directions
	public ArrayList<String> getDirections()
	{
		ArrayList<String> directions = new ArrayList<String>();
			
		for (int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).className().equalsIgnoreCase("recipePartStepDescription"))
			{
				directions.add(elements.get(i).text());
			}
		}
			
		return directions;
	}
		
	//Parse the html and get all ingredients
	public ArrayList<String> getIngredients()
	{
		ArrayList<String> ingredients = new ArrayList<String>();
				
		for (int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).className().equalsIgnoreCase("recipePartIngredient"))
			{
				ingredients.add(elements.get(i).text());
			}
		}
				
		return ingredients;
	}
		
	//Parse the html and get all "Expert tips"
	public ArrayList<String> getTips()
	{
		ArrayList<String> tips = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).className().equalsIgnoreCase("recipePartTipsInfo"))
			{
				String html = elements.get(i).html();
					
				boolean moreTips = true;
				int counter = 1;
				while (moreTips)
				{
					if (html.contains("gmi_rp_expertTips_tip_" + counter))
					{
						String[] text = html.split("gmi_rp_expertTips_tip_" + counter + "\">");
						text = text[1].split("</p");
						String tip = text[0].replaceAll("<i>", "");
						tip = tip.replaceAll("</i>", "");
						tip = tip.replaceAll("<b>", "");
						tip = tip.replaceAll("</b>", "");
						tip = tip.replaceAll("<u>", "");
						tip = tip.replaceAll("</u>", "");
						tips.add(tip);
					}
					else
					{
						moreTips = false;
					}	
					counter++;
				}				
			}
		}
		return tips;
	}
}
