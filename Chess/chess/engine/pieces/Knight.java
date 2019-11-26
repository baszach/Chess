package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import com.chess.engine.alliance.Alliance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Knight extends Piece 
{
	private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 }; // array with possible moves

	public Knight(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);	// constructor for Knight with position and alliance
	}
	
	public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);	// constructor for Knight with position and alliance
	}

	@Override // overridden method from superclass
	public Collection<Move> calculateLegalMoves(final Board board)
	{
		final List<Move> legalMoves = new ArrayList<>(); // list with legal moves
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) // for-each loop 
		{
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset; // set destination coordinate
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) // checks if candidate is valid | e.g. 5-17 == -12 -> not valid tile (-12 doesn't exist) 
			{
				if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
						isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
						isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
						isEigthColumnExclusion(this.piecePosition, currentCandidateOffset))
				{ continue; }
				
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestinationTile.isTileOccupied())
				{
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate)); // new Move if tile not occupied (negated !)
				} else { // if Tile is occupied...
					final Piece pieceAtDestination = candidateDestinationTile.getPiece(); //...check the piece at destination
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); //...check the pieces alliance (friendly or enemy)
					if(this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // if piece at destination is enemy piece -> new Move
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves); // return the List of legal moves for the Knight Piece
	}
	
	@Override
	public Knight movePiece(final Move move) 
	{ 
		return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate()); 
	}
	
	@Override
	public String toString()
	{ return PieceType.KNIGHT.toString(); }
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 1.column
	{
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
				candidateOffset == 6 || candidateOffset == 15);
	}	
	
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 2.column
	{
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
	}
	
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 7. column
	{
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
	}
	
	private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset) // cover case 8. column
	{
		return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 || 
				candidateOffset == 10 || candidateOffset == 17);
	}
}