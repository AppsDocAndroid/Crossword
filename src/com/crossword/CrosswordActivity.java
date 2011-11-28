package com.crossword;

import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CrosswordActivity extends Activity implements OnClickListener  {

	final static int TEXT_INPUT_REQUEST = 1;
	
	final static int GRID_WIDTH = 8;
	final static int GRID_HEIGHT = 10;
	final static private int[][] GRID_ID = {
		{R.id.area_1_1, R.id.area_1_2, R.id.area_1_3, R.id.area_1_4, R.id.area_1_5, R.id.area_1_6, R.id.area_1_7, R.id.area_1_8},
		{R.id.area_2_1, R.id.area_2_2, R.id.area_2_3, R.id.area_2_4, R.id.area_2_5, R.id.area_2_6, R.id.area_2_7, R.id.area_2_8},
		{R.id.area_3_1, R.id.area_3_2, R.id.area_3_3, R.id.area_3_4, R.id.area_3_5, R.id.area_3_6, R.id.area_3_7, R.id.area_3_8},
		{R.id.area_4_1, R.id.area_4_2, R.id.area_4_3, R.id.area_4_4, R.id.area_4_5, R.id.area_4_6, R.id.area_4_7, R.id.area_4_8},
		{R.id.area_5_1, R.id.area_5_2, R.id.area_5_3, R.id.area_5_4, R.id.area_5_5, R.id.area_5_6, R.id.area_5_7, R.id.area_5_8},
		{R.id.area_6_1, R.id.area_6_2, R.id.area_6_3, R.id.area_6_4, R.id.area_6_5, R.id.area_6_6, R.id.area_6_7, R.id.area_6_8},
		{R.id.area_7_1, R.id.area_7_2, R.id.area_7_3, R.id.area_7_4, R.id.area_7_5, R.id.area_7_6, R.id.area_7_7, R.id.area_7_8},
		{R.id.area_8_1, R.id.area_8_2, R.id.area_8_3, R.id.area_8_4, R.id.area_8_5, R.id.area_8_6, R.id.area_8_7, R.id.area_8_8},
		{R.id.area_9_1, R.id.area_9_2, R.id.area_9_3, R.id.area_9_4, R.id.area_9_5, R.id.area_9_6, R.id.area_9_7, R.id.area_9_8},
		{R.id.area_10_1, R.id.area_10_2, R.id.area_10_3, R.id.area_10_4, R.id.area_10_5, R.id.area_10_6, R.id.area_10_7, R.id.area_10_8}
	};
	
	private String[][] area;
	private ArrayList<Word> entries;
	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.crossword);
	    
	    this.area = new String[10][8];

	    CrosswordParser parser = new CrosswordParser();
	    SAXFileHandler.getFeeds(parser);

	    this.entries = parser.getData();
	    for (Word entry: this.entries) {
	    	String text = entry.getText();
	    	boolean horizontal = entry.getHorizontal();
	    	int x = entry.getX() - 1;
	    	int y = entry.getY() - 1;
	    	
	    	Log.d("Crossword", "entry: " + text);
	    	
	    	for (int i = 0 ; i < text.length() ;i++) {
	    		if (horizontal) {
	    			if (y < GRID_HEIGHT && x+i < GRID_WIDTH)
	    				this.area[y][x+i] = String.valueOf(text.charAt(i));
	    		}
	    		else {
	    			if (y+i < GRID_HEIGHT && x < GRID_WIDTH)
	    				this.area[y+i][x] = String.valueOf(text.charAt(i));
	    		}
	    	}
	    	
//	    	String[] result = text.split(".");
//	        for (int i = 0; i < result.length; i++)
//	            this.area[y][x+i] = result[i];
	    }
	    
	    fillGrid();
//	    GridView gridview = (GridView)findViewById(R.id.gridview);
//	    gridview.setAdapter(new ImageAdapter(this));
	}

	private void fillGrid() {
		for (int y = 0; y < GRID_HEIGHT; y++)
			for (int x = 0; x < GRID_WIDTH; x++)
				setArea(y, x);
	}

	private void setArea(int y, int x) {
        TextView area = (TextView) findViewById(GRID_ID[y][x]);
        area.setOnClickListener(this);
        
        if (this.area[y][x] != null)
        {
        	area.setText(this.area[y][x].toUpperCase());
        }
        else
        {
        	area.setText("");
        	area.setBackgroundResource(R.drawable.back_empty);
        }
	}

	public void onClick(View v) {
		int x = 0;
		int y = 0;
		
		for (int i = 0; i < GRID_HEIGHT; i++)
			for (int j = 0; j < GRID_WIDTH; j++)
				if (v.getId() == GRID_ID[i][j]) {
					y = i;
					x = j;
				}
		
		Word word = null;
	    for (Word entry: this.entries) {
	    	if (x >= entry.getX()-1 && x <= entry.getXMax()-1)
		    	if (y >= entry.getY()-1 && y <= entry.getYMax()-1)
		    		word = entry;
	    }
		
	    try {
		    Intent intent = new Intent(this, CrosswordEditAreaActivity.class);
			intent.putExtra("description", word.getDescription());
			intent.putExtra("y", word.getY());
			intent.putExtra("x", word.getX());
			intent.putExtra("horizontal", word.getHorizontal());
			intent.putExtra("length", word.getLength());
			startActivityForResult(intent, TEXT_INPUT_REQUEST);
	    } catch (Exception e) {
	    	// TODO: exception
	    	e.printStackTrace();
	    }
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        if (requestCode == TEXT_INPUT_REQUEST) {
            if (resultCode == RESULT_OK) {
            	String text = data.getExtras().getString("text_input");
            	boolean horizontal = data.getExtras().getBoolean("horizontal");
            	int y = data.getExtras().getInt("y")-1;
            	int x = data.getExtras().getInt("x")-1;
            	Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    	    	for (int i = 0 ; i < text.length(); i++) {
    	    		if (horizontal)
    	    		{
        	    		if (y < GRID_HEIGHT && x+i < GRID_WIDTH) {
        	    			this.area[y][x+i] = String.valueOf(text.charAt(i));
        	    			setArea(y, x+i);
        	    		}
    	    		}
    	    		else
    	    		{
        	    		if (y+i < GRID_HEIGHT && x < GRID_WIDTH) {
        	    			this.area[y+i][x] = String.valueOf(text.charAt(i));
        	    			setArea(y+i, x);
        	    		}
    	    		}
    	    	}

            }
        }
    }

}
