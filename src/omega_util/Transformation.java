package omega_util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.HashMap;
import java.util.Map;

import genesis_util.HelpMath;
import genesis_util.Line;
import genesis_util.Vector3D;

/**
 * Transformation represents an object's state. Transformation can affect many things, 
 * including physics simulation, drawing and collision checking. Transformations are immutable 
 * once created.
 *  
 * @author Mikko Hilpinen
 * @since 3.12.2014
 */
public final class Transformation
{
	// ATTRIBUTES	-----------------------------------
	
	private Vector3D position, scaling, shear;
	private double angle;
	
	
	// CONSTRUCTOR	-----------------------------------
	
	/**
	 * Creates a new default transformation
	 */
	public Transformation()
	{
		// Initializes attributes
		initialize();
	}

	/**
	 * Creates a new transformation with the given preset position
	 * @param position The position attribute of the transformation
	 */
	public Transformation(Vector3D position)
	{
		// Initializes attributes
		initialize();
		this.position = position;
	}
	
	/**
	 * Creates a new transformation with the given attributes
	 * @param position The position attribute of the transformation
	 * @param scaling The scaling attribute of the transformation
	 * @param shear The shearing attribute of the transformation
	 * @param angle The angle attribute of the transformation
	 */
	public Transformation(Vector3D position, Vector3D scaling, Vector3D shear, double angle)
	{
		this.position = position;
		this.scaling = scaling;
		this.shear = shear;
		this.angle = HelpMath.checkDirection(angle);
	}
	
	/**
	 * Creates a new transformation by copying another transformation
	 * @param other The transformation that will be copied
	 */
	public Transformation(Transformation other)
	{
		this.position = other.position;
		this.scaling = other.scaling;
		this.shear = other.shear;
		this.angle = other.angle;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------
	
	@Override
	public Transformation clone()
	{
		return new Transformation(this);
	}
	
	
	// GETTERS & SETTERS	---------------------------
	
	/**
	 * @return The position attribute of this transformation (default at (0,0))
	 */
	public Vector3D getPosition()
	{
		return this.position;
	}
	
	/**
	 * @return The scaling attribute of this transformation (default at (1,1))
	 */
	public Vector3D getScaling()
	{
		return this.scaling;
	}
	
	/**
	 * @return The shearing attribute of this transformation (default at (0,0))
	 */
	public Vector3D getShear()
	{
		return this.shear;
	}
	
	/**
	 * @return The angle attribute of this transformation (default at 0) [0, 360[
	 */
	public double getAngle()
	{
		return this.angle;
	}
	
	
	// OTHER METHODS	-------------------------------
	
	/**
	 * Adds this transformation to the given graphics drawer.
	 * @param g2d The object that does the drawing
	 */
	public void transform(Graphics2D g2d)
	{
		g2d.transform(this.toAffineTransform());
	}
	
	/**
	 * Transforms a position in the transformation's relative coordinate system into a position 
	 * in the absolute world coordinate system.
	 * @param relativeCoordinates The position in the object space (relative)
	 * @return The position in the world space (absolute)
	 */
	public Vector3D transform(Vector3D relativeCoordinates)
	{
		return new Vector3D(this.toAffineTransform().transform(relativeCoordinates.toPoint(), null));
	}
	
	/**
	 * Transforms a line in the transformation's relative coordinate system into a line in 
	 * the absolute world coordinate system.
	 * @param relativeLine The line in the object space (relative)
	 * @return The line in the world space (absolute)
	 */
	public Line transform(Line relativeLine)
	{
		return new Line(transform(relativeLine.getStart()), transform(relativeLine.getEnd()));
	}
	
	/**
	 * Transforms a position in the absolute world coordinate system into a position in the 
	 * transformation's relative coordinate system.
	 * @param absoluteCoordinates The position in the world space (absolute)
	 * @return The position in this transformation's space (relative)
	 */
	public Vector3D inverseTransform(Vector3D absoluteCoordinates)
	{
		AffineTransform transform = this.toAffineTransform();
		AffineTransform inverse = null;
		
		try
		{	
			if (transform.getDeterminant() != 0)
			{
				inverse = new AffineTransform(transform);
				inverse.invert();
			}
			else
			{
				// In the case the current transformation can't be inverted, 
				// inverts the translations (seems to be enough)
				inverse = new AffineTransform();
				inverse.translate(-getPosition().getFirst(), -getPosition().getSecond());
			}
		}
		catch (NoninvertibleTransformException exception)
		{
			System.err.println("Failed to inverse transform a position");
			exception.printStackTrace();
		}
		
		return new Vector3D(inverse.transform(absoluteCoordinates.toPoint(), null));
	}
	
	/**
	 * Transforms a transformation, making it use this transformation's coordinate system.
	 * @param other The transformation that will be transfrormed
	 * @return a transformed transformation
	 */
	public Transformation transform(Transformation other)
	{
		Vector3D transformedPosition = transform(other.getPosition());
		return this.plus(other).withPosition(transformedPosition);
	}
	
	/**
	 * Checks if the two transformations are similar to each other
	 * @param other The transformation this one is compared to
	 * @return Are the two transformations identical
	 */
	public boolean equals(Transformation other)
	{
		return getPosition().equals(other.getPosition()) && 
				getScaling().equals(other.getScaling()) && 
				getShear().equals(other.getShear()) && 
				HelpMath.areApproximatelyEqual(getAngle(), other.getAngle());
	}
	
	/**
	 * @return A transformation that is opposite to this transformation. If the two 
	 * transformations are combined, an identity transformation is created.
	 */
	public Transformation inverse()
	{
		// TODO: Test
		return new Transformation(getPosition().reverse(), 
				Vector3D.identityVector().dividedBy(getScaling()), getShear().reverse(), 
				HelpMath.checkDirection(-getAngle()));
	}
	
	/**
	 * @param other The other transformation
	 * @return A combination of these transformations
	 */
	public Transformation plus(Transformation other)
	{
		return new Transformation(getPosition().plus(other.getPosition()), 
				getScaling().times(other.getScaling()), getShear().plus(other.getShear()), 
				HelpMath.checkDirection(getAngle() + other.getAngle()));
	}
	
	/**
	 * @param other The other transformation
	 * @return The difference between these two transformations
	 */
	public Transformation minus(Transformation other)
	{
		return this.plus(other.inverse());
	}
	
	/**
	 * @param position The new position attribute
	 * @return A transformation like this one except with the given position
	 */
	public Transformation withPosition(Vector3D position)
	{
		Transformation t = this.clone();
		t.position = position;
		return t;
	}
	
	/**
	 * @param scaling The new scaling attribute
	 * @return A transformation like this one except with the given scaling
	 */
	public Transformation withScaling(Vector3D scaling)
	{
		Transformation t = this.clone();
		t.scaling = scaling;
		return t;
	}
	
	/**
	 * @param shear The new shear attribute
	 * @return A transformation like this one except with the given shear
	 */
	public Transformation withShear(Vector3D shear)
	{
		Transformation t = this.clone();
		t.shear = shear;
		return t;
	}
	
	/**
	 * @param angle The new angle attribute
	 * @return A transformation like this one except with the given angle
	 */
	public Transformation withAngle(double angle)
	{
		Transformation t = this.clone();
		t.angle = HelpMath.checkDirection(angle);
		return t;
	}
	
	/**
	 * Creates a copy of this transformation with the given attribute value. This is supposed 
	 * to be used in constructable objects that want to construct their transformations (hence 
	 * the string format).
	 * @param attributeName The name of the changed attribute [position, scaling, shear, angle]
	 * @param attributeValue The value given to the attribute
	 * @return A transformation similar to this but with the given attribute
	 */
	public Transformation withAttribute(String attributeName, String attributeValue)
	{
		switch (attributeName)
		{
			case "position": return this.withPosition(Vector3D.parseFromString(attributeValue));
			case "scaling": return this.withScaling(Vector3D.parseFromString(attributeValue));
			case "shear": return this.withShear(Vector3D.parseFromString(attributeValue));
			case "angle": return this.withAngle(Double.parseDouble(attributeValue));
		}
		
		return this;
	}
	
	/**
	 * @return The attributes of this transformation in string format. This method is supposed 
	 * to be used in writable objects so they can write their transformations more easily.
	 */
	public Map<String, String> getAttributes()
	{
		Map<String, String> attributes = new HashMap<>();
		
		attributes.put("position", getPosition().toString());
		attributes.put("scaling", getScaling().toString());
		attributes.put("shear", getShear().toString());
		attributes.put("angle", getAngle() + "");
		
		return attributes;
	}
	
	/**
	 * Creates a new transformation with position and angle as if it was rotated around a 
	 * position in the world space.
	 * @param rotation How much the transformation is rotated (in degrees)
	 * @param origin The origin point of the rotation (absolute)
	 * @return A new transformation that has been rotated around the given position
	 */
	public Transformation rotatedAroundAbsolutePoint(double rotation, Vector3D origin)
	{
		return this.withPosition(HelpMath.getRotatedPosition(origin, getPosition(), 
				rotation)).plus(rotationTransformation(rotation));
	}
	
	/**
	 * Creates a new transformation with position and angle as if it was rotated around 
	 * relative position coordinates.
	 * @param rotation How much the transformation is rotated (in degrees)
	 * @param origin The origin point of the rotation (relative)
	 * @return A new transformation that has been rotated around the given position
	 */
	public Transformation rotatedAroundRelativePoint(double rotation, Vector3D origin)
	{
		return this.rotatedAroundAbsolutePoint(rotation, this.transform(origin));
	}
	
	/**
	 * @return This transformation turned into an affinetransformation
	 */
	public AffineTransform toAffineTransform()
	{
		AffineTransform t = new AffineTransform();
		
		t.translate(getPosition().getFirst(), getPosition().getSecond());
		t.rotate(Math.toRadians((360 - getAngle())));
		t.scale(getScaling().getFirst(), getScaling().getSecond());
		t.shear(getShear().getFirst(), getShear().getSecond());
		
		return t;
	}
	
	private void initialize()
	{
		this.position = Vector3D.zeroVector();
		this.scaling = Vector3D.identityVector();
		this.shear = Vector3D.zeroVector();
		this.angle = 0;
	}
	
	
	// FACTORIES	---------------------------
	
	/**
	 * @return A transformation that can be added to any other transformation and keep it same
	 */
	public static Transformation identityTransformation()
	{
		return new Transformation();
	}
	
	/**
	 * @param transition The position attribute of the transformation
	 * @return A transformation that only affects the position attribute when combined 
	 * with another transformation
	 */
	public static Transformation transitionTransformation(Vector3D transition)
	{
		return new Transformation(transition);
	}
	
	/**
	 * @param scaling The scaling attribute of the transformation
	 * @return A transformation that only affects the scaling attribute when combined 
	 * with another transformation
	 */
	public static Transformation scalingTransformation(Vector3D scaling)
	{
		return identityTransformation().withScaling(scaling);
	}
	
	/**
	 * @param scaling The scaling attribute of the transformation
	 * @return A transformation that only affects the scaling attribute when combined 
	 * with another transformation
	 */
	public static Transformation scalingTransformation(double scaling)
	{
		return identityTransformation().withScaling(new Vector3D(scaling, scaling));
	}
	
	/**
	 * @param shear The shear attribute of the transformation
	 * @return A transformation that only affects the shear attribute when combined 
	 * with another transformation
	 */
	public static Transformation shearTransformation(Vector3D shear)
	{
		return identityTransformation().withShear(shear);
	}
	
	/**
	 * @param rotation The angle attribute of the transformation
	 * @return A transformation that only affects the angle attribute when combined 
	 * with another transformation
	 */
	public static Transformation rotationTransformation(double rotation)
	{
		return identityTransformation().withAngle(rotation);
	}
}
