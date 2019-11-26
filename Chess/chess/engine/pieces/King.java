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
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Move.MajorAttackMove;

public class King extends Piece
{
	private final static int[] CANDIDATE_MOVE_COORDINATE = { -9, -8, -7, -1, 1, 7, 8, 9 };
	
	public King(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KING, piecePosition, pieceAlliance, true);
	}
	
	public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override // overridden method from superclass
	public Collection<Move> calculateLegalMoves(final Board board)
	{
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE)
		{
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;				
			if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
					isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))
			{ continue; }
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
			{
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestinationTile.isTileOccupied())
				{
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate)); // new Move if tile not occupied (negated !)
				} else { // if Tile is occupied...
					final Piece pieceAtDestination = candidateDestinationTile.getPiece(); //...check the piece at destination
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance(); //...check the pieces alliance (friendly or enemy)
					if(this.pieceAlliance != pieceAlliance) 
					{
						legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // if piece at destination is enemy piece -> new Move
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public King movePiece(final Move move) 
	{ 
		return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate()); 
	}
	
	@Override
	public String toString()
	{ return PieceType.KING.toString(); }
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 1.column
	{
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
				candidateOffset == 7);
	}	
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) // covers case 8.column
	{
		return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == -7|| candidateOffset == 1 ||
				candidateOffset == 9);
	}

}