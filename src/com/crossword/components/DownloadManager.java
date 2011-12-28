package com.crossword.components;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.crossword.Crossword;

public class DownloadManager {
	
	public static void downloadGrid(String filename) {
		InputStream input = null;
	    FileOutputStream writeFile = null;
	
	    // Telechargement du fichier
	    try
	    {
	        URL url = new URL(String.format("http://crossword.lauper.fr/grids/%s", filename));
	        URLConnection connection = url.openConnection();
	        int fileLength = connection.getContentLength();
	
	        if (fileLength == -1)
	        {
	            System.out.println("Invalide URL or file.");
	            return;
	        }
	
	        input = connection.getInputStream();
	        writeFile = new FileOutputStream(Crossword.GRID_DIRECTORY + filename);
	        byte[] buffer = new byte[1024];
	        int read;
	
	        while ((read = input.read(buffer)) > 0)
	            writeFile.write(buffer, 0, read);
	        writeFile.flush();
	    }
	    catch (IOException e)
	    {
	        System.out.println("Error while trying to download the file.");
	        e.printStackTrace();
	    }
	    finally
	    {
	        try
	        {
	        	if (writeFile != null)
	        		writeFile.close();
	        	if (input != null)
	        		input.close();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
}
