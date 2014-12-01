package omega_world;

import java.util.ArrayList;

import arc_resource.GamePhase;
import arc_resource.ResourceActivator;
import omega_gameplay.CollisionHandler;
import omega_graphic.Background;

/**
 * Areas are certain states or places in the program. Areas contain their 
 * own set of basic handlers they offer for their objects. Areas can be started 
 * or ended like any room.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class AreaOldVersion extends Room
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private MouseListenerHandler mousehandler;
	private KeyListenerHandler keyHandler;
	private ActorHandler actorhandler;
	private DrawableHandler drawer;
	private CollisionHandler collisionhandler;
	private GamePhase phase;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new area with separate handler systems. The area will remain 
	 * inactive until started.
	 * 
	 * @param phase The GamePhase during which the area is active
	 * @param superMouseHandler The MouseListenerHandler that the area's handlers will use
	 * @param superActorHandler The ActorHandler that the area's handlers will use
	 * @param superDrawer The DrawableHandler that the area's handlers will use
	 * @param superKeyHandler The keyListenerHandler that the area's keyHandler will use
	 */
	public AreaOldVersion(GamePhase phase, MouseListenerHandler superMouseHandler, 
			ActorHandler superActorHandler, DrawableHandler superDrawer, 
			KeyListenerHandler superKeyHandler)
	{
		super(new ArrayList<Background>());
		
		// Initializes attributes
		this.phase = phase;
		this.mousehandler = new MouseListenerHandler(false, superActorHandler, 
				superMouseHandler);
		this.actorhandler = new ActorHandler(false, superActorHandler);
		this.drawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 5, 
				superDrawer);
		this.collisionhandler = new CollisionHandler(false, superActorHandler);
		this.keyHandler = new KeyListenerHandler(false, superKeyHandler);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void kill()
	{
		// Also kills the handlers
		this.mousehandler.kill();
		this.actorhandler.kill();
		this.drawer.kill();
		this.collisionhandler.kill();
		this.keyHandler.kill();
		
		super.kill();
	}
	
	@Override
	protected void initialize()
	{
		// Area makes sure the correct phase is active before initialization
		ResourceActivator.startPhase(this.phase);
		
		super.initialize();
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The mouseListenerHandler used in the area
	 */
	public MouseListenerHandler getMouseHandler()
	{
		return this.mousehandler;
	}
	
	/**
	 * @return The actorHandler used in the area
	 */
	public ActorHandler getActorHandler()
	{
		return this.actorhandler;
	}
	
	/**
	 * @return The drawableHandler used in the area
	 */
	public DrawableHandler getDrawer()
	{
		return this.drawer;
	}
	
	/**
	 * @return The collisionHandler used in the area
	 */
	public CollisionHandler getCollisionHandler()
	{
		return this.collisionhandler;
	}
	
	/**
	 * @return The keyListenerHandler used in the area
	 */
	public KeyListenerHandler getKeyHandler()
	{
		return this.keyHandler;
	}
}
