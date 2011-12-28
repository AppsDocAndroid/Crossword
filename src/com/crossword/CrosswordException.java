package com.crossword;

@SuppressWarnings("serial")
public class CrosswordException extends Exception {

	public static enum ExceptionType {FILE_NOT_FOUND, GRID_NOT_FOUND, SAV_NOT_FOUND};
	public ExceptionType type;
	
	public CrosswordException(ExceptionType t) {
		this.type = t;
	}

	@Override
	public String getMessage() {
		switch (this.type) {
		case FILE_NOT_FOUND:
			return Crossword.getAppContext().getString(R.string.exception_file_not_found);
		case GRID_NOT_FOUND:
			return Crossword.getAppContext().getString(R.string.exception_grid_not_found);
		case SAV_NOT_FOUND:
			return Crossword.getAppContext().getString(R.string.exception_sav_not_found);
		}
		return Crossword.getAppContext().getString(R.string.exception_unknow);
	}
}
