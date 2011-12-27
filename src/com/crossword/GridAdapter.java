package com.crossword;

import java.util.ArrayList;
import java.util.HashMap;

import com.crossword.activity.CrosswordActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private HashMap<Integer, TextView>	views = new HashMap<Integer, TextView>();
	private Context					context;
	private String[][] 				area;
	private int 					height;

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
	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView v = this.views.get(position);
		String data = this.area[(int)(position / CrosswordActivity.GRID_WIDTH)][(int)(position % CrosswordActivity.GRID_WIDTH)];
		
		// Creation du composant
		if (v == null)
		{
			v = new TextView(this.context);
			v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, this.height));
			v.setTextSize(20);
			v.setGravity(Gravity.CENTER);
			if (data != null && data.length() > 0)
				v.setBackgroundResource(R.drawable.area_empty);
			else
				v.setBackgroundResource(R.drawable.area_block);
			this.views.put(position, v);
		}

		// Mise a jour du texte
		if (data != null && data.length() > 0)
	    	v.setText(data.toUpperCase());
		
		return v;
	}

}
