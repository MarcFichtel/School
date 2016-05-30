import java.util.Scanner;

// CPSC 331 - Assignment 1
// Student: Marc-Andre Fichtel
// Due: June 2, 11pm

// Class performs matrix multiplication with matrices given by user
public class MatrixMultiplication {

	static Scanner input = new Scanner(System.in); 	// Initialize scanner for user input
	static int matrixARows;							// First matrix's # of rows
	static int matrixACols;							// First matrix's # of columns
	static int matrixBRows;							// Second matrix's # of rows
	static int matrixBCols;							// Second matrix's # of columns
	static int[][] matrixA;							// The first matrix
	static int[][] matrixB;							// The second matrix
	static int[][] matrixC;							// The result of multiplying matrices A and B
	
	// Get all user inputs (matrix dimensions and entries)
	// Preconditions: 
	//	~ An input scanner has been initialized
	// 	~ 4 global integer variables for first two matrices' dimensions have been declared
	// 	~ 2 global 2D-integer arrays for first two matrices have been declared
	// Postconditions:
	// 	~ 4 global integer variables for first two matrices' dimensions have been initialized, 
	// 		are greater than zero, and the first matrix's columns is equal to the second matrix's rows
	// 	~ 2 global 2D-integer arrays for first two matrices have been initialized and filled with integer entries
	public static void GetMatrixInput () throws Exception {
		
		// Prompt user for first matrix's dimensions
		System.out.print("Enter first matrix's dimensions.\nRows (between 1 and 5): ");
		matrixARows = input.nextInt(); 			// Prompt for # of rows						
		System.out.print("Columns (between 1 and 5): ");
		matrixACols = input.nextInt();			// Prompt for # of columns

		// Prompt user for second matrix's dimensions
		System.out.print("\nEnter second matrix's dimensions.\nRows (between 1 and 5): ");												
		matrixBRows = input.nextInt(); 			// Prompt for # of rows		
		System.out.print("Columns (between 1 and 5): ");
		matrixBCols = input.nextInt();			// Prompt for # of columns
		
		// Check, if correct dimension values were entered (i.e. values greater than 0)
		if (matrixARows < 1 || matrixARows > 5 ||
			matrixACols < 1 || matrixACols > 5 ||
			matrixBRows < 1 || matrixBRows > 5 ||
			matrixBCols < 1 || matrixBCols > 5) {
			
			// Throw exception
			throw new Exception ("Incorrect dimension values entered.");
		}
		
		// If compatible dimensions were entered, initialize matrices, else throw an exception
		if (matrixACols == matrixBRows) {
			matrixA = new int[matrixARows][matrixACols]; 			// Initialize first matrix
			matrixB = new int[matrixBRows][matrixBCols];			// Initialize second matrix
		} else {
			
			// Throw exception
			throw new Exception ("Matrix dimensions are incompatible for multiplication.");
		}
		
		// Prompt user for first matrix's entries
		System.out.println("\nEnter first matrix's entries.");
		for (int i = 0; i < matrixARows; i++) {			// Iterate the counter i over elements in matrixARows starting from 0
			for (int j = 0; j < matrixACols; j++) {		// Iterate the counter j over elements in matrixACols starting from 0
				System.out.print("Entry " + (i+1) + "x" + (j+1) + ": ");
				matrixA[i][j] = input.nextInt();		// Prompt user for each matrix entry
			}
		}
		
		// Prompt user for second matrix's entries
		System.out.println("\nEnter second matrix's entries.");
		for (int i = 0; i < matrixBRows; i++) {			// Iterate the counter i over elements in matrixBRows starting from 0
			for (int j = 0; j < matrixBCols; j++) {		// Iterate the counter j over elements in matrixBCols starting from 0
				System.out.print("Entry " + (i+1) + "x" + (j+1) + ": ");
				matrixB[i][j] = input.nextInt();		// Prompt user for each matrix entry
			}
		}
	}
	
	// Method takes two square matrices as arguments and attempts to multiply them
	// Preconditions:
	// 	~ 4 global integer variables for first two matrices' dimensions have been initialized, 
	// 		are greater than zero, and the first matrix's columns is equal to the second matrix's rows
	// 	~ 2 global 2D-integer arrays for first two matrices have been initialized and filled with integer entries
	// 	~ The global 2D-integer array for third matrix has been declared
	// Postconditions:
	// 	~ The global 2D-integer array for third matrix has been initialized and filled with integer entries
	public static int[][] MultiplyMatrices (int[][] matrixA, int[][] matrixB) {

		// Create result matrix with the proper dimensions
		int[][] result = new int[matrixARows][matrixBCols];

		// Use 3 for loops for the calculations:
		// 1. loop: Iterate the counter i over elements in matrixARows starting from 0
		for (int i = 0; i < matrixARows; i++) {
			
			// 2. loop: Iterate the counter j over elements in matrixBCols starting from 0
			for (int j = 0; j < matrixBCols; j++) {
				
				// 3. loop: Iterate the counter k over elements in matrixACols starting from 0
				// Each loop here represents one calculation required to sum up each matrix entry
				for (int k = 0; k < matrixACols; k++) {
					
					// Perform calculations based on given formula for matrix multiplication
					result[i][j] += matrixA[i][k] * matrixB[k][j]; 
				}
			}
		}

		// Return the resulting matrix
		return result;
	}

	// Method displays three given matrices in rectangular form
	// Preconditions: 
	// 	~ All global 2D-integer arrays for three matrices have been initialized and filled with integer entries
	// Postconditions:
	// 	~ All global 2D-integer arrays for three matrices have been displayed to the user
	public static void DisplayMatrices (int[][] matrixA, int[][] matrixB, int[][] matrixC) {
		
		// Display first matrix
		System.out.println("\nFirst matrix: ");
		for (int i = 0; i < matrixA.length; i++) {			// Iterate the counter i over elements (rows) in matrixA starting from 0
			for (int j = 0; j < matrixA[0].length; j++) {	// Iterate the counter j over elements (columns) in matrixA starting from 0
				System.out.print(matrixA[i][j] + "\t"); 	// Print each entry
			}
			System.out.print("\n");							// Go to next line after each row
		}
		
		// Display second matrix
		System.out.println("Second matrix: ");
		for (int i = 0; i < matrixB.length; i++) {			// Iterate the counter i over elements (rows) in matrixB starting from 0
			for (int j = 0; j < matrixB[0].length; j++) {	// Iterate the counter j over elements (columns) in matrixB starting from 0
					System.out.print(matrixB[i][j] + "\t"); // Print each entry
				}
			System.out.print("\n");							// Go to next line after each row
		}
				
		// Display third matrix
		System.out.println("Third matrix obtained by multipliying the first two matrices: ");
		for (int i = 0; i < matrixC.length; i++) {			// Iterate the counter i over elements (rows) in matrixC starting from 0
			for (int j = 0; j < matrixC[0].length; j++) {	// Iterate the counter j over elements (columns) in matrixC starting from 0
				System.out.print(matrixC[i][j] + "\t"); 	// Print each entry
			}
			System.out.print("\n");							// Go to next line after each row
		}
	}
	
	// Client code executes the program
	public static void main (String[] args) {

		// Use try/catch to cover any errors, particularly wrong user input
		try {
			GetMatrixInput(); 								// SP1: Get matrix dimensions
			matrixC = MultiplyMatrices (matrixA, matrixB);	// SP2: Perform matrix multiplication
			DisplayMatrices (matrixA, matrixB, matrixC);	// SP3: Display all matrices
		
		// If an exception occurs, print its error message, and inform user
		} catch (Exception ex) {	
			System.out.println("\n" + ex.toString() + "\nProgram terminated.");
		
		// Close input scanner once program is done
		} finally {
			input.close();
		}
	}
}