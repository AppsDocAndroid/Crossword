package com.crossword.activity;

import com.crossword.R;
import com.crossword.R.id;
import com.crossword.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {
    
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
