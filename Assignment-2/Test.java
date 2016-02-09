import java.util.Scanner;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 2, due  February 12, 2016
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// Class tests the Point class
public class Test {
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner (System.in);
		
		// Create & initialize testing variables
		double pointX1 = 0, pointY1 = 0, pointX2 = 0, 
			   pointY2 = 0, pointX3 = 0, pointY3 = 0;
		double xDelta = 0, yDelta = 0;
		double dist4to1 = 0, dist4to2 = 0, dist4to3 = 0;
		double xAverage = 0, yAverage = 0;
		
		// Get coordinates of 3 initial points
		System.out.print("Enter first point's coordinates.\nX: ");
		pointX1 = input.nextDouble();
		System.out.print("Y: ");
		pointY1 = input.nextDouble();
		System.out.print("Enter second point's coordinates.\nX: ");
		pointX2 = input.nextDouble();
		System.out.print("Y: ");
		pointY2 = input.nextDouble();
		System.out.print("Enter third point's coordinates.\nX: ");
		pointX3 = input.nextDouble();
		System.out.print("Y: ");
		pointY3 = input.nextDouble();

		// Create 3 initial points
		Point point1 = new Point(pointX1, pointY1);
		Point point2 = new Point(pointX2, pointY2);
		Point point3 = new Point(pointX3, pointY3);
		
		// Get values by which to move all points
		System.out.print("\nEnter values by which to move all points.\nX: ");
		xDelta = input.nextDouble();
		System.out.print("Y: ");
		yDelta = input.nextDouble();
		
		// Move initial 3 points by the given values
		point1.MovePoint(xDelta, yDelta);
		point2.MovePoint(xDelta, yDelta);
		point3.MovePoint(xDelta, yDelta);
		
		// Print out new coordinates of 3 initial points
		System.out.println("Point 1. ID: " + point1.PointID() + 
				", X: " + point1.GetPointX() + 
				", Y: " + point1.GetPointY());
		System.out.println("Point 2. ID: " + point2.PointID() + 
				", X: " + point2.GetPointX() + 
				", Y: " + point2.GetPointY());
		System.out.println("Point 3. ID: " + point3.PointID() + 
				", X: " + point3.GetPointX() + 
				", Y: " + point3.GetPointY());
		
		// Print out the number of active instances (should be 3)
		System.out.println("\nActive Instances: " + Point.ActiveInstances() + "\n");
		
		// Create a fourth point at the origin, and print out its coordinates
		Point point4 = new Point();
		System.out.println("Point 4. ID: " + point4.PointID() + 
				", X: " + point4.GetPointX() + 
				", Y: " + point4.GetPointY() + "\n");
		
		// Compute and print out the distance from initial 3 to the fourth point
		// Distance from P3 to P4 uses the class method ComputeDistance
		dist4to1 = point4.ComputeDistance(point1);
		dist4to2 = point4.ComputeDistance(point2);
		dist4to3 = Point.ComputeDistance(point4, point3);
		System.out.println("Distance between Point 1 to Point 4: " + dist4to1);
		System.out.println("Distance between Point 2 to Point 4: " + dist4to2);
		System.out.println("Distance between Point 3 to Point 4: " + dist4to3);

		// Get the average of the first 3 points' coordinates
		xAverage = (point1.GetPointX() + point2.GetPointX() + point3.GetPointX()) / 3;
		yAverage = (point1.GetPointY() + point2.GetPointY() + point3.GetPointY()) / 3;
		System.out.println("\nAverage X Position: " + xAverage);
		System.out.println("Average Y Position: " + yAverage);
		
		// Create and print out the coordinates of a fifth point at the average values
		xAverage = (point1.GetPointX() + point2.GetPointX() + point3.GetPointX()) / 3;
		yAverage = (point1.GetPointY() + point2.GetPointY() + point3.GetPointY()) / 3;

		Point point5 = new Point (xAverage, yAverage);
		System.out.println("\nPoint 5. ID: " + point5.PointID() + 
				", X: " + point5.GetPointX() + 
				", Y: " + point5.GetPointY());
		
		// Print out the number of active instances (should be 5)
		System.out.print("\nActive Instances: " + Point.ActiveInstances() + "\n");
		
		// Force garbage collection (keep calling it until it works)
		while (Point.ActiveInstances() >= 3) {
			System.gc();			// Usually takes 2 to 4 loops until garbage has been collected
			System.out.println("Garbage is being collected...");
		}

		// Print out the number of active instances (should be 2)
		System.out.print("Deleted references to Points 1, 2, and 3, and forced Garbage Collection");
		System.out.print("\nActive Instances: " + Point.ActiveInstances() + "\n");
		
		input.close();
	}
}
