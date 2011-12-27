package com.crossword.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.crossword.CrosswordException;
import com.crossword.CrosswordParser;
import com.crossword.GridAdapter;
import com.crossword.R;
import com.crossword.SAXFileHandler;
import com.crossword.keyboard.KeyboardView;
import com.crossword.keyboard.KeyboardViewInterface;
import com.crossword.components.Word;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CrosswordActivity extends Activity implements OnTouchListener, KeyboardViewInterface {

	public static final String	GRID_DIRECTORY = "/data/data/com.crossword/grid/";
	public static final int 	GRID_WIDTH = 9;
	public static final int 	GRID_HEIGHT = 10;
	public static final float 	KEYBOARD_OVERLAY_OFFSET = 90;

	private GridView 		grid;
	private KeyboardView 	keyboardView;
	private GridAdapter 	gridAdapter;
	private TextView 		txtDescription;
	private TextView 		keyboardOverlay;

	private String[][]		area;			// Tableau représentant les lettres
	private ArrayList<Word> entries;		// Liste des mots
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // Liste des cases selectionnées

	private int 			downPos;		// Position ou le joueur à appuyé
    private int 			downX;			// Ligne ou le joueur à appuyé
    private int 			downY;			// Colonne ou le joueur à appuyé
	private int 			currentPos;		// Position actuelle du curseur
	private int 			currentX;		// Colonne actuelle du curseur
	private int 			currentY;		// Ligne actuelle du curseur
	private Word			currentWord;	// Mot actuellement selectionné
	private boolean 		horizontal;		// Sens de la selection

	private String 			filename;			// Nom de la grille
	
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.crossword);
	    
	    this.filename = getIntent().getExtras().getString("filename");
	    
        this.grid = (GridView)findViewById(R.id.grid);
        this.grid.setOnTouchListener(this);
        this.grid.setNumColumns(GRID_WIDTH);

        this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
        this.keyboardView.setDelegate(this);

        this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);

        this.txtDescription = (TextView)findViewById(R.id.description);

	    this.area = new String[GRID_HEIGHT][GRID_WIDTH];

	    CrosswordParser parser = new CrosswordParser();
	    try {
			File directory = new File(CrosswordActivity.GRID_DIRECTORY + this.filename);
			if (directory.exists())
				SAXFileHandler.getSave(parser, this.filename);
			else
				SAXFileHandler.getFeeds(parser, this.filename);
		} catch (CrosswordException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	    this.entries = parser.getData();
	    
	    if (this.entries == null) {
	    	finish();
	    	return;
	    }
	    
	    for (Word entry: this.entries) {
	    	String text = entry.getTmp();
	    	boolean horizontal = entry.getHorizontal();
	    	int x = entry.getX();
	    	int y = entry.getY();
	    	
	    	Log.d("Crossword", "entry: " + text);
	    	
	    	for (int i = 0 ; i < entry.getLength(); i++) {
	    		if (horizontal) {
	    			if (y >= 0 && y < GRID_HEIGHT && x+i >= 0 && x+i < GRID_WIDTH)
	    				this.area[y][x+i] = text != null ? String.valueOf(text.charAt(i)) : " ";
	    		}
	    		else {
	    			if (y+i >= 0 && y+i < GRID_HEIGHT && x >= 0 && x < GRID_WIDTH)
	    				this.area[y+i][x] = text != null ? String.valueOf(text.charAt(i)) : " ";
	    		}
	    	}
	    }
	    
        Display display = getWindowManager().getDefaultDisplay();
        this.gridAdapter = new GridAdapter(this, this.area, display.getWidth() / GRID_WIDTH);
        this.grid.setAdapter(this.gridAdapter);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
        int position = this.grid.pointToPosition((int)event.getX(), (int)event.getY());
        int x = position % GRID_WIDTH;
        int y = position / GRID_WIDTH;

        System.out.println("touch on: " + x + " x " + y);
        
        // Si pas de mot sur cette case (= case noire), return
    	if (getWord(x, y, this.horizontal) == null)
    		return true;
		
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	// Stocke les coordonnees d'appuie sur l'ecran
                this.downX = x;
                this.downY = y;
                this.downPos = position;

                // Remet les anciennes case selectionnees dans leur etat normal
            	for (View child: selectedArea)
            		child.setBackgroundResource(R.drawable.area_empty);
            	selectedArea.clear();

            	// Colore la case en jaune
        		View child = this.grid.getChildAt(position);
        		if (child != null)
            		child.setBackgroundResource(R.drawable.area_selected);

        		break;
            }

            case MotionEvent.ACTION_UP:
            {
            	// Si clique sur la case, inversion horizontale <> verticale
                // Si clique sur une autre case (= mouvement) calcul en fonction de la gesture
            	if (this.downPos == position && this.currentPos == position)
            	{
            		this.horizontal = !this.horizontal;
            	}
            	else if (this.downPos != position) 
            	{
            		this.horizontal = (Math.abs(this.downX - x) > Math.abs(this.downY - y));
            	}

            	// Test si un mot se trouve sur cette case
                this.currentWord = getWord(this.downX, this.downY, this.horizontal);
        	    if (this.currentWord == null)
        	    	break;
        	    
        	    // Force la direction a etre dans le meme sens que le mot
        	    this.horizontal = this.currentWord.getHorizontal();
                
            	// Si clique sur la case, place le curseur sur le mot
                // Sinon place le curseur au debut du mot
            	if (this.downPos == position)
            	{
            	    this.currentX = this.downX;
                    this.currentY = this.downY;
                	this.currentPos = position;
            	}
            	else
            	{
            	    this.currentX = this.currentWord.getX();
                    this.currentY = this.currentWord.getY();
                	this.currentPos = this.currentY * GRID_WIDTH + this.currentX;
            	}

            	this.txtDescription.setText(this.currentWord.getDescription());

        	    // Set background color
        	    boolean horizontal = this.currentWord.getHorizontal();
        	    for (int l = 0; l < this.currentWord.getLength(); l++) {
        	    	int index = this.currentWord.getY() * GRID_WIDTH + this.currentWord.getX() + (l * (horizontal ? 1 : GRID_WIDTH));
        	    	View child = this.grid.getChildAt(index);
            		child.setBackgroundResource(index == this.currentPos ? R.drawable.area_current : R.drawable.area_selected);
        	    	selectedArea.add(child);
        	    }
        	    this.gridAdapter.notifyDataSetChanged();

        	    break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
    private Word getWord(int x, int y, boolean horizontal)
    {
        Word horizontalWord = null;
        Word verticalWord = null;
	    for (Word entry: this.entries) {
	    	if (x >= entry.getX() && x <= entry.getXMax())
	    		if (y >= entry.getY() && y <= entry.getYMax()) {
        	    	if (entry.getHorizontal())
        	    		horizontalWord = entry;
        	    	else
        	    		verticalWord = entry;
	    		}
	    }
	    
	    if (horizontal)
	    	return (horizontalWord != null) ? horizontalWord : verticalWord;
	    else
	    	return (verticalWord != null) ? verticalWord : horizontalWord;
	}

	@Override
	public void onKeyDown(String value, int location[], int width) {
		System.out.println("onKeyDown: " + value + ", insert in: " + currentX + "x" + currentY);

		// Deplace l'overlay du clavier
		if (value.equals(" ") == false) {
			int offsetX = (this.keyboardOverlay.getWidth() - width) / 2;
			int offsetY = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, KEYBOARD_OVERLAY_OFFSET, getResources().getDisplayMetrics());
			FrameLayout.LayoutParams lp = (LayoutParams)this.keyboardOverlay.getLayoutParams();
			lp.leftMargin = location[0] - offsetX;
			lp.topMargin = location[1] - offsetY;
			this.keyboardOverlay.setLayoutParams(lp);
			this.keyboardOverlay.setText(value);
			this.keyboardOverlay.clearAnimation();
			this.keyboardOverlay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onKeyUp(String value) {
		System.out.println("onKeyUp: " + value + ", insert in: " + currentX + "x" + currentY);

		// Efface l'overlay du clavier
		if (value.equals(" ") == false) {
			this.keyboardOverlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.keyboard_overlay_fade_out));
			this.keyboardOverlay.setVisibility(View.INVISIBLE);
		}

		// Si aucun mot selectionne, retour
		if (this.currentWord == null)
			return;

		// Case actuelle
		int x = this.currentX;
		int y = this.currentY;

		// Si la case est pas null (= noire), retour
		if (this.area[y][x] == null)
			return;
		
		// Ecrit la lettre sur le "curseur"
		if (this.area[y][x] != null) {
			this.area[y][x] = value;
			this.gridAdapter.notifyDataSetChanged();
		}
		
		// Deplace sur le "curseur" sur la case precendante (effacer), ou suivante (lettres)
		if (value.equals(" ")) {
			x = (this.horizontal ? x - 1 : x);
			y = (this.horizontal ? y: y - 1);
		}
		else
		{
			x = (this.horizontal ? x + 1 : x);
			y = (this.horizontal ? y: y + 1);
		}
		
		// Si la case suivante est disponible, met la case en jaune, remet l'ancienne en bleu, et set la nouvelle position
		if (x >= 0 && x < GRID_WIDTH
				&& y >= 0 && y < GRID_HEIGHT
				&& this.area[y][x] != null) {
			this.grid.getChildAt(y * GRID_WIDTH + x).setBackgroundResource(R.drawable.area_current);
			this.grid.getChildAt(this.currentY * GRID_WIDTH + this.currentX).setBackgroundResource(R.drawable.area_selected);
			this.currentX = x;
			this.currentY = y;
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		// Sauvegarde vers le XML
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<grid>\n");
		sb.append("<horizontal>\n");
	    for (Word entry: this.entries) {
	    	if (entry.getHorizontal()) {
	    	int x = entry.getX();
	    	int y = entry.getY();
	    	StringBuffer word = new StringBuffer();
	    	for (int i = 0; i < entry.getLength(); i++)
	    		word.append(this.area[y][x+i]);
    	    sb.append("<word x=\""+(x+1)+"\" y=\""+(y+1)+"\" description=\""+entry.getDescription()+"\" tmp=\""+word+"\">"+entry.getText()+"</word>\n");
	    	}
	    }
		sb.append("</horizontal>\n");
		sb.append("<vertical>\n");
	    for (Word entry: this.entries) {
	    	if (entry.getHorizontal() == false) {
	    	int x = entry.getX();
	    	int y = entry.getY();
	    	StringBuffer word = new StringBuffer();
	    	for (int i = 0; i < entry.getLength(); i++)
	    		word.append(this.area[y+i][x]);
    	    sb.append("<word x=\""+(x+1)+"\" y=\""+(y+1)+"\" description=\""+entry.getDescription()+"\" tmp=\""+word+"\">"+entry.getText()+"</word>\n");
	    	}
	    }
		sb.append("</vertical>\n");
		sb.append("</grid>\n");
		
		// Make directory if not exists
		File directory = new File(GRID_DIRECTORY);
		if (directory.exists() == false)
			directory.mkdir();
		
		// Write XML
		FileWriter file;
		try {
			file = new FileWriter(GRID_DIRECTORY + this.filename);
			file.write(sb.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println(sb);
	}

}
