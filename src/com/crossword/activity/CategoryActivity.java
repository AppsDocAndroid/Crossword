package com.crossword.activity;

import com.crossword.R;
import com.crossword.R.id;
import com.crossword.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.Toast;

public class CategoryActivity extends Activity implements OnTouchListener {

	private GridView grid;

    float downXValue;
    float downYValue;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.category);
	    
        this.grid = (GridView)findViewById(R.id.grid);
        //this.grid.setAdapter(new GridAdapter(this));
        this.grid.setOnTouchListener(this);
        
        //this.grid.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// Get the action that was done on this touch event
		
		int position = this.grid.pointToPosition((int)event.getX(), (int)event.getY());
		
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                // store the X value when the user's finger was pressed down
                this.downXValue = event.getX();
                this.downYValue = event.getY();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = event.getX();            
                float currentY = event.getY();
                
                if (Math.abs(this.downXValue - currentX) > Math.abs(this.downYValue - currentY))
                	Toast.makeText(this, "horizontale", Toast.LENGTH_SHORT).show();
                else
                	Toast.makeText(this, "verticale", Toast.LENGTH_SHORT).show();
                	

                // going backwards: pushing stuff to the right
                if (this.downXValue < currentX)
                {
//                    // Get a reference to the ViewFlipper
//                     ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
//                     // Set the animation
//                      vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
//                      // Flip!
//                      vf.showPrevious();
                }

                // going forwards: pushing stuff to the left
                if (this.downXValue > currentX)
                {
//                    // Get a reference to the ViewFlipper
//                    ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
//                     // Set the animation
//                     vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
//                      // Flip!
//                     vf.showNext();
                }
                break;
            }
        }

        // if you return false, these actions will not be recorded
        return true;
	}
	
}
