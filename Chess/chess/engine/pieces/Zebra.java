package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.pieces.Piece.PieceType;

public class Zebra extends Piece {
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = { -16, -9, -8, -7, 14, 16, 18 }; //TODO find good moves sets

	public Zebra(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
	}
	
	public Zebra(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(pieceType, piecePosition, pieceAlliance, isFirstMove);
	}
	
	@Override // overridden method from superclass
	public Collection<Move> calculateLegalMoves(final Board board)
	{
		final List<Move> legalMoves = new ArrayList<>(); // list with legal moves
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) // for-each loop 
		{
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset * this.getPieceAlliance().getDirection(); // set destination coordinate
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
	public Zebra movePiece(final Move move) 
	{ 
		return new Zebra(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate()); 
	}
	
	@Override
	public String toString()
	{ return PieceType.ZEBRA.toString(); }
	
	//TODO fix Exclusions
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 1.column
	{
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -18 || candidateOffset == 14 ||
				candidateOffset == -7 || candidateOffset == 9);
	}	
	
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 2.column
	{
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == 14 || candidateOffset == 18);
	}
	
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 7. column
	{
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == 18 || candidateOffset == -14);
	}
	
	private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset) // cover case 8. column
	{
		return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == -14 ||
				candidateOffset == 18 || candidateOffset == -7);
	}
}