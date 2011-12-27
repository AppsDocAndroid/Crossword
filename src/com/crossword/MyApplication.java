package com.crossword;

import java.io.File;

import com.crossword.activity.CrosswordActivity;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        MyApplication.context = getApplicationContext();

		File directory = new File(CrosswordActivity.GRID_DIRECTORY);
		if (directory.exists() == false)
			directory.mkdir();
    }

    public static Context getAppContext() {
    	return MyApplication.context;
    }
}