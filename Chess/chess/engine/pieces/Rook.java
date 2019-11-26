package com.chess.engine.pieces;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Rook extends Piece
{
	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATE = { -8, -1, 1, 8 }; // Vector styled move -> multipliers of values in array
	
	public Rook(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.ROOK, piecePosition, pieceAlliance, true);
	}
	
	public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override // overridden method from superclass
	public Collection<Move> calculateLegalMoves(final Board board)	
	{
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int candidateCooridinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATE)
		{
			int candidateDestinationCoordinate = this.piecePosition;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
			{
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCooridinateOffset) ||
						isEigthColumnExclusion(candidateDestinationCoordinate, candidateCooridinateOffset))
				{ break; }
				
				candidateDestinationCoordinate += candidateCooridinateOffset;
				if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) 
				{
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					if (!candidateDestinationTile.isTileOccupied()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate)); // new Move if tile not occupied (negated !)
					} else { // if Tile is occupied...
						final Piece pieceAtDestination = candidateDestinationTile.getPiece(); // ...check the piece at destination
						final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); // ...check the pieces alliance (friendly or enemy)
						if (this.pieceAlliance != pieceAlliance) {
							legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // if piece at destination is enemy piece -> new Move
						}
						break; 
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public Rook movePiece(final Move move) 
	{ 
		return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate()); 
	}
	
	@Override
	public String toString()
	{ return PieceType.ROOK.toString(); }
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset)
	{
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}
	
	private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset)
	{
		return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == 1);
	}
}