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
import java.util.ArrayList;
import java.util.Calendar;

import org.xml.sax.helpers.DefaultHandler;

import com.crossword.Crossword;
import com.crossword.CrosswordException;
import com.crossword.DownloadManager;
import com.crossword.R;
import com.crossword.SAXFileHandler;
import com.crossword.adapter.GridListAdapter;
import com.crossword.data.Grid;
import com.crossword.parser.GridParser;
import com.crossword.parser.GridListParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class GridListActivity extends Activity implements OnItemClickListener {

	private GridListAdapter	gridAdapter;
	private ListView		gridListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gridlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_main_preferences:
        	startActivityForResult(new Intent(this, PeferencesActivity.class), Crossword.REQUEST_PREFERENCES);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gridlist);
	    this.initComponents();
	}

	private void initComponents()
	{
		// Download grid list
		File gridListFile = new File(Crossword.GRIDLIST_LOCAL_PATH);
		long now = Calendar.getInstance().getTimeInMillis();
		long expire = gridListFile.lastModified() + Crossword.GRIDLIST_OUTDATED;
		if (gridListFile.exists() == false || now > expire)
		{
			gridListFile.lastModified();
			ArrayList<String> gridList = null;
	    	GridListParser listGridParser = new GridListParser();
			DownloadManager.downloadListGrid();
			try {
				SAXFileHandler.read((DefaultHandler)listGridParser, Crossword.GRIDLIST_LOCAL_PATH);
				gridList = listGridParser.getList();
			} catch (CrosswordException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				finish();
			}

			// Download grids
			for (String filename: gridList) {
				File file = new File(String.format(Crossword.GRID_LOCAL_PATH, filename));
				if (file.exists() == false) {
					DownloadManager.downloadGrid(filename);
				}
			}
		}

		// Read grids
	    this.gridAdapter = new GridListAdapter(this);
	    File directoryToScan = new File(Crossword.GRID_DIRECTORY); 
	    File files[] = directoryToScan.listFiles();
	    try {
	    	for (File file: files) {
		    	GridParser parser = new GridParser();
		    	parser.setFileName(file.getName());
				SAXFileHandler.read((DefaultHandler)parser, file.getAbsolutePath());
				this.gridAdapter.addGrid(parser.getData());
	    	}
		} catch (CrosswordException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
	    }

	    // Set listview
	    this.gridListView = (ListView)findViewById(R.id.gridListView);
	    this.gridListView.setOnItemClickListener(this);
	    this.gridListView.setAdapter(this.gridAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> p, View v, int i, long l) {
		Grid grid = (Grid)this.gridAdapter.getItem(i);
		Intent intent = new Intent(this, GameGridActivity.class);
		intent.putExtra("filename", grid.getFileName());
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		this.initComponents();
//		this.gridAdapter.notifyDataSetChanged();
	}

}
