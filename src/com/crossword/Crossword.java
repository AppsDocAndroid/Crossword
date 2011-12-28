package com.crossword;

import java.io.File;

import android.app.Application;
import android.content.Context;

public class Crossword extends Application {

	public static final String	GRID_DIRECTORY = "/data/data/com.crossword/grid/";
	public static final int		REQUEST_PREFERENCES = 2;


    private static Context context;

    public void onCreate() {
        Crossword.context = getApplicationContext();

		File directory = new File(GRID_DIRECTORY);
		if (directory.exists() == false)
			directory.mkdir();
    }

    public static Context getAppContext() {
    	return Crossword.context;
    }
}