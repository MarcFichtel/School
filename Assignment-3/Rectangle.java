/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 3, due March 4, 2016
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// Class represents a Rectangle object (Superclass: Shape)
// The origin is located at the lower-left hand corner
public class Rectangle extends Shape {

	/**
	 * The length of the rectangle
	 */
	private double length;
	
	/**
	 * The width of the rectangle
	 */
	private double width;
	
	/**
	 * Constructor lets user specify dimensions and origin coordinates
	 * @param len: The length of the rectangle
	 * @param wid: The width of the rectangle
	 * @param x: The x-coordinate of the rectangle's origin
	 * @param y: The y-coordinate of the rectangle's origin
	 */
	public Rectangle (double len, double wid, double x, double y) {
		SetSize(len, wid);
		this.SetOrigin(x, y);
	}
	
	/**
	 * Constructor (default) creates a rectangle at origin (0, 0)
	 * and with length and width set to zero
	 */
	public Rectangle () {
		this(0,0,0,0);
	}
	
	/**
	 * Sets the rectangle's length
	 * @param len: The rectangle's length
	 */
	public void SetLength (double len) {
		length = len;
	}
	
	/**
	 * Sets the rectangle's width
	 * @param wid: The rectangle's width
	 */
	public void SetWidth (double wid) {
		width = wid;
	}
	
	/**
	 * Sets the rectangle's dimensions
	 * @param len: The rectangle's length
	 * @param wid: The rectangle's width
	 */
	public void SetSize (double len, double wid) {
		length = len;
		width = wid;
	}
	
	/**
	 * Get the length of the rectangle
	 * @return length: The rectangle's length
	 */
	public double GetLength () {
		return length;
	}
	
	/**
	 * Get the width of the rectangle
	 * @return width: The rectangle's width
	 */
	public double GetWidth () {
		return width;
	}
	
	/**
	 * Get the area of the rectangle
	 * @return area: The rectangle's area
	 */
	public double GetArea () {
		double area = length * width;
		return area;
	}
	
	/**
	 * Get the circumference of the rectangle
	 * @return circumference: The rectangle's circumference
	 */
	public double GetCircumference () {
		double circumference = 2 * GetArea();
		return circumference;
	}
	
	/**
	 * Get the rectangle's current state as a string
	 * @return state: The rectangle's current state
	 */
	public String toString() {
		String state = ("Origin at X: " + this.GetOrigin().GetPointX() +
				", Y: " + this.GetOrigin().GetPointY() +
				"\nLength: " + this.GetLength() +
				"\nWidth: " + this.GetWidth() +
				"\nArea: " + this.GetArea() +
				"\nCircumference: " + this.GetCircumference() + "\n");
		return state;
	}
}
