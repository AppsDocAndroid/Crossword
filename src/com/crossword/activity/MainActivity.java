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

import com.crossword.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_main_preferences:
        	startActivityForResult(new Intent(this, PeferencesActivity.class), 0);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.button_random).setOnClickListener(this);
        findViewById(R.id.button_list).setOnClickListener(this);
        findViewById(R.id.button_category).setOnClickListener(this);
        findViewById(R.id.button_search).setOnClickListener(this);
    }
    
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_random: {
				Intent intent = new Intent(this, CrosswordActivity.class);
				intent.putExtra("filename", "slam_28_11_11.xml");
				startActivity(intent);
				break;
			}
			case R.id.button_list: {
				Intent intent = new Intent(this, ListActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.button_category: {
				Intent intent = new Intent(this, CategoryActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.button_search: {
				Intent intent = new Intent(this, CategoryActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
    
}
