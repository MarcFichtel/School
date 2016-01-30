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
		xPos = x;
		yPos = y;
		pointID = idCounter;
		idCounter++;
		activeInstances++;
	}
	
	// Default constructor creates a point at location (0, 0)
	Point () {
		this(0, 0);
	}
	
	// Set point's x and y location
	public void SetPosition (double newX, double newY) {
		xPos = newX;
		yPos = newY;
	}
	
	// Set point's x location
	public void SetPointX (double newX) {
		xPos = newX;
	}
	
	// Set point's y location
	public void SetPointY (double newY) {
		yPos = newY;
	}
	
	// Get point's x location
	public double GetPointX () {
		return xPos;
	}
	
	// Get point's y location
	public double GetPointY () {
		return yPos;
	}
	
	// Move point's x and y locations by the given values
	// Takes 2 parameters:
	//		deltaX is the value by which xPos is moved
	// 		deltaY is the value by which yPos is moved
	public void MovePoint (double deltaX, double deltaY) {
		xPos += deltaX;
		yPos += deltaY;
	}
	
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
	public int PointID () {
		return pointID;
	}
	

	protected void finalize () throws Throwable {
		activeInstances--;
		super.finalize();
	}

	// Get the number of active point instances
	public static int ActiveInstances () {
		return activeInstances;
		
	}
	
	public static double ComputeDistance (Point start, Point end) {
		double distance = start.ComputeDistance(end);
		return distance;
	}
}
