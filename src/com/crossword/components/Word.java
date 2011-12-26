package com.crossword.components;

public class Word {
	private int		x;
	private int		y;
	private int		length;
	private String	text;
	private String	description;
	private boolean	horizontal = true;
	
	public void		setText(String value) { this.text = value; this.length = value.length(); }
	public String	getText() { return this.text; }
	
	public void		setDescription(String value) { this.description= value; }
	public String	getDescription() { return this.description; }
	
	public boolean	getHorizontal() { return this.horizontal; }
	public void		setHorizontal(boolean value) { this.horizontal = value; }

	public void		setX(int value) { this.x = value; }
	public int		getX() { return this.x; }
	public int 		getXMax() { return this.horizontal ? this.x + this.length - 1: this.x; }
	
	public void		setY(int value) { this.y = value; }
	public int		getY() { return this.y; }
	public int 		getYMax() { return this.horizontal ? this.y : this.y + this.length - 1; }

	public int 		getLength() { return this.length; }

}
