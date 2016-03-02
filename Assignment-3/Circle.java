/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 3, due March 4, 2016
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// Class represents a Circle object (Superclass: Shape)
// The origin is located at the center
public class Circle extends Shape {
	
	/**
	 * The radius of the rectangle
	 */
	private double radius;
	
	/**
	 * Pi (used to compute the circle's area and circumference)
	 */
	private final double PI = 3.14159265359;
	
	/**
	 * Constructor lets user specify a radius and origin coordinates
	 * @param rad: The radius of the circle
	 * @param x: The x-coordinate of the circle's origin
	 * @param y: The y-coordinate of the circle's origin
	 */
	public Circle (double rad, double x, double y) {
		super(x, y);
		SetRadius(rad);
	}
	
	/**
	 * Constructor (default) creates a circle at origin (0, 0)
	 * and with radius set to zero
	 */
	public Circle () {
		this(0,0,0);
	}
	
	/**
	 * Sets the circle's radius
	 * @param rad: The circle's radius
	 */
	public void SetRadius (double rad) {
		radius = rad;
	}
	
	/**
	 * Get the radius of the circle
	 * @return radius: The circle's radius
	 */
	public double GetRadius () {
		return radius;
	}
	
	/**
	 * Get the area of the circle
	 * @return area: The circle's area
	 */
	public double GetArea () {
		double area = PI * (radius * radius);
		return area;
	}
	
	/**
	 * Get the circumference of the circle
	 * @return circumference: The circle's circumference
	 */
	public double GetCircumference () {
		double circumference = 2 * PI * radius;
		return circumference;
	}
	
	/**
	 * Get the circle's current state as a string
	 * @return state: The circle's current state
	 */
	public String toString() {
		String state = ("Shape is a Circle\n" +
				super.toString() +
				"\nRadius: " + this.GetRadius() + "\n");
		return state;
	}
}
