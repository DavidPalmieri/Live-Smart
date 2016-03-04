package modifydb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class URLGrabber
{
	
	String fileName;
	String category;

	public URLGrabber(String fileName)
	{
		this.fileName = fileName;
		category = getCategory();
	}
	
	public String getCategory()
	{
		String[] cat = fileName.split(".txt");
		category = cat[0];
		return category;
	}
	
	public void populateMasterList() throws IOException 
	{
		ArrayList<String> links = new ArrayList<String>();
		
		InputStream in =  URLGrabber.class.getResourceAsStream("/modifydb/CategoryLinks/" + fileName);		
		BufferedReader br = new BufferedReader( new InputStreamReader(in));
	 
		String line = null;
		while ((line = br.readLine()) != null) 
		{
			if (line.charAt(line.length() - 13) == '-' && line.charAt(line.length() - 18) == '-' && 
					line.charAt(line.length() - 23) == '-' && line.charAt(line.length() - 28) == '-' && 
					line.charAt(line.length() - 37) == '/')
			{
				links.add(line);
			}
		}
		
		br.close();
		
		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("src/modifydb/CategoryLinks/MasterList.txt", true)));
		    out.println(category);
		    for (String link : links)
		    {
		    	 out.println(link);
		    }
		    out.close();
		}
		catch (IOException e) 
		{
		    e.printStackTrace();
		}
		
		
	}
}
