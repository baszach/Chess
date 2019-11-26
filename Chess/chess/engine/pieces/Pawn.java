package com.chess.engine.pieces;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece 
{
	private final static int[] CANDIDATE_MOVE_COORDINATE = { 8, 16, 7, 9 };
	
	private int killedPieces;

	public Pawn(final Alliance pieceAlliance, final int piecePosition, final int killedPieces) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, true);
		this.killedPieces = killedPieces;
	}
	
	public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove, final int killedPieces) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
		this.killedPieces = killedPieces;
	}

	@Override 
	public Collection<Move> calculateLegalMoves(final Board board)
	{
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE)
		{
			final int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset * this.pieceAlliance.getDirection());
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
			{ continue; }
			
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied())
			{
				if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
				} else {
					legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
				}
			} else if(currentCandidateOffset == 16 && this.isFirstMove() &&
					 ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
					 (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) { // jump move (moving two Tiles, if first move)
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
						!board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
				} 
			} else if(currentCandidateOffset == 7 && 
					!((BoardUtils.EIGTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
					(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(pieceOnCandidate != null && this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
							legalMoves.add(new PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						} else {
							if(this.killedTwoPieces()) {
								legalMoves.add(new Move.PawnUpgrade(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
							} else {
								legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
							}
						}
					}
				} else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							if(this.killedTwoPieces()) {
								legalMoves.add(new Move.PawnUpgrade(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
							} else {
								legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
							}
						}
					}
				} 
			} else if(currentCandidateOffset == 9 &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() || 
					(BoardUtils.EIGTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) { // attack move
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(pieceOnCandidate != null && this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
							legalMoves.add(new PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						} else {
							if(this.killedTwoPieces()) {
								legalMoves.add(new Move.PawnUpgrade(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
							} else {
								legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
							}
						}
					}
				} else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							if(this.killedTwoPieces()) {
								legalMoves.add(new Move.PawnUpgrade(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
							} else {
								legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
							}
						}
					}
				}	
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public Pawn movePiece(final Move move) 
	{ 
		if(move.isAttack()) {
			this.killedPieces++;
		}
		return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate(), this.killedPieces); 
	}
	
	@Override
	public String toString()
	{ return PieceType.PAWN.toString(); }
	
	public Piece getPromotionPiece() {
		return new Queen(this.pieceAlliance, this.piecePosition, false);
	}
	
	public Piece getUpgradePiece() {
		return new Commoner(this.piecePosition, this.pieceAlliance, false);
	}
	
	public Boolean killedTwoPieces() {
		return this.killedPieces >= 1 ;
	}
}