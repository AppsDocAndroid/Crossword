package com.crossword;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.content.Context;
import android.util.Log;

public class SAXFileHandler {
	static public Context context;
	
	public SAXFileHandler() {
	}

	public static void getFeeds(CrosswordParser parser) {
		// On passe par une classe factory pour obtenir une instance de sax
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		SAXParser sax = null;
		ArrayList entries = null;
		try {
			// On "fabrique" une instance de SAXParser
			sax = fabrique.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		// On défini l'url du fichier XML
//		URL url = null;
//		try {
//			url = new URL("http://thibault-koprowski.fr/feed");
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//		}
		/*
		 * Le handler sera gestionnaire du fichier XML c'est à dire que c'est lui qui sera chargé
		 * des opérations de parsing. On vera cette classe en détails ci après.
		*/
		try {
			// On parse le fichier XML
			//InputStream input = url.openStream();
			InputStream input = new FileInputStream("/sdcard/text.xml");
			if(input == null)
				Log.e("erreur android","null");
			else{
				sax.parse(input, parser);
				// On récupère directement la liste des feeds
				entries = ((CrosswordParser)parser).getData();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}