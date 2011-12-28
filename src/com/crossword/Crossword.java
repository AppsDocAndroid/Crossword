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

import java.io.File;

import android.app.Application;
import android.content.Context;

public class Crossword extends Application {

	public static final String	GRID_DIRECTORY = "/data/data/com.crossword/grid/";
	public static final int		REQUEST_PREFERENCES = 2;
	public static final int 	GRID_WIDTH = 9;
	public static final int 	GRID_HEIGHT = 10;
	public static final float 	KEYBOARD_OVERLAY_OFFSET = 90;

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