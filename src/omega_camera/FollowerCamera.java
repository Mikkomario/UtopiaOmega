package omega_camera;

import omega_gameplay.BasicPhysicDrawnObject;
import omega_world.AreaOldVersion;

/**
 * A camera, which follows the given object around. The camera's position is 
 * fixed to the followed object's position
 * 
 * @author Unto Solala & Mikko Hilpinen 
 * @since 18.6.2013
 */
public class FollowerCamera extends BasicCamera
{
	// ATTRIBUTES ----------------------------------------------------

	private BasicPhysicDrawnObject followed;

	
	// CONSTRUCTOR ---------------------------------------------------

	/**
	 * Creates a new follower camera added to the given handlers and starting 
	 * from the <b>followed</b> object's coordinates
	 * 
	 * @param screenWidth The width of the screen
	 * @param screenHeight The height of the screen
	 * @param depthLayers How many layers of depth handling there should be. 
	 * The less the content's depth changes, the more there should be. [1, 6]
	 * @param followed The followed PhysicDrawnObject
	 * @param area The area where the object will reside
	 */
	public FollowerCamera(int screenWidth, int screenHeight, int depthLayers, 
			BasicPhysicDrawnObject followed, AreaOldVersion area)
	{
		super((int) (followed.getX()), (int) (followed.getY()), 
				screenWidth, screenHeight, depthLayers, area);

		// Initializes attributes
		this.followed = followed;
	}

	
	// IMPLEMENTED METHODS --------------------------------------------

	@Override
	public void act(double steps)
	{
		// In addition to normal acting, the camera follows the object
		super.act(steps);

		if (this.followed == null)
			return;

		// Follows the object
		setPosition(this.followed.getX(), this.followed.getY());
	}
	
	
	// GETTERS & SETTERS	-------------------------------------------
	
	/**
	 * @return The object that the camera follows
	 */
	public BasicPhysicDrawnObject getFollowedObject()
	{
		return this.followed;
	}
	
	/**
	 * Changes the object the camera follows
	 *
	 * @param d The new object the camera will follow
	 */
	public void setFollowedObject(BasicPhysicDrawnObject d)
	{
		this.followed = d;
	}
}
