package omega_graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import arc_bank.BankObject;


/**
 * This object represents a drawn image that can be animated. Sprites are
 * meant to be used in multiple objects and those objects should handle the
 * animation (this class merely loads and provides all the neccessary images)
 *
 * @author Mikko Hilpinen.
 *         Created 27.11.2012.
 */
public class Sprite implements BankObject
{	
	// ATTRIBUTES	-------------------------------------------------------
	
	private BufferedImage[] images;
	
	private int origX, origY, forcedWidth, forcedHeight;
	private boolean dead, dimensionsSpecified;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * This method creates a new sprite based on the information provided by 
	 * the caller. The images are loaded from a strip that contains one or more 
	 * images.
	 *
	 * @param filename The location of the loaded image (data/ is added 
	 * automatically to the beginning)
	 * @param numberOfImages How many separate images does the strip contain?
	 * @param originX the x-coordinate of the sprite's origin (Pxl)
	 * @param originY the y-coordinate of the sprite's origin (Pxl)
	 */
	public Sprite(String filename, int numberOfImages, int originX, int originY)
	{
		// Checks the variables
		if (filename == null || numberOfImages <= 0)
			throw new IllegalArgumentException();
		
		//System.out.println("loads sprite " + filename);
		// TODO: Add downscaling and upscaling?
		
		// Initializes attributes
		this.origX = originX;
		this.origY = originY;
		this.dead = false;
		this.forcedHeight = 0;
		this.forcedWidth = 0;
		this.dimensionsSpecified = false;
		
		// Loads the image
		File img = new File("data/" + filename);
		BufferedImage strip = null;
		
		try
		{
			strip = ImageIO.read(img);
		}
		catch (IOException ioe)
		{
			System.err.println(this + " failed to load the image data/" + 
					filename);
			ioe.printStackTrace();
			return;
		}
		
		// Creates the subimages
		this.images = new BufferedImage[numberOfImages];
		
		// Calculates the subimage width
		int sw = strip.getWidth() / numberOfImages;
		
		for (int i = 0; i < numberOfImages; i++)
		{
			// Calculates the needed variables
			int sx;
			sx = i*sw;
			
			this.images[i] = strip.getSubimage(sx, 0, sw, strip.getHeight());
		}
		
		// If an origin position was set to -1, sets it to the middle of the 
		// sprite
		if (this.origX == -1)
			this.origX = getWidth() / 2;
		if (this.origY == -1)
			this.origY = getHeight() / 2;
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------
	
	@Override
	public void kill()
	{
		// Doesn't do much since the image information will be released 
		// automatically once the sprite is not held anywhere anymore
		this.dead = true;
	}
	
	@Override
	public boolean isDead()
	{
		return this.dead;
	}
	
	
	// GETTERS & SETTERS	------------------------------------------------
	
	/**
	 * @return returns how many subimages exist within this sprite
	 */
	public int getImageNumber()
	{
		return this.images.length;
	}
	
	/**
	 * @return The x-coordinate of the origin of the sprite (relative pixel)
	 */
	public int getOriginX()
	{
		return (int) (this.origX * getXScale());
	}
	
	/**
	 * @return The y-coordinate of the origin of the sprite (relative pixel)
	 */
	public int getOriginY()
	{
		return (int) (this.origY * getYScale());
	}
	
	/**
	 * @return How wide a single subimage is (pixels)
	 */
	public int getWidth()
	{
		if (this.dimensionsSpecified)
			return this.forcedWidth;
		return getImageWidth();
	}
	
	/**
	 * @return How tall a single subimage is (pixels)
	 */
	public int getHeight()
	{
		if (this.dimensionsSpecified)
			return this.forcedHeight;
		return getImageHeight();
	}
	
	/**
	 * @return How much the width of the sprite should be scaled upon drawing
	 */
	public double getXScale()
	{
		if (this.dimensionsSpecified)
			return ((double) getWidth()) / getImageWidth();
		else
			return 1;
	}
	
	/**
	 * @return How much the height of the sprite should be scaled upon drawing
	 */
	public double getYScale()
	{
		if (this.dimensionsSpecified)
			return ((double) getHeight()) / getImageHeight();
		return 1;
	}
	
	
	// METHODS	------------------------------------------------------------
	
	/**
	 * This method returns a single subimage from the sprite.
	 *
	 * @param imageIndex The index of the image to be drawn [0, numberOfImages[
	 * @return The subimage from the given index
	 * @see #getImageNumber()
	 */
	public BufferedImage getSubImage(int imageIndex)
	{
		// Checks the given index and adjusts it if needed
		if (imageIndex < 0 || imageIndex >= this.images.length)
			imageIndex = Math.abs(imageIndex % this.images.length);
		
		return this.images[imageIndex];
	}
	
	/**
	 * Sets the sprite to have the given size.
	 * 
	 * @param width How wide the sprite will be (in pixels)
	 * @param height How high the sprite will be (in pixels)
	 * @notice This size is used when the sprite is drawn using a SpriteDrawer, 
	 * if you draw the sprite using another class, you must use the getXScale() 
	 * and getYScale() -methods.
	 */
	public void forceDimensions(int width, int height)
	{
		this.forcedWidth = width;
		this.forcedHeight = height;
		this.dimensionsSpecified = true;
	}
	
	/**
	 * Permanently scales the sprite with the given modifiers
	 * 
	 * @param xScale How much the width of the sprite is scaled (1 keeps the width the same)
	 * @param yScale How much the height of the sprite is scaled (1 keeps the height the same)
	 */
	public void scale(double xScale, double yScale)
	{
		// If there is not yet any forced scaling, initializes it
		forceDimensions((int) (getWidth() * xScale), (int) (getHeight() * yScale));
	}
	
	private int getImageWidth()
	{
		return getSubImage(0).getWidth();
	}
	
	private int getImageHeight()
	{
		return getSubImage(0).getHeight();
	}
	
	// TODO: If you get bored, try to implement filters into the project
	// check: http://docs.oracle.com/javase/tutorial/2d/images/drawimage.html
}
