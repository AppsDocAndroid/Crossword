package com.crossword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
	    
	    ImageButton buttonDone = (ImageButton)findViewById(R.id.button_done);
        buttonDone.setOnClickListener(this);

       	ImageView	iconOrientation = (ImageView)findViewById(R.id.icon_orientation);
       	iconOrientation.setBackgroundResource(this.horizontal ? R.drawable.icon_horizontal : R.drawable.icon_vertical);

	    TextView textDescription = (TextView)findViewById(R.id.text_description);
	    textDescription.setText(this.description);
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
	    EditText input = (EditText) findViewById(R.id.text_input);

	    Intent intent = this.getIntent();
	    if (input.getText().length() > this.length)
	    	intent.putExtra("text_input", String.valueOf(input.getText()).substring(0, this.length));
	    else
	    	intent.putExtra("text_input", String.valueOf(input.getText()));
		intent.putExtra("horizontal", this.horizontal);
		intent.putExtra("y", this.y);
		intent.putExtra("x", this.x);
		this.setResult(RESULT_OK, intent);

		finish();
	}

}
