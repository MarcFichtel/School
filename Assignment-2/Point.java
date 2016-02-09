<<<<<<< HEAD
/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 2, due  February 12, 2016
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */


// Class represents Points in a Cartesian coordinate system
public class Point {
	
	/**
	 * idCounter is used to assign a unique ID to every point
	 */
	private static int idCounter = 1;
	
	/**
	 * activeInstances counts the number of currently active instances
	 */
	private static int activeInstances = 0;
	
	/**
	 * pointID stores a point's unique ID
	 */
	private int pointID = 0;
	
	/**
	 * A point's x-coordinate
	 */
	private double xPos;
	
	/**
	 * A point's y-coordinate
	 */
	private double yPos;

	
	/**
	 * Constructor lets user specify point coordinates
	 * @param x: X coordinate of the point
	 * @param y: Y coordinate of the point
	 */
	public Point (double x, double y) {
=======
/* Author: Marc-Andre Fichtel
 * Assignment 2, due  February 12, 2016
 * Course: CPSC 233 
 * University of Calgary
 * Tutorial 05
 * Instructor: Edward Chan */


// Class represents Points in a Catesian coordinate system
public class Point {
	
	private static int idCounter = 1;
	private static int activeInstances = 0;
	private int pointID = 0;
	private double xPos;
	private double yPos;

	
	// Constructor lets user specify point coordinates
	Point (double x, double y) {
>>>>>>> origin/master
		xPos = x;
		yPos = y;
		pointID = idCounter;
		idCounter++;
		activeInstances++;
	}
	
<<<<<<< HEAD
	/**
	 * Default constructor creates a point at the origin (0, 0)
	 */
	public Point () {
		this(0, 0);
	}
	
	/** 
	 * Set point's x and y location
	 * @param newX: Point's new x-coordinate
	 * @param newY: Point's new y-coordinate
	 */
=======
	// Default constructor creates a point at location (0, 0)
	Point () {
		this(0, 0);
	}
	
	// Set point's x and y location
>>>>>>> origin/master
	public void SetPosition (double newX, double newY) {
		xPos = newX;
		yPos = newY;
	}
	
<<<<<<< HEAD
	/** 
	 * Set point's x location
	 * @param newX: Point's new x-coordinate
	 */
=======
	// Set point's x location
>>>>>>> origin/master
	public void SetPointX (double newX) {
		xPos = newX;
	}
	
<<<<<<< HEAD
	/**
	 * Set point's y location
	 * @param newY: Point's new y-coordinate
	 */
=======
	// Set point's y location
>>>>>>> origin/master
	public void SetPointY (double newY) {
		yPos = newY;
	}
	
<<<<<<< HEAD
	/** 
	 * Get point's x location
	 * @return xPos: Point's current x-coordinate
	 */
=======
	// Get point's x location
>>>>>>> origin/master
	public double GetPointX () {
		return xPos;
	}
	
<<<<<<< HEAD
	/** 
	 * Get point's y location
	 * @return yPos: Point's current y-coordinate
	 */
=======
	// Get point's y location
>>>>>>> origin/master
	public double GetPointY () {
		return yPos;
	}
	
<<<<<<< HEAD
	/**
	 * Move point's x and y locations by the given values
	 * @param deltaX is the value by which xPos is moved
	 * @param deltaY is the value by which yPos is moved
	 */
=======
	// Move point's x and y locations by the given values
	// Takes 2 parameters:
	//		deltaX is the value by which xPos is moved
	// 		deltaY is the value by which yPos is moved
>>>>>>> origin/master
	public void MovePoint (double deltaX, double deltaY) {
		xPos += deltaX;
		yPos += deltaY;
	}
	
<<<<<<< HEAD
	/** 
	 * Compute the distance between this and another given point
	 * @param endPoint: The given second point
	 * @return distance: the distance between this and the given point
	 */
	public double ComputeDistance (Point endPoint) {
		double distance = 0.0;
		
		// Create a distance vector point
		Point distanceVector = new Point();
		
		// Decrement idCounter and activeInstances 
		// (the vector is a point but should not be counted)
		idCounter--;
		activeInstances--;
		
		// Get distance vector values
		distanceVector.xPos = endPoint.xPos - this.xPos;
		distanceVector.yPos = endPoint.yPos - this.yPos;
		
		// Compute magnitude of the distance vector
		distance = Math.sqrt(
				(distanceVector.xPos*distanceVector.xPos)+
				(distanceVector.yPos*distanceVector.yPos));

		return distance;
	}
	
	/** 
	 * Get point's unique ID
	 * @return pointID
	 */
=======
	// Compute the distance between this and another given point
	// Takes 1 parameter:
	// 		endPoint is the given second point
	// Returns the distance between this and the given point
	public double ComputeDistance (Point endPoint) {
		double distance = 0.0;
		Point distanceVector = new Point();
		distanceVector.xPos = endPoint.xPos - this.xPos;
		distanceVector.yPos = endPoint.yPos - this.yPos;
		distance = Math.sqrt(
				(distanceVector.xPos*distanceVector.xPos)+
				(distanceVector.yPos*distanceVector.yPos));
		idCounter--;
		activeInstances--;
		return distance;
	}
	
	// Get point's unique ID
>>>>>>> origin/master
	public int PointID () {
		return pointID;
	}
	

<<<<<<< HEAD
	/** 
	 * Method is called by garbage collector
	 */
	protected void finalize () throws Throwable {
		super.finalize();
		activeInstances--;
	}

	/** 
	 * Get the number of active point instances
	 * @return activeInstances
	 */
=======
	protected void finalize () throws Throwable {
		activeInstances--;
		super.finalize();
	}

	// Get the number of active point instances
>>>>>>> origin/master
	public static int ActiveInstances () {
		return activeInstances;
		
	}
	
<<<<<<< HEAD
	/** 
	 * Class method counter for computing the distance between two given points
	 * @param start: A starting point
	 * @param end: An ending point
	 * @return distance: the distance between the starting and ending point as a double
	 */
=======
>>>>>>> origin/master
	public static double ComputeDistance (Point start, Point end) {
		double distance = start.ComputeDistance(end);
		return distance;
	}
}
