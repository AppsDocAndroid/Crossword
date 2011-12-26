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
        
        ImageView buttonRandom = (ImageView) findViewById(R.id.button_random);
        buttonRandom.setOnClickListener(this);

        ImageView buttonCategory = (ImageView) findViewById(R.id.button_category);
        buttonCategory.setOnClickListener(this);

        ImageView buttonSearch = (ImageView) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(this);
}
    
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_random: {
				Intent intent = new Intent(this, CrosswordActivity.class);
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
