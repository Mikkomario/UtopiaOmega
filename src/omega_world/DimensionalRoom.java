package omega_world;

import genesis_graphic.DrawableHandler;
import genesis_logic.ActorHandler;
import genesis_logic.KeyListenerHandler;
import genesis_logic.MouseListenerHandler;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import arc_resource.GamePhase;
import omega_gameplay.Collidable;
import omega_gameplay.HelpMath;
import omega_graphic.SpriteBank;
import omega_graphic.TileMap;

/**
 * Dimensional room is a room that has a position and size
 *
 * @author Mikko Hilpinen.
 * @since 12.7.2013.
 * @warning Changing the position of the room doesn't work well if the room 
 * is active. Please end the room before moving it. Also remember to move 
 * the map, background and the objects in the room as well
 * @deprecated This class hasn't been tested well and is rather dated
 */
public class DimensionalRoom extends TiledRoom implements Collidable
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int width, height;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new dimensionalroom with the given size to the given position. 
	 * A new tilemap is also created into the room using the given information. 
	 * The room has the given background(s). The room is inctive until started
	 * @param gamePhase The gamePhase that is active while the room is active
	 * @param mouseHandler The mouseListenerHandler that informs the objects 
	 * about mouse events
	 * @param keyHandler The keyListenerHandler that informs the objects about 
	 * key events
	 * @param x The room's x-coordinate (in pixels)
	 * @param y The room's y-coordinate (in pixels)
	 * @param drawer The drawableHandler that will draw the contents of the room
	 * @param actorHandler the actorHandler that will inform the objects about 
	 * step events
	 * @param width The width of the room (in pixels)
	 * @param height The height of the room (in pixels)
	 * @param tileMap The tileMap that contains the tiles used in this room
	 * used in which tile
	 * find their spritename in a spritebank
	 * @param tileTextureBanks A list of spritebanks containing the textures used in tiles
	 * @param tileTextureNames A list of names of the tiletextures
	 * @see Room#start()
	 * @see Room#end()
	 */
	public DimensionalRoom(GamePhase gamePhase, MouseListenerHandler mouseHandler, 
			KeyListenerHandler keyHandler, int x, int y, DrawableHandler drawer, 
			ActorHandler actorHandler, int width, int height, TileMap tileMap, 
			ArrayList<SpriteBank> tileTextureBanks, ArrayList<String> tileTextureNames)
	{
		super(gamePhase, mouseHandler, actorHandler, drawer, keyHandler, 
				tileMap/*new TileMap(x, y, xtiles, ytiles, width / xtiles, 
				height / ytiles, bankindexes, rotations, xscales, yscales, 
				nameindexes, this)*/, tileTextureBanks, tileTextureNames);
		
		// Initializes attributes
		this.width = width;
		this.height = height;
		
		// Adds the object to the handler(s)
		getCollisionHandler().getCollidableHandler().addCollidable(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean pointCollides(Point2D testPoint)
	{
		// Room uses tilemap for collision checking
		if (getTiles() != null)
			return getTiles().pointCollides(testPoint);
		else
			return false;
	}

	@Override
	public boolean isSolid()
	{
		// Uses tilemap for collision checking
		if (getTiles() != null)
			return getTiles().isSolid();
		else
			return false;
	}

	@Override
	public void makeSolid()
	{
		// Uses tilemap for collision checking
		if (getTiles() != null)
			getTiles().makeSolid();
	}

	@Override
	public void makeUnsolid()
	{
		// Uses tilemap for collision checking
		if (getTiles() != null)
			getTiles().makeUnsolid();
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		if (getTiles() != null)
			return getTiles().getSupportedListenerClasses();
		else
			return null;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The width of the room (in pixels)
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 * @return The height of the room (in pixels)
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 * @return The room's top-left x-coordinate
	 */
	public double getX()
	{
		return getTiles().getX();
	}
	
	/**
	 * @return The room's top-left y-coordinate
	 */
	public double getY()
	{
		return getTiles().getY();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Transforms coordinates relative to room's position into absolute coordinates.
	 *
	 * @param x The x-coordinate from the top left corner of the room (in pixels)
	 * @param y The y-coordinate from the top left corner of the room (in pixels)
	 * @return The absolute position
	 */
	public Point2D.Double getTransformedPosition(int x, int y)
	{
		return new Point2D.Double(getX() + x, getY() + y);
	}
	
	/**
	 * Transforms the absolute coordinates into coordinates relative to the room's position
	 *
	 * @param x The absolute x-coordinate to be transformed
	 * @param y The absolute y-coordinate to be transformed
	 * @return A point relative to the room's coordinates
	 */
	public Point2D.Double getNegatedPosition(int x, int y)
	{
		return new Point2D.Double(x - getX(), y - getY());
	}
	
	/**
	 * Checks whether an absolute position is within room borders
	 *
	 * @param absp The absolute point to be tested
	 * @return Is the point within the room borders
	 */
	public boolean absolutePointIsInRoom(Point2D.Double absp)
	{
		return HelpMath.pointIsInRange(absp, (int) getX(), 
				(int) getX() + getWidth(), (int) getY(), 
				(int) getY() + getHeight());
	}
	
	/**
	 * Checks whether an relative position is within room borders
	 *
	 * @param relp The relative point to be tested
	 * @return Is the point within the room borders
	 */
	public boolean relativePointIsInRoom(Point2D.Double relp)
	{
		return HelpMath.pointIsInRange(relp, 0, getWidth(), 0, getHeight());
	}
}
