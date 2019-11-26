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

public class Commoner extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE = {-16, -9, -7, -2, 2, 7, 9, 16};
 	
	public Commoner(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.COMMONER, piecePosition, pieceAlliance, true);
	}
	
	public Commoner(final int piecePosition, final Alliance pieceAlliance, final Boolean isFirstMove) {
		super(PieceType.COMMONER, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATE) {
			int candidateDestinationCoordinate = this.piecePosition;
			
			if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
					isSecondColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
					isSeventhColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
					isEigthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) { 
				continue; 
			}		
			
			candidateDestinationCoordinate += candidateCoordinateOffset;
			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) 
			{
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} else {
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
					if (this.pieceAlliance != pieceAlliance) {
						legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	
	}
	
	@Override
	public Piece movePiece(final Move move) {
		return new Commoner(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	@Override
	public String toString() {
		return PieceType.COMMONER.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -2 || candidateOffset == 7 || candidateOffset == -9);
	}
	
	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -2);
	}
	
	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == 2);
	}
	
	private static boolean isEigthColumnExclusion(final int currentPosition, final int candidateOffset)	{
		return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == 2 || candidateOffset == -7 || candidateOffset == 9);
	}
}