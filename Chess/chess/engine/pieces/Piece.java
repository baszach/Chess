package com.chess.engine.pieces;

import java.util.Collection;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;
import com.google.common.collect.ImmutableList;

public abstract class Piece // TODO: Check out Enum !
{
	protected final int piecePosition;
	protected final Alliance pieceAlliance; // (black or white)
	protected final boolean isFirstMove;
	protected final PieceType pieceType;
	private final int cachedHashCode;
	
	public Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove)
	{
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove = isFirstMove;
		this.cachedHashCode = computeHashCode();
	}
	
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
				pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}
	
	public int getPiecePosition()
	{
		return this.piecePosition;
	}
	
	public Alliance getPieceAlliance()
	{
		return this.pieceAlliance;
	}
	
	public PieceType getPieceType()
	{
		return this.pieceType;
	}
	
	public int getPieceValue() 
	{
		return this.pieceType.getPieceValue();
	}
	
	public boolean isFirstMove()
	{
		return this.isFirstMove;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board); // method which will be overridden by pieces to calculate moves
	// TODO: Check out Set, List, Collection !
	
	public abstract Piece movePiece(Move move);
	
	public enum PieceType
	{
		PAWN("P", 100) {
			@Override
			public boolean isKing() 
			{ return false; }

			@Override
			public boolean isRook() 
			{ return false; }
		},
		SIEGE("S", 200) {
			@Override
			public boolean isKing()
			{ return false;}
			
			@Override
			public boolean isRook() 
			{ return false; }
		},
		COMMONER("C", 300) {
			@Override
			public boolean isKing()
			{ return false;}
			
			@Override
			public boolean isRook() 
			{ return false; }
		},
		ZEBRA("Z", 300) {
			@Override
			public boolean isKing() 
			{ return false; }
			
			@Override
			public boolean isRook() 
			{ return false; }
		},
		KNIGHT("N", 300) {
			@Override
			public boolean isKing() 
			{ return false; }
			
			@Override
			public boolean isRook() 
			{ return false; }
		}, 
		BISHOP("B", 300) {
			@Override
			public boolean isKing() 
			{ return false; }
			
			@Override
			public boolean isRook() 
			{ return false; }
		}, 
		ROOK("R", 500) {
			@Override
			public boolean isKing() 
			{ return false; }
			
			@Override
			public boolean isRook() 
			{ return true; }
		}, 
		QUEEN("Q", 900) {
			@Override
			public boolean isKing() 
			{ return false; }
			
			@Override
			public boolean isRook() 
			{ return false; }
		}, 
		KING("K", 10000) {
			@Override
			public boolean isKing() 
			{ return true; }
			
			@Override
			public boolean isRook() 
			{ return false; }
		};
		
		private String pieceName;
		private int pieceValue;
		
		PieceType(final String pieceName, final int pieceValue) 
		{
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		@Override
		public String toString()
		{ return this.pieceName; }
		
		public int getPieceValue() {
			return this.pieceValue;
		}
		
		public abstract boolean isKing();
		
		public abstract boolean isRook();
	}
	
}