package com.crossword;

import com.crossword.activity.CrosswordActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private Context context;
	private String[][] area;
	private int height;

	public GridAdapter(Context c, String[][] area, int h) {
		this.context = c;
		this.area = area;
		this.height = h;
	}
	
	@Override
	public int getCount() {
		return 81;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
//		View child = inflater.inflate(R.layout.area);
		
		TextView v = new TextView(this.context);
		v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, this.height));
//		v.setMinimumHeight(42);
		v.setTextSize(20);

		String data = this.area[(int)(position / CrosswordActivity.NUM_COLUMNS)][(int)(position % CrosswordActivity.NUM_COLUMNS)];
		if (data != null && data.length() > 0)
		{
			v.setBackgroundColor(0xFFFFFFFF);
	    	v.setText(data.toUpperCase());
	    	v.setGravity(Gravity.CENTER);
		}
		else
		{
	    	//v.setBackgroundResource(R.drawable.back_empty);
		}
		
		return v;
	}

}
