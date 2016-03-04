package modifydb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class URLGrabber
{
	
	String fileName;

	public URLGrabber(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getCategory()
	{
		String[] category = fileName.split(".txt");
		return category[0];
	}
	
	public ArrayList<String> getLinks() throws IOException 
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
		
		return links;
	}

}
