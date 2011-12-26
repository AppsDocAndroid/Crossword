package com.crossword.activity;

import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import com.crossword.CrosswordException;
import com.crossword.CrosswordParser;
import com.crossword.GridAdapter;
import com.crossword.R;
import com.crossword.SAXFileHandler;
import com.crossword.R.id;
import com.crossword.R.layout;
import com.crossword.components.KeyboardView;
import com.crossword.components.KeyboardViewInterface;
import com.crossword.components.Word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CrosswordActivity extends Activity implements OnClickListener, OnTouchListener, KeyboardViewInterface {

	public final static int NUM_COLUMNS = 9;
	
	final static int TEXT_INPUT_REQUEST = 1;
	
	final static int GRID_WIDTH = 9;
	final static int GRID_HEIGHT = 10;

	private LinearLayout mainLayout;
	private String[][] area;
	private ArrayList<Word> entries;
	
    float downXValue;
    float downYValue;
    private int	position = -1;

	private GridView grid;

	private KeyboardView keyboard;

	private boolean horizontal;

	private int x;

	private int y;

	private GridAdapter gridAdapter;

	private TextView description;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.crossword);
	    
        this.mainLayout = (LinearLayout)findViewById(R.id.main_layout);

        this.grid = (GridView)findViewById(R.id.grid);
        this.grid.setOnTouchListener(this);
        this.grid.setNumColumns(NUM_COLUMNS);

        this.keyboard = (KeyboardView)findViewById(R.id.keyboard);
        this.keyboard.setDelegate(this);

        this.description = (TextView)findViewById(R.id.description);

	    this.area = new String[GRID_HEIGHT][GRID_WIDTH];

	    CrosswordParser parser = new CrosswordParser();
	    try {
			SAXFileHandler.getFeeds(parser);
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
	    
        Display display = getWindowManager().getDefaultDisplay();
        this.gridAdapter = new GridAdapter(this, this.area, display.getWidth() / NUM_COLUMNS);
        this.grid.setAdapter(this.gridAdapter);
	    //fillGrid();
//	    GridView gridview = (GridView)findViewById(R.id.gridview);
//	    gridview.setAdapter(new ImageAdapter(this));
	}

	public void onClick(View v) {
//		int x = 0;
//		int y = 0;
//		
//		for (int i = 0; i < GRID_HEIGHT; i++)
//			for (int j = 0; j < GRID_WIDTH; j++)
//				if (v.getId() == GRID_ID[i][j]) {
//					y = i;
//					x = j;
//				}
//		
//		Word word = null;
//	    for (Word entry: this.entries) {
//	    	if (x >= entry.getX()-1 && x <= entry.getXMax()-1)
//		    	if (y >= entry.getY()-1 && y <= entry.getYMax()-1)
//		    		word = entry;
//	    }
//		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// Get the action that was done on this touch event
		
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	// Store grid position
            	this.position = this.grid.pointToPosition((int)event.getX(), (int)event.getY());
        		View child = this.grid.getChildAt(position);
        		if (child != null)
        			child.setBackgroundColor(0xFFD5DBFF);
        			//child.setBackgroundResource(R.drawable.back_select);

        		// store the X value when the user's finger was pressed down
                this.downXValue = event.getX();
                this.downYValue = event.getY();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
        		View child = this.grid.getChildAt(position);
        		if (child != null)
        			child.setBackgroundColor(0xFFFFFFFF);
        			//child.setBackgroundResource(R.drawable.back);

        		// Get the X value when the user released his/her finger
                float currentX = event.getX();            
                float currentY = event.getY();
                
                this.horizontal = Math.abs(this.downXValue - currentX) > Math.abs(this.downYValue - currentY);
                this.x = this.position % NUM_COLUMNS;
                this.y = this.position / NUM_COLUMNS;
                
                Word word = null;
        	    for (Word entry: this.entries) {
        	    	if (entry.getHorizontal() == horizontal)
        	    		if (x >= entry.getX()-1 && x <= entry.getXMax()-1)
        	    			if (y >= entry.getY()-1 && y <= entry.getYMax()-1)
        	    				word = entry;
        	    }
        	    
        	    this.description.setText(word.getDescription());

        	    break;
            }
        }

        // if you return false, these actions will not be recorded
        return true;
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
    	    		if (horizontal && y < GRID_HEIGHT && x+i < GRID_WIDTH)
    	    			this.area[y][x+i] = String.valueOf(text.charAt(i));
    	    		else if (y+i < GRID_HEIGHT && x < GRID_WIDTH)
    	    			this.area[y+i][x] = String.valueOf(text.charAt(i));
    	    	}

            }
        }
    }

	@Override
	public void onReceiveKey(String value) {
		System.out.println("receive key: " + value + ", insert in: " + x + "x" + y);
		
		if (value.equals("DEL")) {
			this.area[y][x] = " ";
			if (this.horizontal) this.x--;
			else this.y--;
		}
		else
		{
			this.area[y][x] = value;
			if (this.horizontal) this.x++;
			else this.y++;
		}
		this.gridAdapter.notifyDataSetChanged();
	}

}
