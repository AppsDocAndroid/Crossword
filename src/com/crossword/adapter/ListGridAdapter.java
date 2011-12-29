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

package com.crossword.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.crossword.R;
import com.crossword.data.Grid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListGridAdapter extends BaseAdapter {

	private HashMap<Integer, View>	views = new HashMap<Integer, View>();
	private ArrayList<Grid>			data = new ArrayList<Grid>();
	private Context 				context;

	public ListGridAdapter(Context c) {
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
			
			TextView author = (TextView)v.findViewById(R.id.author);
			author.setText(String.format(this.context.getString(R.string.author_format), this.data.get(position).getAuthor()));
			
			if (this.data.get(position).getDate() != null) {
				TextView date = (TextView)v.findViewById(R.id.date);
				date.setText(this.data.get(position).getDate().toString());
			}
			
			TextView level = (TextView)v.findViewById(R.id.level);
			level.setText(String.format(this.context.getString(R.string.level_format), this.data.get(position).getLevel()));

			ImageView imgPercent = (ImageView)v.findViewById(R.id.percent);
			int percent = this.data.get(position).getPercent();
			if (percent == 0)
				imgPercent.setImageResource(R.drawable.progress_0);
			else if (percent <= 15)
				imgPercent.setImageResource(R.drawable.progress_1);
			else if (percent <= 30)
				imgPercent.setImageResource(R.drawable.progress_2);
			else if (percent <= 45)
				imgPercent.setImageResource(R.drawable.progress_3);
			else if (percent <= 60)
				imgPercent.setImageResource(R.drawable.progress_4);
			else if (percent <= 75)
				imgPercent.setImageResource(R.drawable.progress_5);
			else if (percent <= 90)
				imgPercent.setImageResource(R.drawable.progress_6);
			else if (percent <= 99)
				imgPercent.setImageResource(R.drawable.progress_7);
			else
				imgPercent.setImageResource(R.drawable.progress_8);
			
			this.views.put(position, v);
		}

		return v;
	}

	public void addGrid(Grid item) {
		this.data.add(item);
	}

}
