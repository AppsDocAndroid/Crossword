package com.crossword.components;

import java.util.ArrayList;
import java.util.HashMap;

import com.crossword.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridListAdapter extends BaseAdapter {

	private HashMap<Integer, View>	views = new HashMap<Integer, View>();
	private ArrayList<Grid>			data = new ArrayList<Grid>();
	private Context 				context;

	public GridListAdapter(Context c) {
		this.context = c;
	}
	
	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public Object getItem(int position) {
		return this.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = this.views.get(position);
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.gridlist_item, null);
			
			TextView name = (TextView)v.findViewById(R.id.name);
			name.setText(this.data.get(position).getName());
			
			TextView description = (TextView)v.findViewById(R.id.description);
			description.setText(this.data.get(position).getDescription());

			TextView author = (TextView)v.findViewById(R.id.author);
			author.setText(this.data.get(position).getAuthor());
			
			if (this.data.get(position).getDate() != null) {
				TextView date = (TextView)v.findViewById(R.id.date);
				date.setText(this.data.get(position).getDate().toString());
			}
			
			TextView level = (TextView)v.findViewById(R.id.level);
			level.setText(String.valueOf(this.data.get(position).getLevel()));

			TextView percent = (TextView)v.findViewById(R.id.percent);
			percent.setText(String.valueOf(this.data.get(position).getPercent()));
			
			this.views.put(position, v);
		}
		return v;
	}

	public void addGrid(Grid item) {
		this.data.add(item);
	}

}
