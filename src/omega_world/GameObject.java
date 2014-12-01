package omega_world;

import genesis_event.Handled;
import genesis_event.HandlerRelay;
import genesis_util.LatchStateOperator;
import genesis_util.StateOperator;

/**
 * GameObject represents any game entity. All of the gameobjects can be created, 
 * handled and killed. Pretty much any visible or invisible object in a game 
 * that can become an 'object' of an action should inherit this class.
 *
 * @author Mikko Hilpinen.
 * @since 11.7.2013.
 */
public class GameObject implements Handled
{
	// TODO: Use a separate interface instead?
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private StateOperator isDeadOperator, isActiveOperator;
	private GameObject master;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new gameobject that is alive until it is killed
	 * @param handlers The handlers that handle the object
	 */
	public GameObject(HandlerRelay handlers)
	{
		// Initializes attributes
		this.isDeadOperator = new LatchStateOperator(false);
		this.isActiveOperator = new StateOperator(true, true);
		this.master = null;
		
		// Adds the object to the handler(s)
		if (handlers != null)
			handlers.addHandled(this);
	}
	
	/**
	 * Creates a new GameObject that will become dependent of the given object
	 * @param handlers The handlers that handle this gameObject
	 * @param master The object this gameObject depends on. If the master dies, this object 
	 * will follow.
	 */
	public GameObject(HandlerRelay handlers, GameObject master)
	{
		this.isDeadOperator = null;
		this.isActiveOperator = new StateOperator(true, true);
		this.master = master;
		
		// Adds the object to the handler(s)
		if (handlers != null)
			handlers.addHandled(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public StateOperator getIsDeadStateOperator()
	{
		if (this.master != null)
			return this.master.getIsDeadStateOperator();
		
		return this.isDeadOperator;
	}
	
	
	// GETTERS & SETTERS	----------------------
	
	/**
	 * @return The object this GameObject depends on
	 */
	protected GameObject getMasterObject()
	{
		return this.master;
	}
	
	/**
	 * @return This stateOperator tells whether the object is active (= is in an active Area 
	 * / other context) or not. The subclasses should take notice of this operator's states.
	 */
	public StateOperator getIsActiveStateOperator()
	{
		return this.isActiveOperator;
	}
}
