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

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.crossword.components.Word;
public class CrosswordParser extends DefaultHandler {
	
	private ArrayList<Word>	entries;
	private boolean 		inItem;
	private Word 			currentFeed;
	private StringBuffer 	buffer;

	private String 			name;
	private String 			description;
	private String 			difficulty;
	private String 			date;
	private String 			author;
	private boolean			inHorizontal;

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public CrosswordParser() {
		super();
	}
	// * Cette méthode est appelée par le parser une et une seule
	// * fois au démarrage de l'analyse de votre flux xml.
	// * Elle est appelée avant toutes les autres méthodes de l'interface,
	// * à l'exception unique, évidemment, de la méthode setDocumentLocator.
	// * Cet événement devrait vous permettre d'initialiser tout ce qui doit
	// * l'être avant ledébut du parcours du document.
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		entries = new ArrayList();
	}
	/*
	 * Fonction étant déclenchée lorsque le parser trouve un tag XML
	 * C'est cette méthode que nous allons utiliser pour instancier un nouveau feed
 	*/
	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		// Nous réinitialisons le buffer a chaque fois qu'il rencontre un item
		buffer = new StringBuffer();
		// Ci dessous, localName contient le nom du tag rencontré
		// Nous avons rencontré un tag ITEM, il faut donc instancier un nouveau feed
//		if (localName.equalsIgnoreCase(ITEM)){
//			this.currentFeed = new Word();
//			inItem = true;
//		}
		if (localName.equalsIgnoreCase("horizontal")) {
			this.inHorizontal = true;
		}

		if (localName.equalsIgnoreCase("vertical")) {
			this.inHorizontal = false;
		}
		
		if (localName.equalsIgnoreCase("word")) {
			this.currentFeed = new Word();
			this.currentFeed.setX(Integer.parseInt(attributes.getValue("x"))-1);
			this.currentFeed.setY(Integer.parseInt(attributes.getValue("y"))-1);
			this.currentFeed.setTmp(attributes.getValue("tmp"));
			this.currentFeed.setDescription(attributes.getValue("description"));
			this.currentFeed.setHorizontal(this.inHorizontal);
		}
	}

	// * Fonction étant déclenchée lorsque le parser à parsé
	// * l'intérieur de la balise XML La méthode characters
	// * a donc fait son ouvrage et tous les caractère inclus
	// * dans la balise en cours sont copiés dans le buffer
	// * On peut donc tranquillement les récupérer pour compléter
	// * notre objet currentFeed
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equalsIgnoreCase("word")) {
			this.currentFeed.setText(buffer.toString());
			entries.add(this.currentFeed);
			System.out.println("Word, text: " + this.currentFeed.getText() + ", tmp: " + this.currentFeed.getTmp() + ", x: " + this.currentFeed.getX() + ", y: " + this.currentFeed.getY());
			buffer = null;
		}
		if(localName.equalsIgnoreCase("name")) {
			this.name = buffer.toString();
		}
		if(localName.equalsIgnoreCase("description")) {
			this.description = buffer.toString();
		}
		if (localName.equalsIgnoreCase("date")) {
			this.date = buffer.toString();
		}
		if (localName.equalsIgnoreCase("author")) {
			this.author = buffer.toString();
		}
	}

	// * Tout ce qui est dans l'arborescence mais n'est pas partie
	// * intégrante d'un tag, déclenche la levée de cet événement.
	// * En général, cet événement est donc levé tout simplement
	// * par la présence de texte entre la balise d'ouverture et
	// * la balise de fermeture
	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public ArrayList<Word> getData() { return entries; }
	public String getName() { return this.name; }
	public String getDescription () { return this.description; }
	public String getAuthor () { return this.author; }
	public String getDate () { return this.date; }
	public String getDifficulty () { return this.difficulty; }
}
