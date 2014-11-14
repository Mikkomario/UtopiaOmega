package omega_world;

import java.util.ArrayList;

import omega_graphic.Background;
import omega_graphic.OpenSpriteBank;
import omega_graphic.Tile;

/**
 * AreaObject creators are set into areas. They create a set of objects when 
 * the area starts. AreaObjectCreators also handle the background of an area 
 * if it's necessary
 * 
 * @author Mikko Hilpinen
 * @since 9.3.2014
 */
public abstract class AreaObjectCreator extends GameObject implements RoomListener
{
	// ATTRIBUTES	----------------------------------------------------
	
	private Area area;
	private String areaBackgroundName, areaBackgroundBankName;
	private int areaWidth, areaHeight;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new areaObjectCreator to the given area. The creator will 
	 * create objects when the area starts.
	 * 
	 * @param area The area the creator will reside at
	 * @param backgroundName The name of the area's background image in a 
	 * spriteBank (null if no background)
	 * @param backgroundBankName The name of the spriteBank that holds the 
	 * background used in the area (null if no background)
	 * @param areaWidth How wide the area is (optional, used for background scaling)
	 * @param areaHeight How high the area is (optional, used for background scaling)
	 */
	public AreaObjectCreator(Area area, String backgroundName, 
			String backgroundBankName, int areaWidth, int areaHeight)
	{
		super(area);
		
		// Initializes attributes
		this.area = area;
		this.areaBackgroundBankName = backgroundBankName;
		this.areaBackgroundName = backgroundName;
		this.areaWidth = areaWidth;
		this.areaHeight = areaHeight;
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Here the creator will create objects it's supposed to and adds them 
	 * to the area (if necessary)
	 * 
	 * @param area The area where the objects will be created to.
	 */
	protected abstract void createObjects(Area area);
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects
		createObjects(this.area);
		
		// Changes the room's background
		if (this.areaBackgroundBankName != null && 
				this.areaBackgroundName != null && this.areaWidth != 0 && 
				this.areaHeight != 0)
		{
			ArrayList<Background> backs = new ArrayList<Background>();
			Tile back = new Tile(this.areaWidth / 2, this.areaHeight / 2, 
					OpenSpriteBank.getSprite(this.areaBackgroundBankName, 
					this.areaBackgroundName), this.areaWidth, this.areaHeight, this.area);
			backs.add(back);
			this.area.setBackgrounds(backs, false);
		}
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Removes the background (if any)
		if (this.areaBackgroundBankName != null && this.areaBackgroundName != null)
			this.area.setBackgrounds(null, true);
	}
}
