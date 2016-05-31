import java.util.Scanner;

// CPSC 331 - Assignment 1
// Student: Marc-Andre Fichtel
// Due: June 2, 11pm

// Class performs matrix multiplication with matrices given by user
public class MatrixMultiplication {

	static int[][] matrixA;		// Declare the first matrix
	static int[][] matrixB;		// Declare the second matrix
	static int[][] matrixC;		// Declare the third matrix
								// 	(result of the multiplication)
	
	// Method prompts user for all inputs (matrix dimensions and entries) 
	// Precondition P1 - Inputs include:
	//		~ matrixA and matrixB: Globally declared uninitialized 
	//			static 2D integer arrays
	//		~ User integer input for number of dimensions of matrixA and matrixB, 
	//			each between 1 and 5, where matrixA’s number of columns is equal 
	//			to matrixB’s number of rows.
	//		~ User integer input for every entry of matrixA and matrixB.
	//	Postcondition Q1:
	//		~ matrixA: A globally declared initialized static 2D integer array 
	//			with number of rows m and number of columns n, where 0 < m, n < 6.
	//		~ matrixB: A globally declared initialized static 2D integer array 
	//			with number of rows n and number of columns k, where 0 < n, k < 6.
	//			--> SP1 was successfully executed
	//	Precondition P2 - Inputs include:
	//		~ matrixA and matrixB: Globally declared uninitialized static 2D integer arrays
	//		~ User integer input for number of dimensions of matrixA and matrixB, 
	//			where any one of them is smaller than 1 or greater than 5, and matrixA’s 
	//			number of columns is equal to matrixB’s number of rows.
	//		~ User integer input for every entry of matrixA and matrixB.
	//	Postcondition Q2:
	//		~ An exception (Dimensions out of range) is thrown.
	//			--> SP1 was unsuccessful (an error occurred)
	// Precondition P3 - Inputs include:
	//		~ matrixA and matrixB: Globally declared uninitialized static 2D integer arrays
	//		~ User integer input for number of dimensions of matrixA and matrixB, each 
	//			between 1 and 5, where matrixA’s number of columns is not equal to 
	//			matrixB’s number of rows.
	//		~ User integer input for every entry of matrixA and matrixB.
	//	Postcondition Q3:
	//		~ An exception (Incompatible matrix dimensions) is thrown.
	//			--> SP1 was unsuccessful (an error occurred)
	//	Precondition P4 - Inputs include:
	//		~ matrixA and matrixB: Globally declared uninitialized static 2D integer arrays
	//		~ User input of any type other than integer
	//	Postcondition Q4:
	//		~ An exception (Input mismatch) is thrown.
	//			--> SP1 was unsuccessful (an error occurred)
	public static void GetMatrixInput () throws Exception {
		
		// Initialize scanner for user input
		Scanner input = new Scanner(System.in); 	
		
		// Prompt user for first matrix's dimensions
		System.out.print("Enter first matrix's dimensions."
						+ "\nRows (between 1 and 5): ");
		int matrixARows = input.nextInt(); 			// Prompt for # of rows	of first matrix					
		System.out.print("Columns (between 1 and 5): ");
		int matrixACols = input.nextInt();			// Prompt for # of columns of first matrix

		// Prompt user for second matrix's dimensions
		System.out.print("\nEnter second matrix's dimensions."
						+ "\nRows (between 1 and 5): ");												
		int matrixBRows = input.nextInt(); 			// Prompt for # of rows	of decond matrix	
		System.out.print("Columns (between 1 and 5): ");
		int matrixBCols = input.nextInt();			// Prompt for # of columns of decond matrix
		
		// Check, if dimension values are invalid (between 1 and 5)
		if (matrixARows < 1 || matrixARows > 5 ||
			matrixACols < 1 || matrixACols > 5 ||
			matrixBRows < 1 || matrixBRows > 5 ||
			matrixBCols < 1 || matrixBCols > 5) {
			
			// Throw exception, if any dimension value is invalid
			throw new Exception ("Incorrect dimension values entered.");
		}
		
		// If dimensions are valid, initialize matrices, else throw an exception
		if (matrixACols == matrixBRows) {
			matrixA = new int[matrixARows][matrixACols]; // Initialize first matrix
			matrixB = new int[matrixBRows][matrixBCols]; // Initialize second matrix
		} else {
			
			// Throw exception
			throw new Exception (
				"Matrix dimensions are incompatible for multiplication.");
		}
		
		// Prompt user for first matrix's entries
		System.out.println("\nEnter first matrix's entries.");
		
		// Iterate the counter i over elements in matrixARows starting from 0
		for (int i = 0; i < matrixARows; i++) {			
			
			// Iterate the counter j over elements in matrixACols starting from 0
			for (int j = 0; j < matrixACols; j++) {		
				System.out.print("Entry " + (i+1) + "x" + (j+1) + ": ");
				matrixA[i][j] = input.nextInt();	// Prompt user for each matrix entry
			}
		}
		
		// Prompt user for second matrix's entries
		System.out.println("\nEnter second matrix's entries.");
		
		// Iterate the counter i over elements in matrixBRows starting from 0
		for (int i = 0; i < matrixBRows; i++) {			
			
			// Iterate the counter j over elements in matrixBCols starting from 0
			for (int j = 0; j < matrixBCols; j++) {		
				System.out.print("Entry " + (i+1) + "x" + (j+1) + ": ");
				matrixB[i][j] = input.nextInt();	// Prompt user for each matrix entry
			}
		}
		// Close input scanner
		input.close();
	}
	
	// Method takes two square matrices as arguments and multiplies them
	// Precondition P5:
	//		~ matrixA: A given 2D integer array with number of 
	//			rows m and number of columns n.
	//		~ matrixB: A given 2D integer array with number of 
	//			rows n and number of columns k.
	// 		~ m, n, k > 0.
	// Postcondition Q5:
	//		~ matrixC: Returns an initialized 2D integer array 
	//			with number of rows m and number of columns k.
	public static int[][] MultiplyMatrices (int[][] matrixA, int[][] matrixB) {

		// Create result matrix with the proper dimensions
		int[][] result = new int[matrixA.length][matrixB[0].length];

		// Use 3 for loops for the calculations:
		// 1. loop: Iterate the counter i over elements in matrixARows starting from 0
		for (int i = 0; i < matrixA.length; i++) {
			
			// 2. loop: Iterate the counter j over elements in matrixBCols starting from 0
			for (int j = 0; j < matrixB[0].length; j++) {
				
				// 3. loop: Iterate the counter k over elements in matrixACols starting from 0
				// Each loop here represents one calculation required to sum up each matrix entry
				for (int k = 0; k < matrixA[0].length; k++) {
					
					// Perform calculations based on given formula for matrix multiplication
					result[i][j] += matrixA[i][k] * matrixB[k][j]; 
				}
			}
		}

		// Return the resulting matrix
		return result;
	}

	// Method displays three given matrices in rectangular form
	// Precondition P6:
	// 		~ matrixA: A given 2D integer array 
	// 		~ matrixB: A given 2D integer array 
	// 		~ matrixC: A given 2D integer array 
	// Postcondition Q6:
	// 		~ matrixA, matrixB, and matrixC have been printed to the console in square matrix format.
	public static void DisplayMatrices (int[][] matrixA, int[][] matrixB, int[][] matrixC) {
		
		// Display first matrix
		System.out.println("\nFirst matrix: ");
		
		// Iterate the counter i over elements (rows) in matrixA starting from 0
		for (int i = 0; i < matrixA.length; i++) {			
			
			// Iterate the counter j over elements (columns) in matrixA starting from 0
			for (int j = 0; j < matrixA[0].length; j++) {	
				System.out.print(matrixA[i][j] + "\t"); 	// Print each entry
			}
			System.out.print("\n");							// Go to next line after each row
		}
		
		// Display second matrix
		System.out.println("Second matrix: ");
		
		// Iterate the counter i over elements (rows) in matrixB starting from 0
		for (int i = 0; i < matrixB.length; i++) {			
			
			// Iterate the counter j over elements (columns) in matrixB starting from 0
			for (int j = 0; j < matrixB[0].length; j++) {	
					System.out.print(matrixB[i][j] + "\t"); // Print each entry
				}
			System.out.print("\n");							// Go to next line after each row
		}
				
		// Display third matrix
		System.out.println("Third matrix obtained by multipliying the first two matrices: ");
		
		// Iterate the counter i over elements (rows) in matrixC starting from 0
		for (int i = 0; i < matrixC.length; i++) {			
		
			// Iterate the counter j over elements (columns) in matrixC starting from 0
			for (int j = 0; j < matrixC[0].length; j++) {	
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
			System.out.println("\n" + ex.toString());
		
		// At the end of the program, let user know it is done
		} finally {
			System.out.println("\nProgram terminated.");
		}
	}
}