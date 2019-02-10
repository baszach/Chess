package com.chess.engine.board;

import com.google.common.collect.ImmutableList;
import com.chess.engine.pieces.Piece;

public abstract class Move // class for Moves inherited by subclasses
{
	final Board board;
	final Piece movedPiece;
	final int destinationCoordinate;
	
	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate)
	{
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
	}
	
	public static final class MajorMove extends Move // class for Major Move (just a normal move)
	{
		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate)
		{
			super(board, movedPiece, destinationCoordinate);
		}
	}
	
	public static final class AttackMove extends Move // class for Attack Move
	{
		
		final Piece attackedPiece;
		
		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) 
		{
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
	}
}