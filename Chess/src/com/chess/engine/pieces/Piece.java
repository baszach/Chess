package com.chess.engine.pieces;

import java.util.Collection;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;
import com.google.common.collect.ImmutableList;

public abstract class Piece // TODO: Check out Enum !
{
	protected final int piecePosition; // position of the piece
	protected final Alliance pieceAlliance; // alliance of piece (black or white)
	protected final boolean isFirstMove;
	
	public Piece(final int piecePosition, final Alliance pieceAlliance)
	{
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		// TODO: more work todo here (regarding the First Move)
		this.isFirstMove = false;
	}
	
	public int getPiecePosition()
	{
		return this.piecePosition;
	}
	
	public Alliance getPieceAlliance()
	{
		return this.pieceAlliance;
	}
	
	public boolean isFirstMove()
	{
		return this.isFirstMove;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board); // method which will be overridden by pieces to calculate moves
	// TODO: Check out Set, List, Collection !
	
	
	public enum PieceType
	{
		PAWN("P"), 
		KNIGHT("N"), 
		BISHOP("B"), 
		ROOK("R"), 
		QUEEN("Q"), 
		KING("K");
		
		private String pieceName;
		
		PieceType(String pieceName) 
		{
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString()
		{ return this.pieceName; }
	}
	
}