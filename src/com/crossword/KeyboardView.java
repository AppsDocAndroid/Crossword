package com.crossword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KeyboardView extends ViewGroup {

	public KeyboardView(Context context) {
		super(context);
		this.initComponent(context);
	}

	private void initComponent(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.keyboard, null, false);
		this.addView(v);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
	}

}
