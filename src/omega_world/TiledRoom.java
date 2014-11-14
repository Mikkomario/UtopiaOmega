package omega_world;

import genesis_graphic.DrawableHandler;
import genesis_logic.ActorHandler;
import genesis_logic.KeyListenerHandler;
import genesis_logic.MouseListenerHandler;

import java.util.ArrayList;

import arc_resource.GamePhase;
import omega_graphic.SpriteBank;
import omega_graphic.TileMap;

/**
 * TiledRoom is a room that contains a tilemap in addition to just objects and 
 * backgrounds.
 *
 * @author Mikko Hilpinen.
 * @since 11.7.2013.
 */
public class TiledRoom extends Area
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<SpriteBank> texturebanks;
	private ArrayList<String> texturenames;
	private TileMap tilemap;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new room, filled with backgrounds, tiles and objects. 
	 * The room will remain inactive until started.
	 * 
	 * @param gamePhase The gamePhase that is active while this room is active
	 * @param mouseHandler The mouseHandler that will inform the room's objects 
	 * about mouse events
	 * @param actorHandler The actorHandler that will inform the room's objects 
	 * about step events 
	 * @param drawer The drawer the will draw the objects in the room
	 * @param keyHandler The keyHandler that will inform the objects about 
	 * key events
	 * @param tilemap The tilemap used in the room (null if no tiles are used)
	 * @param tiletexturebanks A list of spritebanks that contained the textures 
	 * used in the tilemap
	 * @param tiletexturenames A list of the names of the textures used in the 
	 * tilemap
	 * @see Room#end()
	 * @see Room#start()
	 */
	public TiledRoom(GamePhase gamePhase, MouseListenerHandler mouseHandler, 
			ActorHandler actorHandler, DrawableHandler drawer, 
			KeyListenerHandler keyHandler,
			TileMap tilemap, ArrayList<SpriteBank> tiletexturebanks, 
			ArrayList<String> tiletexturenames)
	{
		super(gamePhase, mouseHandler, actorHandler, drawer, keyHandler);
		
		// Initializes attributes
		this.tilemap = tilemap;
		this.texturebanks = tiletexturebanks;
		this.texturenames = tiletexturenames;
		
		// Uninitializes the tilemap
		uninitialize();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void killWithoutKillingHandleds()
	{
		// In addition to the normal killing process, kills the tilemap as well
		if (this.tilemap != null)
		{
			this.tilemap.kill();
			this.tilemap = null;
		}
		
		super.killWithoutKillingHandleds();
	}
	
	@Override
	protected void initialize()
	{
		super.initialize();
		// In addition to normal initialization, initializes the tilemap
		if (this.tilemap != null)
			this.tilemap.initialize(this.texturebanks, this.texturenames);
	}
	
	@Override
	protected void uninitialize()
	{
		// In addition to the normal uninitialization, also clears the tilemap
		super.uninitialize();
		// Clears the tilemap
		if (this.tilemap != null)
			this.tilemap.clear();
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The tilemap used in the room
	 */
	public TileMap getTiles()
	{
		return this.tilemap;
	}
	
	/**
	 * Changes the room's tilemap
	 *
	 * @param tiles The new tilemap used in the room (null if no tiles are used)
	 */
	public void setTiles(TileMap tiles)
	{
		this.tilemap = tiles;
	}
	
	/**
	 * @return A list containing all the spritebanks used in the tilemap
	 */
	protected ArrayList<SpriteBank> getTileTextureBanks()
	{
		return this.texturebanks;
	}
	
	/**
	 * @return A list containing all the spritenames used in the tilemap
	 */
	protected ArrayList<String> getTileTextureNames()
	{
		return this.texturenames;
	}
}
