package com.crossword.keyboard;

import com.crossword.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class KeyboardView extends LinearLayout implements OnClickListener {

	private KeyboardViewInterface delegate;
	
	/** Constructeur
	 * 
	 * @param context
	 */
	public KeyboardView(Context context) {
		super(context);
		this.initComponent();
	}
	
	/** Constructeur
	 * 
	 * @param context
	 * @param attrs
	 */
	public KeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initComponent();
	}

	/** Initialisation des composants
	 */
	public void initComponent() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		ViewGroup keyboard = (ViewGroup) inflater.inflate(R.layout.keyboard, null);
		int size = LinearLayout.LayoutParams.FILL_PARENT;
		keyboard.setLayoutParams(new LinearLayout.LayoutParams(size, size));
		this.addView(keyboard);

		this.findViewById(R.id.buttonA).setOnClickListener(this);
		this.findViewById(R.id.buttonB).setOnClickListener(this);
		this.findViewById(R.id.buttonC).setOnClickListener(this);
		this.findViewById(R.id.buttonD).setOnClickListener(this);
		this.findViewById(R.id.buttonE).setOnClickListener(this);
		this.findViewById(R.id.buttonF).setOnClickListener(this);
		this.findViewById(R.id.buttonG).setOnClickListener(this);
		this.findViewById(R.id.buttonH).setOnClickListener(this);
		this.findViewById(R.id.buttonI).setOnClickListener(this);
		this.findViewById(R.id.buttonJ).setOnClickListener(this);
		this.findViewById(R.id.buttonK).setOnClickListener(this);
		this.findViewById(R.id.buttonL).setOnClickListener(this);
		this.findViewById(R.id.buttonM).setOnClickListener(this);
		this.findViewById(R.id.buttonN).setOnClickListener(this);
		this.findViewById(R.id.buttonO).setOnClickListener(this);
		this.findViewById(R.id.buttonP).setOnClickListener(this);
		this.findViewById(R.id.buttonQ).setOnClickListener(this);
		this.findViewById(R.id.buttonR).setOnClickListener(this);
		this.findViewById(R.id.buttonS).setOnClickListener(this);
		this.findViewById(R.id.buttonT).setOnClickListener(this);
		this.findViewById(R.id.buttonU).setOnClickListener(this);
		this.findViewById(R.id.buttonV).setOnClickListener(this);
		this.findViewById(R.id.buttonW).setOnClickListener(this);
		this.findViewById(R.id.buttonX).setOnClickListener(this);
		this.findViewById(R.id.buttonY).setOnClickListener(this);
		this.findViewById(R.id.buttonZ).setOnClickListener(this);
		this.findViewById(R.id.buttonDELETE).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonA: this.delegate.onReceiveKey("A"); break;
		case R.id.buttonB: this.delegate.onReceiveKey("B"); break;
		case R.id.buttonC: this.delegate.onReceiveKey("C"); break;
		case R.id.buttonD: this.delegate.onReceiveKey("D"); break;
		case R.id.buttonE: this.delegate.onReceiveKey("E"); break;
		case R.id.buttonF: this.delegate.onReceiveKey("F"); break;
		case R.id.buttonG: this.delegate.onReceiveKey("G"); break;
		case R.id.buttonH: this.delegate.onReceiveKey("H"); break;
		case R.id.buttonI: this.delegate.onReceiveKey("I"); break;
		case R.id.buttonJ: this.delegate.onReceiveKey("J"); break;
		case R.id.buttonK: this.delegate.onReceiveKey("K"); break;
		case R.id.buttonL: this.delegate.onReceiveKey("L"); break;
		case R.id.buttonM: this.delegate.onReceiveKey("M"); break;
		case R.id.buttonN: this.delegate.onReceiveKey("N"); break;
		case R.id.buttonO: this.delegate.onReceiveKey("O"); break;
		case R.id.buttonP: this.delegate.onReceiveKey("P"); break;
		case R.id.buttonQ: this.delegate.onReceiveKey("Q"); break;
		case R.id.buttonR: this.delegate.onReceiveKey("R"); break;
		case R.id.buttonS: this.delegate.onReceiveKey("S"); break;
		case R.id.buttonT: this.delegate.onReceiveKey("T"); break;
		case R.id.buttonU: this.delegate.onReceiveKey("U"); break;
		case R.id.buttonV: this.delegate.onReceiveKey("V"); break;
		case R.id.buttonW: this.delegate.onReceiveKey("W"); break;
		case R.id.buttonX: this.delegate.onReceiveKey("X"); break;
		case R.id.buttonY: this.delegate.onReceiveKey("Y"); break;
		case R.id.buttonZ: this.delegate.onReceiveKey("Z"); break;
		case R.id.buttonDELETE: this.delegate.onReceiveKey(" "); break;
		}
	}

	public void setDelegate(KeyboardViewInterface delegate) {
		this.delegate = delegate; 
}
}