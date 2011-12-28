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

package com.crossword;

import java.util.HashMap;

import com.crossword.activity.CrosswordActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	public static final int 			AREA_BLOCK = -1;
	public static final int 			AREA_WRITABLE = 0;
	private HashMap<Integer, TextView>	views = new HashMap<Integer, TextView>();
	private Context						context;
	private String[][] 					area;
	private String[][] 					correctionArea;
	private int 						height;
	private boolean						isLower;

	public GridAdapter(Context c, String[][] area, String[][] correctionArea, int h) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
		this.isLower = preferences.getBoolean("grid_is_lower", false);
		this.context = c;
		this.area = area;
		this.height = h;
		this.correctionArea = correctionArea;
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
//		// Stop le traitement si la vue vient d'etre genere
//		if (this.lastPosition == position)
//			return this.views.get(position);
//		this.lastPosition = position;
		
		TextView v = this.views.get(position);
		int y = (int)(position / CrosswordActivity.GRID_WIDTH);
		int x = (int)(position % CrosswordActivity.GRID_WIDTH);
		String data = this.area[y][x];
		String correction = this.correctionArea[y][x];
		
		// Creation du composant
		if (v == null)
		{
			v = new TextView(this.context);
			v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, this.height));
			v.setTextSize(20);
			v.setGravity(Gravity.CENTER);

			if (data != null && data.length() > 0) {
				v.setBackgroundResource(R.drawable.area_empty);
				v.setTag(AREA_WRITABLE);
			} else {
				v.setBackgroundResource(R.drawable.area_block);
				v.setTag(AREA_BLOCK);
			}
			
			this.views.put(position, v);
		}

		// Si la grille est en mode check, colore les fautes en rouge
    	if (CrosswordActivity.currentMode == CrosswordActivity.GRID_MODE.CHECK)
    	{
    		if (data != null) {
    			v.setTextColor(context.getResources().getColor(data.equalsIgnoreCase(correction) ? R.color.normal : R.color.wrong));
    	    	v.setText(this.isLower ? data.toLowerCase() : data.toUpperCase());
    		}
    	}
		// Si la grille est en mode correction, ajoute les bonnes lettres en verte
    	else if (CrosswordActivity.currentMode == CrosswordActivity.GRID_MODE.CORRECTION)
    	{
    		if (data != null && data.equalsIgnoreCase(correction)) {
    			v.setTextColor(context.getResources().getColor(R.color.normal));
    	    	v.setText(this.isLower ? data.toLowerCase() : data.toUpperCase());
    		} else if (correction != null) {
    			v.setTextColor(context.getResources().getColor(R.color.right));
    	    	v.setText(this.isLower ? correction.toLowerCase() : correction.toUpperCase());
    		}
    	}
    	// Sinon mode normal, text en noire
    	else
    	{
    		if (data != null) {
    			v.setTextColor(context.getResources().getColor(R.color.normal));
    	    	v.setText(this.isLower ? data.toLowerCase() : data.toUpperCase());
    		}
    	}
		
		return v;
	}

	public void setLower(boolean isLower) {
		this.isLower = isLower;
	}

}
