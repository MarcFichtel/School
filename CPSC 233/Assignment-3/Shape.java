/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 3, due March 4, 2016
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// Abstract Class represents a Shape object (Superclass: Object)
public abstract class Shape extends Object {

	/**
	 * The Shape's origin
	 */
	private Point origin;
	
	/**
	 * The Shape's area
	 */
	private double area = 0.0;
	
	/**
	 * The Shape's circumference
	 */
	private double circumference = 0.0;
	
	/**
	 * Constructor lets user specify origin coordinates
	 * @param x: The x-coordinate of the origin
	 * @param y: The y-coordinate of the origin
	 */
	public Shape (double x, double y) {
		origin = new Point(x, y);
	}
	
	/**
	 * Constructor (default) creates a shape at origin (0, 0)
	 */
	public Shape () {
		this (0, 0);
	}
	
	/**
	 * Sets origin coordinates to the given values
	 * @param xVal: The new x-coordinate of the origin
	 * @param yVal: The new y-coordinate of the origin
	 */
	public void SetOrigin (double xVal, double yVal) {
		origin.SetPosition(xVal, yVal);
	}
	
	/**
	 * Gets the origin
	 * @return origin: The origin point
	 */
	public Point GetOrigin () {
		return origin;
	}
	
	/**
	 * Move shape's origin by the given values
	 * @param deltaX is the value by which origin's x-coordinate is moved
	 * @param deltaY is the value by which origin's y-coordinate is moved
	 */
	public void MoveShape (double deltaX, double deltaY) {
		SetOrigin(origin.GetPointX() + deltaX, origin.GetPointY() + deltaY);
	}
	
	/** 
	 * Compute the distance between this and another given shape
	 * @param endShape: The given second shape
	 * @return distance: the distance between this and the given shape
	 */
	public double ComputeDistance (Shape endShape) {
		double distance = 0.0;
		
		// Create a distance vector point
		Point distanceVector = new Point();
		
		// Get distance vector values
		distanceVector.SetPointX(endShape.GetOrigin().GetPointX() - this.GetOrigin().GetPointX());
		distanceVector.SetPointY(endShape.GetOrigin().GetPointY() - this.GetOrigin().GetPointY());
		
		// Compute magnitude of the distance vector
		distance = Math.sqrt(
				(distanceVector.GetPointX()*distanceVector.GetPointX())+
				(distanceVector.GetPointY()*distanceVector.GetPointY()));

		return distance;
	}
	
	/**
	 * Get the shape's area
	 * @return area: The shape's area
	 */
	public abstract double GetArea ();
	
	/**
	 * Get the shape's circumference
	 * @return circumference: The shape's circumference
	 */
	public abstract double GetCircumference ();
	
	/**
	 * Get the shape's current state as a string
	 * @return state: The shape's current state
	 */
	public String toString () {
		String state = ("Origin at X: " + origin.GetPointX() + 
				", Y: " + origin.GetPointY() +
				"\nArea: " + this.GetArea() +
				"\nCircumference: " + this.GetCircumference());
		return state;
	}
}
