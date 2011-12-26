package com.crossword.activity;

import com.crossword.R;
import com.crossword.R.drawable;
import com.crossword.R.id;
import com.crossword.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CrosswordEditAreaActivity extends Activity implements OnClickListener  {
	
	private int	x;
	private int	y;
	private String description;
	private int length;
	private boolean horizontal;
	private EditText editText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.crossword_edit_area);
	    
	    Bundle extras = this.getIntent().getExtras();
	    this.description = extras.getString("description");
	    this.horizontal = extras.getBoolean("horizontal");
	    this.length = extras.getInt("length");
	    this.x = extras.getInt("x");
	    this.y = extras.getInt("y");
	    
	    System.out.println("Edit word: " + this.x + " x " + this.y);
	    
	    this.editText = (EditText) findViewById(R.id.text_input);

	    ImageButton buttonDone = (ImageButton)findViewById(R.id.button_done);
        buttonDone.setOnClickListener(this);

       	ImageView	iconOrientation = (ImageView)findViewById(R.id.icon_orientation);
       	iconOrientation.setBackgroundResource(this.horizontal ? R.drawable.icon_horizontal : R.drawable.icon_vertical);

	    TextView textDescription = (TextView)findViewById(R.id.text_description);
	    textDescription.setText(this.description);
	    
	    InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
	    if (imm != null) {
	    	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	    }
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	returnInput();
           return true;
        }
        return false;
    }
    
    public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_done:
			returnInput();
		}
	}

	private void returnInput() {

	    Intent intent = this.getIntent();
	    if (this.editText.getText().length() > this.length)
	    	intent.putExtra("text_input", String.valueOf(this.editText.getText()).substring(0, this.length));
	    else
	    	intent.putExtra("text_input", String.valueOf(this.editText.getText()));
		intent.putExtra("horizontal", this.horizontal);
		intent.putExtra("y", this.y);
		intent.putExtra("x", this.x);
		this.setResult(RESULT_OK, intent);

		finish();
	}

}
