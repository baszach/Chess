package com.chess.engine.board;

import java.util.Collections;
import com.chess.engine.pieces.Piece;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ImmutableList;

public abstract class Tile // Class for Chess Board (fields/tiles on board)
{
	protected final int tileCoordinate; // Coordinate of single Tile	
	/*protected -> ONLY visible to package and all sub-classes  //	final -> tileCoordinate can ONLY be set ONCE when object is constructed*/
	
	private Tile (final int tileCoordinate) // Constructor for Tile Class
	{
		this.tileCoordinate = tileCoordinate;
	}
	
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles(); //TODO: Google "Map" in Java, to understand that line of code!
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() 
	{
		final Map<Integer, Tile.EmptyTile> emptyTileMap = new HashMap<>();
		
		for (int i = 0; i < BoardUtils.NUM_TILES; i++)
		{
			emptyTileMap.put(i, new EmptyTile(i));
		}
		return Collections.unmodifiableMap(emptyTileMap); 
		// TODO: Check out Collection.unmodifiableMap();
		// TODO: ImmutableMap.copyOf(emtyTileMap) instead, but need new library (check Guava) -> check Vid#2 for link!
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece)
	{
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	
	public abstract boolean isTileOccupied(); // abstract Method for subclass/ other classes
	
	public abstract Piece getPiece(); // abstract Method for subclass/ other classes
	
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	public static final class EmptyTile extends Tile // Class for empty Tile on Chess Board (extends Class for Chess Board)
	{
		private EmptyTile (final int coordinate)  
		{
			super(coordinate); // uses constructor from Superclass
		}
		
		@Override
		public String toString()
		{ return "-"; }
		
		@Override // overridden method from superclass
		public boolean isTileOccupied() 
		{ return false; }
		
		@Override // overridden method from superclass
		public Piece getPiece()
		{ return null; }
	}
	
	
	public static final class OccupiedTile extends Tile // Class for occupied Tile on Chess Board (extends Class for Chess Board)
	{
		private final Piece pieceOnTile;
		
		private OccupiedTile (final int tileCoordinate,final Piece pieceOnTile)
		{
			super(tileCoordinate); // constructor from Superclass
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public String toString()
		{ return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : 
			getPiece().toString(); }
		
		@Override // overridden method from superclass
		public boolean isTileOccupied()
		{ return true; }
		
		@Override // overridden method from superclass
		public Piece getPiece()
		{ return this.pieceOnTile; }
	}
}