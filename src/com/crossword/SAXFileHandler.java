package com.crossword;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import com.crossword.CrosswordException.ExceptionType;
import com.crossword.activity.CrosswordActivity;

import android.content.Context;

public class SAXFileHandler {
	static public Context context;
	
	public SAXFileHandler() {
	}

	public static void getFeeds(CrosswordParser parser, String name) throws CrosswordException {
		// On passe par une classe factory pour obtenir une instance de sax
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		SAXParser sax = null;
		try {
			sax = fabrique.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			InputStream input = new URL(String.format("http://crossword.lauper.fr/grids/%s", name)).openStream();;
			//InputStream input = new FileInputStream(path);
			sax.parse(input, parser);
		} catch (FileNotFoundException e) {
			throw new CrosswordException(ExceptionType.GRID_NOT_FOUND);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getSave(CrosswordParser parser, String name) throws CrosswordException
	{
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sax = factory.newSAXParser();
			//InputStream input = new URL(String.format("http://crossword.lauper.fr/grids/%s", name)).openStream();;
			InputStream input = new FileInputStream(CrosswordActivity.SAV_DIRECTORY + name);
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
}