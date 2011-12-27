package com.crossword.activity;

import java.io.File;
import java.util.ArrayList;

import com.crossword.R;
import com.crossword.R.id;
import com.crossword.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity implements OnItemClickListener {

	private GridView grid;

    float downXValue;
    float downYValue;

	private ListView gridListView;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gridlist);
	    
	    this.gridListView = (ListView)findViewById(R.id.gridListView);
	    this.gridListView.setOnItemClickListener(this);
	    ArrayList<String> fileList = new ArrayList<String>();

	    File directoryToScan = new File(CrosswordActivity.GRID_DIRECTORY); 
	    File files []= directoryToScan.listFiles();
	    for (File file: files)
	    	fileList.add(file.getName());
	    
	    //this.gridListView.setAdapter(new ArrayAdapter<String>(this, R.layout.gridlist_item, listeStrings));
	    this.gridListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList));

	}

	@Override
	public void onItemClick(AdapterView<?> p, View v, int i, long l) {
		String filename = (String)((TextView)v).getText();
		Intent intent = new Intent(this, CrosswordActivity.class);
		intent.putExtra("filename", filename);
		startActivity(intent);
	}

}
