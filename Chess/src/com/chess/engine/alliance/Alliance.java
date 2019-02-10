package com.chess.engine.alliance;

public enum Alliance // contains the constants White and Black
{
	WHITE
	{
		@Override
		public int getDirection() 
		{ return -1; }
		@Override
		public boolean isWhite()
		{ return true; }
		@Override
		public boolean isBlack()
		{ return false; }
	},
 
	BLACK
	{
		@Override
		public int getDirection() 
		{ return 1; }
		@Override
		public boolean isBlack()
		{ return true; }
		@Override
		public boolean isWhite()
		{ return false; }
		
	};
	
	public abstract int getDirection();
	public abstract boolean isWhite();
	public abstract boolean isBlack();
}