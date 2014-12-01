package omega_world;

import genesis_graphic.GamePanel;
import genesis_graphic.GameWindow;

import java.util.HashMap;

import arc_resource.GamePhase;

/**
 * AreaRelay keeps track of multiple areas and offers them for other objects.
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public class AreaRelay
{
	// ATTRIBUTES	------------------------------------------------------
	
	private HashMap<String, AreaOldVersion> areas;
	private GameWindow window;
	private GamePanel panel;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates an empty areaRelay. The areas need to be added separately. 
	 * If you are using the relay in a single GameWindow and GamePanel, it is 
	 * recommended to use the alternative constructor.
	 * @see #addArea(String, AreaOldVersion)
	 * @see #AreaRelay(GameWindow, GamePanel)
	 */
	public AreaRelay()
	{
		// Initializes attributes
		this.areas = new HashMap<String, AreaOldVersion>();
		this.window = null;
		this.panel = null;
	}
	
	/**
	 * Creates an empty areaRelay that is tied to the given GameWindow and 
	 * GamePanel. Using this constructor allows easier addition of new areas 
	 * since they are automatically added to the given window and panel. 
	 * The new areas need to be created separately.<br>
	 * If you don't want to tie all the areas into a single window + panel, 
	 * please use the alternative constructor (or simply alternate between the 
	 * addArea methods).
	 * 
	 * @param window The GameWindow where all the areas in the relay will be 
	 * shown.
	 * @param panel The GamePanel in which all the areas will be drawn.
	 * @see #AreaRelay()
	 */
	public AreaRelay(GameWindow window, GamePanel panel)
	{
		this.areas = new HashMap<String, AreaOldVersion>();
		this.window = window;
		this.panel = panel;
	}

	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Returns an area with the given name
	 * @param areaName The name of the area to be returned
	 * @return An area with the given name or null if no such area exists.
	 */
	public AreaOldVersion getArea(String areaName)
	{
		if (!this.areas.containsKey(areaName))
		{
			System.err.println("There is no area named " + areaName);
			return null;
		}
		
		return this.areas.get(areaName);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new area to the collection
	 * 
	 * @param areaName The name of the new area
	 * @param area The new area to be added
	 */
	public void addArea(String areaName, AreaOldVersion area)
	{
		if (area == null)
			return;
		
		this.areas.put(areaName, area);
	}
	
	/**
	 * Creates and adds a new area to the collection. Only works if the 
	 * GameWindow and GamePanel were provided in the constructor. The created 
	 * areas will be used in those displays.
	 * 
	 * @param areaName The name of the new area.
	 * @param areaPhase The gamePhase the area is tied to.
	 * @see #AreaRelay(GameWindow, GamePanel)
	 */
	public void addArea(String areaName, GamePhase areaPhase)
	{
		// Checks if the window and panel have been provided
		if (this.window == null || this.panel == null)
		{
			System.err.println("AreaRelay#addArea(String, GamePhase) cannot "
					+ "create a new area without the information about the "
					+ "GameWindow and GamePanel used. Please provide them in "
					+ "the constructor or use addArea(String, Area)");
			return;
		}
		
		addArea(areaName, new AreaOldVersion(areaPhase, 
				this.window.getMouseListenerHandler(), 
				this.window.getStepHandler(), this.panel.getDrawer(), 
				this.window.getKeyListenerHandler()));
	}
	
	/**
	 * Ends functions in all the areas. This might be useful to do between 
	 * area transitions
	 */
	public void endAllAreas()
	{
		for (AreaOldVersion area : this.areas.values())
		{
			area.end();
		}
	}
}
