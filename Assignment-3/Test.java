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
		
		int menuChoice = -2;	// Number controls menu flow
		
		// Create an ArrayList to hold all shape objects
		ArrayList shapes = new ArrayList();
		
		// Welcome the user
		System.out.println("Dear User,\n"
				+ "This program lets you create, edit, and delete Rectangle and Circle Shapes.\n"
				+ "Please choose an option.\n");
		
		// Create text menu, Sentinel value -1 ends the program
		while (menuChoice != -1) {
			
			// Display ArrayList contents, if there are any
			if (shapes.size() > 0) {
				System.out.println("Shapes:");
				for (int i = 0; i < shapes.size(); i++) {
					if (shapes.get(i).getClass().equals(Rectangle.class)) {
						System.out.println("Shape " + i + ": Rectangle");
					} else {
						System.out.println("Shape " + i + ": Circle");
					}
				}
				System.out.print("\n");
			}
			
			// Display menu options
			System.out.println("Options:" + 
					"\n\t [0] Create shapes." +
					"\n\t [1] Set Origin for a shape." + 
					"\n\t [2] Set Dimensions for a shape." + 
					"\n\t [3] Display state of a shape." +
					"\n\t [4] Move a shape." +
					"\n\t [5] Show Distance between two shapes." +
					"\n\t [6] Destroy a shape." +
					"\n\t [7] List state of all shapes." +
					"\n\t [-1] Exit Program.");
			
			// Prompt user to choose an option
			System.out.print("Choose Option: ");
			menuChoice = input.nextInt();
			System.out.print("\n");
			
			// Validate user input for menu choice
			if (menuChoice < -1 || menuChoice > 7) {
				System.out.println("Please choose one of the given options.\n");
			}
			
			// Options 1-7 require existing shapes
			else if ((menuChoice >= 1 && menuChoice <= 7) && shapes.size() == 0) {
				System.out.println("Please create Shapes before choosing this option.\n");
			}
			
			// Option 0: Create Shapes
			else if (menuChoice == 0) {
				
				// If 10 shapes exist already, inform user
				if (shapes.size() >= 10) {
					System.out.println("Maximum number of shapes reached. Please choose a different option.\n");
				
				} else {				
					int numOfRects = -1; 	// Number of rectangles chosen by user
					int numOfCircs = -1;	// Number of circles chosen by user
					
					// Request user input until valid input has been given for number of rectangles
					System.out.println("Create up to 10 Shapes.");
					while (numOfRects < 0 || numOfRects > 10) {
						
						// Prompt user for number of rectangles
						System.out.print("How many Rectangles would you like to create? ");
						numOfRects = input.nextInt();
						
						// Inform user of invalid data input
						if (numOfRects < 0 || numOfRects > 10) {
							System.out.println("Please choose a number between 0 and 10.");
						}
					}
					
					// Request user input until valid input has been given for number of circles
					if (numOfRects != 10) {
						while (numOfCircs < 0 || numOfCircs + numOfRects > 10) {
									
							// Prompt user for number of rectangles
							System.out.print("How many Circles would you like to create? ");
							numOfCircs = input.nextInt();		
							
							// Inform user of invalid data input
							if (numOfCircs < 0 || numOfCircs + numOfRects > 10) {
								System.out.println("Please choose a number between 0 and " + (10 - numOfRects) + ".");
							}
						}
						System.out.print("\n");
					}
				
					// Create and store shapes in the ArrayList
					for (int j = 0; j < numOfRects; j++) {
						shapes.add(new Rectangle());
					}
					for (int j = 0; j < numOfCircs; j++) {
						shapes.add(new Circle());
					}
				}
			}
			
			// Option 1: Set Origin
			else if (menuChoice == 1) {
				
				// Let user indicate a shape for which to set the origin
				System.out.print("For which shape do you want to set the Origin? ");
				int chosenShape = input.nextInt();

				// Prompt user for new origin
				double xVal, yVal;
				System.out.print("Enter the shape's new origin.\nX: ");
				xVal = input.nextDouble();
				System.out.print("Y: ");
				yVal = input.nextDouble();
				System.out.print("\n");

				// Set origin of chosen shape to given values
				((Shape)shapes.get(chosenShape)).SetOrigin(xVal, yVal);
			}
			
			
			// Option 2: Set dimensions
			else if (menuChoice == 2) {
				
				// Let user indicate a shape for which to set the dimensions
				System.out.print("For which shape do you want to set the Dimensions? ");
				int chosenShape = input.nextInt();

				// Prompt user for length and width, if shape is a rectangle
				if (shapes.get(chosenShape).getClass().equals(Rectangle.class)) {
					System.out.print("Shape is a Rectangle. \nEnter new Length: ");
					double newLength = input.nextDouble();
					System.out.print("Enter new Width: ");
					double newWidth = input.nextDouble();
					System.out.print("\n");

					((Rectangle)shapes.get(chosenShape)).SetSize(newLength, newWidth);

					// Prompt user for radius, if shape is a circle
				} else {
					System.out.print("Shape is a Circle. Enter new Radius: ");
					double newRadius = input.nextDouble();
					System.out.print("\n");

					((Circle)shapes.get(chosenShape)).SetRadius(newRadius);
				}
			}
			
			
			// Option 3: Display shape state
			else if (menuChoice == 3) {
					
				// Let user indicate a shape for which to display the state
				System.out.print("For which shape do you want to display the state? ");
				int chosenShape = input.nextInt();

				// Display shape state
				if (shapes.get(chosenShape).getClass().equals(Rectangle.class)) {
					System.out.println(((Rectangle)shapes.get(chosenShape)).toString());
				} else {
					System.out.println(((Circle)shapes.get(chosenShape)).toString());
				}
			}
			
			
			// Option 4: Move shape
			else if (menuChoice == 4) {

				// Let user indicate which shape should be moved
				System.out.print("Which shape do you want to move? ");
				int chosenShape = input.nextInt();

				// Prompt user for values by which to move the shape
				System.out.println("Enter values by which to move the shape.");
				System.out.print("Enter x-value: ");
				double deltaX = input.nextDouble();
				System.out.print("Enter y-value: ");
				double deltaY = input.nextDouble();
				System.out.print("\n");

				// Move shape
				((Shape)shapes.get(chosenShape)).MoveShape(deltaX, deltaY);
			}
			
			
			// Option 5: Distance between shapes
			else if (menuChoice == 5) {

				// Let user indicate which shapes should be compared
				System.out.print("Which shapes do you want to compare? \nFirst Shape: ");
				int chosenShape1 = input.nextInt();
				System.out.print("Second Shape: ");
				int chosenShape2 = input.nextInt();

				// Get and display distance between chosen shapes
				System.out.println("Distance: " + ((Shape)shapes.get(chosenShape1)).
						ComputeDistance(((Shape)shapes.get(chosenShape2))) + "\n");
			}
			
			
			// Option 6: Destroy shape
			else if (menuChoice == 6) {

				// Let user indicate which shape should be deleted
				System.out.print("Which shape do you want to destroy? ");
				int chosenShape = input.nextInt();

				// Delete chosen shape
				shapes.remove(chosenShape);
			}
			
			
			// Option 7: List state of all shapes
			else if (menuChoice == 7) {

				// Iterate through ArrayList and print each shape's state
				for (int i = 0; i < shapes.size(); i++) {
					if (shapes.get(i).getClass().equals(Rectangle.class)) {
						System.out.println("Shape " + i + ": " +
								((Rectangle)shapes.get(i)).toString());
					} else {
						System.out.println("Shape " + i + ": " +
								((Circle)shapes.get(i)).toString());
					}
				}
			}
		}
		
	// Inform user that the program is done
	System.out.println("Program done.");
	input.close();
	}
}
