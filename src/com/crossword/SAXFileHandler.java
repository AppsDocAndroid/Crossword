package com.crossword;

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

import android.content.Context;

public class SAXFileHandler {
	static public Context context;
	
	public SAXFileHandler() {
	}

	public static void getFeeds(CrosswordParser parser) throws CrosswordException {
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

		URL url = null;
		try {
			url = new URL("http://crossword.lauper.fr/grids/slam_28_11_11.xml");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}


		try {
			InputStream input = url.openStream();
			//InputStream input = new FileInputStream("/sdcard/text.xml");
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
}