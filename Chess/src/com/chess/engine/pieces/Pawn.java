package com.chess.engine.pieces;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece 
{
	private final static int[] CANDIDATE_MOVE_COORDINATE = { 8, 7, 9, 16 };

	public Pawn(final Alliance pieceAlliance, final int piecePosition)
	{
		super(piecePosition, pieceAlliance);
	}

	@Override // overridden method from superclass
	public Collection<Move> calculateLegalMoves(final Board board) 
	{
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE)
		{
			final int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset * this.pieceAlliance.getDirection());
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
			{ continue; }
			
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
			{ // TODO: more work todo here (pawn promotions)
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
			} else if(currentCandidateOffset == 16 && this.isFirstMove() &&
					 (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
					 (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) { // jump move (moving two Tiles, if first move)
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
						!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} 
			} else if(currentCandidateOffset == 7 && 
					!((BoardUtils.EIGTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
					(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) { // attack move
				if(!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(pieceOnCandidate != null && this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						// TODO: add attack move
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					}
				}
			} else if(currentCandidateOffset == 9 &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
					(BoardUtils.EIGTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) { // attack move
				if(!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(pieceOnCandidate != null && this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						// TODO: add attack move
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString()
	{ return PieceType.PAWN.toString(); }
	
}

// TODO: check comments on vid#12 ! idea with column exclusion (as we had in other piece classes) is better than long if-statement above