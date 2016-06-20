import java.util.Scanner;
import java.util.Stack;

// CPSC 331 - Assignment 3
// Student: Marc-Andre Fichtel
// Due: June 20, 11pm

// References:
// 	~ Calculation of a postfix expression with stacks
// 		--> User "Scarl" on stackexchange.com
// 			Accessed June 20, 2016
// 			URL: http://codereview.stackexchange.com/questions/35750/postfix-evaluation-using-a-stack

// Class represents a program that prompts a user for an arithmetic 
// 	expression in post fix format, computes the result, and then 
//  displays it to the user
public class PostfixArithmetic {

	// Method prompts user for an arithmetic expression in post fix format
	// Note that this method assumes a valid expression is entered
	// Precondition P1:
	// 	~ The user gives an arithmetic expression in valid post fix notation
	// Postcondition Q1:
	// 	~ A stack of strings with an arithmetic expression in valid post fix 
	// 		notation is returned
	public static Stack<String> GetExpr () {
		
		// Create a scanner for user input
		Scanner input = new Scanner (System.in);
		
		// Create a stack of strings to hold user input
		// Note that a stack of strings is required, since operators are not numbers
		Stack<String> expr = new Stack<String>();
		
		// Instruct user, and get the first input
		System.out.print("Enter the post fix expression one element at a time, "
				+ "\nor 0 to get the result of the expression entered so far."
				+ "\n\nEnter first expression element: ");
		expr.push(input.nextLine()); // Push user input onto stack
		
		// Keep prompting user for expression elements 
		// until termination symbol 0 is entered
		while (expr.peek().charAt(0) != '0') {
			System.out.print("Enter next expression element: ");
			expr.push(input.nextLine()); // Push user input onto stack
		}
		
		// Close the input scanner
		input.close();
		
		// Remove the termination symbol from the stack
		expr.pop();
		
		// Return the expression
		return expr;
	}
	
	// Method takes a stack of strings containing an arithmetic expression 
	// 	in post fix notation, and computes its result
	// Precondition P2:
	// 	~ A stack of strings with an arithmetic expression in valid post fix 
	// 		notation is given
	// Postcondition Q2:
	// 	~ A double with the correct result of the arithmetic post fix expression 
	// 		stored in the stack of strings is returned
	public static double ComputeExpr (Stack<String> expr) {
		
		// Initialize variable to hold the result
		double result = 0.0;
		
		// Create a temporary string to hold the expression
		String exprString = "";
		
		// Push all elements from stack into temporary string
		while (!expr.isEmpty()) {
			
			// Insert next expression element at the start of the string
			exprString = expr.pop() + exprString;
		
		}

		// Create a new stack to calculate the expression's result
		Stack <String> s = new Stack <String> ();
		
		// Create helper variables to calculate subexpressions
		double x;
	    double y;

	    // Iterate over each element in the expression string
	    for (int i = 0; i < exprString.length(); i++) {

	    	// Get the element the for loop is currently looking at
	    	char element = exprString.charAt(i);
	    	
	    	// Ignore empty spaces
	        if (element != ' ') {
	        
	        	// Check if the element is a number
	        	if (element > '0' && element < '9') {
	                
	        		// Push numbers onto the helper stack
	        		s.push(Character.toString(element));
	        		
	            // All other elements must be operators
	        	} else {
	                
	        		// Pop the previous two numbers into the helper double variables
	        		x = Double.parseDouble("" + s.pop());
	                y = Double.parseDouble("" + s.pop());

	                // Apply operator to previous two numbers in reverse order
	                // 	(since the stack will pop the numbers out in reverse order)
	                // Check, if the operator is a plus
	                if (element == '+') {
	                	
	                	// Apply operator to previous two numbers, and push 
	                	// 	result to the helper stack
	                	s.push(Double.toString(y + x));
	                
	                // Check, if the operator is a minus
	                } else if (element == '-') {
	                	
	                	// Apply operator to previous two numbers, and push 
	                	// 	result to the helper stack
	                	s.push(Double.toString(y - x));
	                
	                // Check, if the operator is a multiplication
	                } else if (element == '*') {
	                	
	                	// Apply operator to previous two numbers, and push 
	                	// 	result to the helper stack
	                	s.push(Double.toString(y * x));
	                
	                // Check, if the operator is division
	                } else {
	                	
	                	// Apply operator to previous two numbers, and push 
	                	// 	result to the helper stack
	                	s.push(Double.toString(y / x));
	                }
	            }
	        }
	    }
		
	    // The remaining number in the helper stack is the result
	    // Parse it to a double, and return it
	    // Note: This line could be left out, and the stack's only entry returned returned instead
	    result = Double.parseDouble("" + s.pop());
		
		// Return the result
		return result;
	}
	
	// Method takes a number and prints it to the screen
	// Precondition P3:
	// 	~ A double variable is given
	// Postcondition Q3:
	// 	~ The double variable has been printed to the screen
	public static void DisplayResult (double result) {
		
		// Print out the given number (the result of the post fix expression)
		System.out.println("\nResult: " + result);
	}
	
	// Main method executes the program
	public static void main (String[] args) {
		
		// Create a new stack of strings by calling GetExpr()
		Stack<String> expr = GetExpr();
		
		// Create a new double by calling ComputeExpr with the stack from GetExpr()
		double result = ComputeExpr(expr);
		
		// Print the number to the screen
		DisplayResult(result);
	}
}