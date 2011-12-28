/*
 * Copyright 2011 Alexis Lauper <alexis.lauper@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.crossword;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


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
