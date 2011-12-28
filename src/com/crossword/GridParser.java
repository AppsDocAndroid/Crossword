package com.crossword;

import java.sql.Date;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.crossword.components.Grid;

public class GridParser extends DefaultHandler {
	
	private Grid			grid = new Grid();
	private StringBuffer	buffer;

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public GridParser() {
		super();
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		buffer = new StringBuffer();
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equalsIgnoreCase("name")) {
			this.grid.setName(buffer.toString());
		}

		if (localName.equalsIgnoreCase("description")) {
			this.grid.setDescription(buffer.toString());
		}

		if (localName.equalsIgnoreCase("level")) {
			this.grid.setLevel(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("percent")) {
			this.grid.setPercent(Integer.parseInt(buffer.toString()));
		}

		if (localName.equalsIgnoreCase("date")) {
			try {
				this.grid.setDate(Date.valueOf(buffer.toString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		if (localName.equalsIgnoreCase("author")) {
			this.grid.setAuthor(buffer.toString());
		}
	}

	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public Grid getData() {
		return this.grid;
	}
	
	public void setFileName(String name) {
		this.grid.setFileName(name);
	}
	
}
