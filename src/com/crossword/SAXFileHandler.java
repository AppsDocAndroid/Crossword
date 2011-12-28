package com.crossword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.crossword.CrosswordException.ExceptionType;
import android.content.Context;

public class SAXFileHandler {
	static public Context context;
	
	public SAXFileHandler() {
	}

	public static void getFeeds(CrosswordParser parser, String filename) throws CrosswordException {
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
                writeFile.close();
                input.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
		
		// On passe par une classe factory pour obtenir une instance de sax
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		SAXParser sax = null;
		try {
			sax = fabrique.newSAXParser();
			sax.parse(input, parser);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void read(DefaultHandler parser, String path) throws CrosswordException
	{
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			InputStream input = new FileInputStream(path);
			sax.parse(input, parser);
		} catch (FileNotFoundException e) {
			throw new CrosswordException(ExceptionType.SAV_NOT_FOUND);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getSave(CrosswordParser parser, String filename) throws CrosswordException
	{
		read(parser, Crossword.GRID_DIRECTORY + filename);
	}

}