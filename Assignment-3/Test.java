import java.util.ArrayList;
import java.util.Scanner;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 3, due March 4, 2016
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// Class tests the classes Point, Shape, Rectangle, and Circle
public final class Test extends Object {
	public static void main(String[] args) {
		
		Scanner input = new Scanner (System.in);
		
		// Create an ArrayList to hold all shape objects
		ArrayList shapes = new ArrayList(10);
		
		// Create and store 5 rectangles and circles each in the ArrayList
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				if (j == 0) {
					shapes.add(new Rectangle());
				} else {
					shapes.add(new Circle());
				}
			}
		}
		System.out.println("Created 5 rectangles and 5 circles.\n");
		
		// Set the origin for a rectangle and a circle
		((Rectangle)shapes.get(0)).SetOrigin(1, 2); // Rectangle
		((Circle)shapes.get(1)).SetOrigin(3, 4); 	// Circle
		
		// Set the dimensions for a rectangle and a circle
		((Rectangle)shapes.get(0)).SetSize(2, 4); 	// Rectangle
		((Circle)shapes.get(1)).SetRadius(5); 		// Circle
					
		// Display the state of a rectangle and a circle
		System.out.println("First Rectangle State: " + 
				((Rectangle)shapes.get(0)).toString()); // Rectangle
		System.out.println("First Circle State: " + 
				((Circle)shapes.get(1)).toString()); 	// Circle
		
		// Prompt user for values by which to move the shapes
		System.out.println("Enter values by which to move the first rectangle and circle.");
		System.out.print("Enter x-value: ");
		double deltaX = input.nextDouble();
		System.out.print("Enter y-value: ");
		double deltaY = input.nextDouble();
		
		// Move the shapes by the user specified values
		((Rectangle)shapes.get(0)).MoveShape(deltaX, deltaY);
		((Circle)shapes.get(1)).MoveShape(deltaX, deltaY);
		System.out.println("The shapes have been moved.\n");
		
		// Compute and display the distance between the two shapes
		System.out.println("Distance between the first rectangle and circle is: " + 
				((Rectangle)shapes.get(0)).ComputeDistance(
						((Circle)shapes.get(1))) + "\n");
		
		// Destroy all but the first two shapes
		for (int i = 2; i < 10; i++) {
			shapes.set(i, null);
		}
		System.out.println("All other shapes have been destroyed.\n");
		
		// Display state of all object
		System.out.println("State of all available shapes: ");
		
		// Iterate through shapes, ignoring all null pointers
		for (int i = 0; i < shapes.size(); i++) {
			
			// If the shape is a rectangle, print its state
			if (shapes.get(i) != null &&
				shapes.get(i).getClass().equals(Rectangle.class)) {
				System.out.println("Rectangle at index " + i + ": " +
						((Rectangle)shapes.get(i)).toString());
			
			// If the shape is a circle, print its state
			} else if (shapes.get(i) != null) {
				System.out.println("Circle at index " + i + ": " +
						((Circle)shapes.get(i)).toString());
			}
		}
		
		// Inform user that the program is done
		System.out.println("Program done.");
		input.close();
	}
}
