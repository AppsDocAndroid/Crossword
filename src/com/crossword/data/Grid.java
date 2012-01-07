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

package com.crossword.data;

import java.util.Date;

/**
 * Data class for grid meta information
 * 
 * @author alex
 */
public class Grid  implements Comparable<Grid> {
	private String	fileName;
	private String	name;
	private String	description;
	private int		percent;
	private int		level;
	private Date	date;
	private String	author;
	private int 	width;
	private int 	height;
	
	// This is a little hack, if isSeparator is true, element will be displayed
	// with "date separator" layout (a week ago, a month ago, etc).
	private boolean	isSeparator;

	public boolean isSeparator() {
		return this.isSeparator;
	}
	
	public void isSeparator(boolean value) {
		this.isSeparator = value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getPercent() {
		return percent;
	}
	
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setWidth(int value) {
		this.width = value;
	}

	public void setHeight(int value) {
		this.height = value;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	@Override
	public int compareTo(Grid arg) {
		if (arg.getDate() == null)
			return -1;
		if (this.date == null)
			return 1;
			
		return this.date.before(arg.getDate()) ? 1 : -1;
	}

}
