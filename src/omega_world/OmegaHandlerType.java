package omega_world;

import genesis_event.HandlerType;

/**
 * The different types of Handlers introduced in the omega module
 * 
 * @author Mikko Hilpinen
 * @since 2.12.2014
 */
public enum OmegaHandlerType implements HandlerType
{
	/**
	 * Areas contain GameObjects and usually connect different handlers as well
	 */
	AREA, 
	/**
	 * AreaListenerHandlers inform areaListeners
	 */
	AREALISTENERHANDLER;
	
	
	// IMPLEMENTED METHODS	-------------------------

	@Override
	public Class<?> getSupportedHandledClass()
	{
		switch (this)
		{
			case AREA: return GameObject.class;
			case AREALISTENERHANDLER: return AreaListener.class;
		}
		
		return null;
	}
}
