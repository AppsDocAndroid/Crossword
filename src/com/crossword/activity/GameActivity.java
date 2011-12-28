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

package com.crossword.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import com.crossword.Crossword;
import com.crossword.CrosswordException;
import com.crossword.R;
import com.crossword.SAXFileHandler;
import com.crossword.keyboard.KeyboardView;
import com.crossword.keyboard.KeyboardViewInterface;
import com.crossword.parser.GridFullParser;
import com.crossword.parser.GridParser;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Word;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnTouchListener, KeyboardViewInterface {

	public enum GRID_MODE {NORMAL, CHECK, CORRECTION};
	public static GRID_MODE currentMode = GRID_MODE.NORMAL;
	
	public static final int 	GRID_WIDTH = 9;
	public static final int 	GRID_HEIGHT = 10;
	public static final float 	KEYBOARD_OVERLAY_OFFSET = 90;

	private GridView 		gridView;
	private KeyboardView 	keyboardView;
	private GameGridAdapter 	gridAdapter;
	private TextView 		txtDescription;
	private TextView 		keyboardOverlay;

	private Grid			grid;
	private String[][]		area;			// Tableau représentant les lettres du joueur
	private String[][] 		correctionArea; // Tableau représentant les lettres correctes
	private ArrayList<Word> entries;		// Liste des mots
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // Liste des cases selectionnées

	private boolean			downIsPlayable;	// false si le joueur à appuyé sur une case noire 
	private int 			downPos;		// Position ou le joueur à appuyé
    private int 			downX;			// Ligne ou le joueur à appuyé
    private int 			downY;			// Colonne ou le joueur à appuyé
	private int 			currentPos;		// Position actuelle du curseur
	private int 			currentX;		// Colonne actuelle du curseur
	private int 			currentY;		// Ligne actuelle du curseur
	private Word			currentWord;	// Mot actuellement selectionné
	private boolean 		horizontal;		// Sens de la selection

	private String 			filename;		// Nom de la grille

	private boolean 		solidSelection;	// PREFERENCES: Selection persistante
	private boolean			gridIsLower;	// PREFERENCES: Grille en minuscule


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crossword, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.check:
        	if (GameActivity.currentMode == GRID_MODE.CHECK)
        		GameActivity.currentMode = GRID_MODE.NORMAL;
        	else
        		GameActivity.currentMode = GRID_MODE.CHECK;
        	this.gridAdapter.notifyDataSetChanged();
        	return true;
        case R.id.correction:
        	if (GameActivity.currentMode == GRID_MODE.CORRECTION)
        	{
        		GameActivity.currentMode = GRID_MODE.NORMAL;
        		this.gridAdapter.notifyDataSetChanged();
        	}
        	else
        	{
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setMessage(R.string.dialog_display_correction)
	        	       .setCancelable(false)
	        	       .setPositiveButton(R.string.display, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   GameActivity.currentMode = GRID_MODE.CORRECTION;
	        	        	   GameActivity.this.gridAdapter.notifyDataSetChanged();
	        	           }
	        	       })
	        	       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	           }
	        	       });
	        	builder.create().show();
        	}
        	return true;
        case R.id.menu_main_preferences:
        	startActivityForResult(new Intent(this, PeferencesActivity.class), Crossword.REQUEST_PREFERENCES);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch (requestCode) {
    	case Crossword.REQUEST_PREFERENCES:
    		Toast.makeText(this, "PREFERENCES_UPDATED", Toast.LENGTH_SHORT).show();
    		readPreferences();
    		this.gridAdapter.setLower(this.gridIsLower);
    		this.gridAdapter.notifyDataSetChanged();
        	break;
    	}
	}

	private void readPreferences() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.solidSelection = preferences.getBoolean("solid_selection", false);
		this.gridIsLower = preferences.getBoolean("grid_is_lower", false);
	}

	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.game);
	    
		readPreferences();
	    
	    this.filename = getIntent().getExtras().getString("filename");
	    
        this.gridView = (GridView)findViewById(R.id.grid);
        this.gridView.setOnTouchListener(this);
        this.gridView.setNumColumns(GRID_WIDTH);

        this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
        this.keyboardView.setDelegate(this);

        this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);

        this.txtDescription = (TextView)findViewById(R.id.description);

	    this.area = new String[GRID_HEIGHT][GRID_WIDTH];
	    this.correctionArea = new String[GRID_HEIGHT][GRID_WIDTH];

	    GridFullParser crosswordParser = new GridFullParser();
	    try {
			File file = new File(Crossword.GRID_DIRECTORY + this.filename);
			if (file.exists())
			{
				SAXFileHandler.read((DefaultHandler)crosswordParser, Crossword.GRID_DIRECTORY + this.filename);

				GridParser gridParser = new GridParser();
				SAXFileHandler.read((DefaultHandler)gridParser, Crossword.GRID_DIRECTORY + this.filename);
				this.grid = gridParser.getData();
			}
			else
			{
		    	finish();
		    	return;
			}
		} catch (CrosswordException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	    this.entries = crosswordParser.getData();
	    if (this.entries == null) {
	    	finish();
	    	return;
	    }
	    
	    for (Word entry: this.entries) {
	    	String tmp = entry.getTmp();
	    	String text = entry.getText();
	    	boolean horizontal = entry.getHorizontal();
	    	int x = entry.getX();
	    	int y = entry.getY();
	    	
	    	for (int i = 0 ; i < entry.getLength(); i++) {
	    		if (horizontal) {
	    			if (y >= 0 && y < GRID_HEIGHT && x+i >= 0 && x+i < GRID_WIDTH)
	    				this.area[y][x+i] = tmp != null ? String.valueOf(tmp.charAt(i)) : " ";
	    				this.correctionArea[y][x+i] = String.valueOf(text.charAt(i));
	    		}
	    		else {
	    			if (y+i >= 0 && y+i < GRID_HEIGHT && x >= 0 && x < GRID_WIDTH)
	    				this.area[y+i][x] = tmp != null ? String.valueOf(tmp.charAt(i)) : " ";
	    				this.correctionArea[y+i][x] = String.valueOf(text.charAt(i));
	    		}
	    	}
	    }
	    
        Display display = getWindowManager().getDefaultDisplay();
        this.gridAdapter = new GameGridAdapter(this, this.area, this.correctionArea, display.getWidth() / GRID_WIDTH);
        this.gridView.setAdapter(this.gridAdapter);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
            	View child = this.gridView.getChildAt(position);

            	// Si pas de mot sur cette case (= case noire), aucun traitement
            	if (child == null || child.getTag().equals(GameGridAdapter.AREA_BLOCK)) {
            		if (this.solidSelection == false) {
                        clearSelection();
                    	this.gridAdapter.notifyDataSetChanged();
            		}
            			
            		this.downIsPlayable = false;
            		return true;
            	}
        		this.downIsPlayable = true;

            	// Stocke les coordonnees d'appuie sur l'ecran
            	this.downPos = position;
                this.downX = this.downPos % GRID_WIDTH;
                this.downY = this.downPos / GRID_WIDTH;
                System.out.println("ACTION_DOWN, x:" + this.downX + ", y:" + this.downY + ", position: " + this.downPos);

                clearSelection();
                
            	// Colore la case en bleu
            	child.setBackgroundResource(R.drawable.area_selected);
            	selectedArea.add(child);

            	this.gridAdapter.notifyDataSetChanged();
        		break;
            }

            case MotionEvent.ACTION_UP:
            {
            	// Si le joueur à appuyé sur une case noire, aucun traitement 
            	if (this.downIsPlayable == false)
            		return true;
            	
                int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
                int x = position % GRID_WIDTH;
                int y = position / GRID_WIDTH;
                System.out.println("ACTION_DOWN, x:" + x + ", y:" + y + ", position: " + position);

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
        	    	View currentChild = this.gridView.getChildAt(index);
        	    	if (currentChild != null) {
        	    		currentChild.setBackgroundResource(index == this.currentPos ? R.drawable.area_current : R.drawable.area_selected);
        	    		selectedArea.add(currentChild);
        	    	}
        	    }

        	    this.gridAdapter.notifyDataSetChanged();
        	    break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
	// Remet les anciennes case selectionnees dans leur etat normal
    private void clearSelection() {
    	for (View selected: selectedArea)
    		selected.setBackgroundResource(R.drawable.area_empty);
    	selectedArea.clear();
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
			this.gridView.getChildAt(y * GRID_WIDTH + x).setBackgroundResource(R.drawable.area_current);
			this.gridView.getChildAt(this.currentY * GRID_WIDTH + this.currentX).setBackgroundResource(R.drawable.area_selected);
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
		sb.append("<name>"+this.grid.getName()+"</name>\n");
		sb.append("<description>"+this.grid.getDescription()+"</description>\n");
		sb.append("<date>"+this.grid.getDate()+"</date>\n");
		sb.append("<author>"+this.grid.getAuthor()+"</author>\n");
		sb.append("<level>"+this.grid.getLevel()+"</level>\n");
		sb.append("<percent>"+this.grid.getPercent()+"</percent>\n");
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
		File directory = new File(Crossword.GRID_DIRECTORY);
		if (directory.exists() == false)
			directory.mkdir();
		
		// Write XML
		FileWriter file;
		try {
			file = new FileWriter(Crossword.GRID_DIRECTORY + this.filename);
			file.write(sb.toString());
			file.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println(sb);
	}

}
