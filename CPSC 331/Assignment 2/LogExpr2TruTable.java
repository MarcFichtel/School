import java.util.ArrayList;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

// Class represents a program that prompts the user for a 
// 	logical expression, analyzes it's validity, and then prints 
// 	a truth table for it to the screen.

// References:
// 	~ Check, if a character is valid or not: 
// 		--> https://docs.oracle.com/javase/tutorial/i18n/text/charintro.html
// 	~ Check, how to find the second occurance of a char in a string
// 		--> http://stackoverflow.com/questions/19035893/finding-second-occurrence-of-a-substring-in-a-string-in-java
// 	~ Check truth table representation with binary values, was used as a 
// 		help to figuring out how to fill independent variable columns in 
// 		the truth table with truth values
// 		--> http://www.ee.surrey.ac.uk/Projects/Labview/gatesfunc/TruthFrameSet.htm
// 	~ Convert boolean expressions in strings to boolean values
// 		--> http://stackoverflow.com/questions/31811315/how-to-convert-a-string-to-boolean-expression

public class LogExpr2TruTable {
	
	// Initialize global list of chars for independent expression variables
	private static ArrayList<Character> exprVars = 
			new ArrayList<Character>();
	
	// Initialize global list of strings for subexpressions
	private static ArrayList<String> exprSubs = 
			new ArrayList<String>();
	
	// Method prompts user for a logical expression, checks its validity, 
	//	and updates the two global lists
	// Precondition P1:
	// 	~ Global lists for independent expression variables and 
	//		subexpressions exist
	// 	~ User enters a valid logical expression
	// Postcondition Q1:
	// 	~ Global lists for independent expression variables and 
	//		subexpressions contain valid information
	// Precondition P2:
	// 	~ Global lists for independent expression variables and 
	//		subexpressions exist
	// 	~ User enters an invalid logical expression
	// Postcondition Q2: 
	// 	~ An exception (Invalid Logical Expression) is thrown
	public static void GetExpr () throws Exception {
		
		// Initialize a scanner for user input
		Scanner input = new Scanner (System.in);
		
		// Initialize integer to track bracket usage in the expression
		int brackets = 0;
		
		// Inform user of logical expression rules
		System.out.println("Enter a logical expression where"
				+ "\n * stands for AND,"
				+ "\n + stands for OR,"
				+ "\n - stands for NOT,"
				+ "\n and brackets ( and ) match: ");
		
		// Prompt user for a logical expression
		String logExpr = input.nextLine();
		
		// Close input scanner
		input.close();
		
		// Check, if input is empty
		if (logExpr.length() == 0) {
			
			// If so, throw an exception
			throw new Exception ("The given expression is empty.");
		}
		
		// Convert logical expression string to an array of chars
		char[] logExprChars = logExpr.toCharArray();
		
		// For every element in the expression, check if its valid
		// Throw an exception, if it is invalid
		
		// If the element is a variable, add it to its proper location
		// 	in the sorted list of independent expression variables
		// Use enhanced for loop to iterate over each char in logExprChars
		for (int i = 0; i < logExprChars.length; i++) {
			
			// Check, if character is a letter from the alphabet (a variable)
			if ((logExprChars[i] >= 'a' && logExprChars[i] <= 'z') || 
				(logExprChars[i] >= 'A' && logExprChars[i] <= 'Z')) {
				
				// If a variable is not followed by an operator (except NOT) 
				// 	or closing bracket, and isn't the last one, throw an exception
				// Note that an expression like (A) is legal 
				if (i != logExprChars.length - 1 &&
					logExprChars[i + 1] != '*' &&
					logExprChars[i + 1] != '+' &&
					logExprChars[i + 1] != ')') {
					
					// Throw an exception
					throw new Exception ("Faulty expression syntax: A variable "
							+ "must be followed by *, +, or ).");
				}
				
				// Check, if exprVars is empty
				if (exprVars.isEmpty()) {
					exprVars.add(logExprChars[i]);	// If so, simply add the character
				
				// Else, compare elements in exprVars with the new character
				} else {
					
					// Iterate over each element in exprVars with a counter x starting at 0
					for (int x = 0; x < exprVars.size(); x++) {
						
						// Check if char has already been noted as a var
						if (logExprChars[i] == exprVars.get(x)) {
							
							// If already present, continue to next char
							break;		// Check next var
			
						// Check if char is smaller than (i.e. comes before) var
						} else if (logExprChars[i] < exprVars.get(x)) {
							
							// If found, insert char at var's location, which 
							// 	pushes var to next spot
							exprVars.add(x, logExprChars[i]);
							break; 			// After insertion, check next char
						
						// If no larger var was found, add char to end of the list
						// Conditional checks, if for loop is currently looking at
						// 	the last var in exprVars
						} else if (x == exprVars.size() - 1) {
							exprVars.add(logExprChars[i]); 	// Add char to end of list
							break; 			// After insertion, check next char
						}
					}
				}
			
			// Check, if character is an opening or closing bracket
			} else if (logExprChars[i] == '(' || logExprChars[i] == ')') {
				
				// Do the following for opening brackets
				if (logExprChars[i] == '(') {
					
					// Check for syntax errors if this isn't the last char (i.e. if an opening bracket
					// 	is not followed by a variable, another opening bracket, or NOT)
					if (i != logExprChars.length - 1 &&
						!((logExprChars[i + 1] >= 'a' && logExprChars[i + 1] <= 'z') || 
						(logExprChars[i + 1] >= 'A' && logExprChars[i + 1] <= 'Z') ||
						logExprChars[i + 1] == '-' || logExprChars[i + 1] == '(')) {
						
						// Throw an exception
						throw new Exception ("Faulty expression syntax: An opening "
								+ "bracket must be followed by a variable or NOT.");
					} 
					brackets += 1; 	// Opening bracket: Increment brackets counter
				
				// Do the following for closing brackets
				} else {
					
					// Check for syntax errors (i.e. if a closing bracket
					// 	is not followed by *, +, or another closing bracket)
					if (i != logExprChars.length - 1 &&
						logExprChars[i + 1] != '*' &&
						logExprChars[i + 1] != '+' &&
						logExprChars[i + 1] != ')') {
						throw new Exception ("Faulty expression syntax: A closing "
								+ "bracket must be followed by *, +, or ).");
					}
			
					brackets -= 1;	// Closing bracket: Decrement brackets counter
					
					// Check, if counter is negative 
					//	(i.e. closing brackets placed before opening brackets)
					if (brackets < 0) {
						
						// Throw an exception
						throw new Exception ("Invalid bracket placement.");
					
					// Add the current subexpression to exprSubs whenever 
					// 	a closing bracket occurs
					} else {
						
						// Note index of closing bracket (end of subexpression)
						int end = i + 1;
						
						// Declare integer for subexpression start index
						int start = 0;
						
						// Create a local counter = 1
						int counter = 1;
						
						/* 
						* Use for loop to check leftwards for any brackets with 
						* 	loop counter starting at the current index
						* Increment counter on closing bracket,
						* Decrement counter on opening bracket
						* If counter equals 0, note the index (start of subexpression)
						*/
						for (int j = i - 1; j >= 0; j--) {
							
							// Check for closing brackets
							if (logExprChars[j] == ')') {
								counter++; 	// Increment counter
							
							// Check for closing brackets
							} else if (logExprChars[j] == '(') {
								counter--; 	// Decrement counter
								
								// Check, if counter equals 0
								if (counter == 0) {
									start = j; 	// If so, note starting index
									
									// Isolate the subexpression
									String subexp = logExpr.substring(start, end);
									
									// Add subexpression, if it is not a single variable
									// 	or the complete expression
									if (subexp != logExpr && subexp.length() > 3) {
										exprSubs.add(subexp); 	// Add subexpression
									}
									break; 		// Stop iterating leftwards if current 
												// subexpression is complete
								}
							}
						}
					}
				}
				
			// Do the following for operators
			} else if  (logExprChars[i] == '*' || 
						logExprChars[i] == '+' || 
						logExprChars[i] == '-') {
				
				// AND and OR cannot be at the start of the expression
				if (i == 0 && logExprChars[i] != '-') {
					throw new Exception ("Faulty expression syntax: AND/OR operator is in first place.");
				
				// No operator can be at the end of the expression
				} else if (i == logExprChars.length - 1) {
					throw new Exception ("Faulty expression syntax: Operator is in last place.");
				
				// AND and OR cannot be preceded by an opening bracket
				} else if (i != 0 && 
						logExprChars[i - 1] == '(' && 
						logExprChars[i] != '-') {
					throw new Exception ("Faulty expression syntax: AND/OR operator cannot be preceded by an opening bracket.");
				
				// Operators cannot be followed by AND/OR, or closing brackets
				} else if (logExprChars[i + 1] == ')' || 
						logExprChars[i + 1] == '*' ||
						logExprChars[i + 1] == '+') {
					throw new Exception ("Faulty expression syntax: Operator cannot be followed by an operator or closing bracket.");
				}
				
			// If the element is not a variable, bracket, or operator, it is invalid
			} else {
				// Throw an exception
				throw new Exception ("Invalid character entered.");
			}
		}
		
		// Check, if brackets counter is positive after all brackets have been 
		// 	accounted for (i.e. not enough closing brackets)
		if (brackets > 0) {
			
			// Throw an exception
			throw new Exception ("Not enough closing brackets.");
		}
		
		// If the expression is valid and longer than one variable, 
		// 	add the complete expression to exprSubs
		if (logExpr.length() > 1) {
			exprSubs.add(logExpr);
		}
	}
	
	// Method creates a truth table from the given expression variables 
	// 	and subexpressions
	// Precondition P:
	// 	~ 
	// Postcondition Q:
	// 	~
	public static String[][] CreateTruthTable (
								ArrayList<Character> exprVars, 
								ArrayList<String> exprSubs) throws Exception {
		
		// Get # of rows 2^x rows, where x is # of variables + 1 for header row
		int rows = (int) (Math.pow(2, exprVars.size()) + 1);
		
		// Create a 2D String array with the required dimensions
		String[][] truthTable = new String
				
				// Give table the proper # of rows
				[rows] 
				
				// Tables has columns = # of variables + # of subexpressions
				[exprVars.size() + exprSubs.size()];
		
		// Add all variables and subexpressions to first row by
		// 	iterating over a counter i starting at 0
		for (int i = 0; i < truthTable.length; i++) {
			
			// Add independent variables first
			if (i < exprVars.size()) {
				truthTable[0][i] = exprVars.get(i).toString(); 	// Add variables
			
			// Then add subexpressions, if there are any
			// Subtract # of variables from i to iterate through subexpressions
			} else if (!exprSubs.isEmpty() && 
					i - exprVars.size() < exprSubs.size()) {
				truthTable[0][i] = exprSubs.get(i - exprVars.size()); // Add subexpression
			}
		}
		
		// Add truth values for the variable columns
		// Iterate over rows below header with counter i starting at 1
		for (int i = 1; i < rows; i++) {
			
			// Get the binary representation of the row indices
			String rowToBinary = Integer.toBinaryString(i - 1);
			
			// Check, if the binary string is shorter than the number of columns
			if (rowToBinary.length() < exprVars.size()) {
				
				// If so, note the length of the binary string
				int x = rowToBinary.length();
				
				// Prepend zeros to the binary string until its length is 
				// 	equal to the number of columns
				// Iterate over each variable column with 
				// 	a counter j starting at 0
				for (int j = 0; j < exprVars.size() - x; j++) {
					rowToBinary = "0" + rowToBinary; // Prepend 0 to the string
				}
			}
			
			// Iterate over columns with counter j starting at 0
			for (int j = 0; j < exprVars.size(); j++) {
				
				// Insert a truth value into the table depending on the
				// 	binary string value corresponding to each cell
				// Insert True for 0, False for 1
				if (rowToBinary.charAt(j) == '0') {
					truthTable[i][j] = "true";
				} else {
					truthTable[i][j] = "false";
				}
			}
		}	
		
		// Add truth values for each subexpression
		// Iterate over columns with counter i starting after the variable columns
		for (int i = exprVars.size(); i < truthTable[0].length; i++) {

			// Iterate over rows
			for (int r = 1; r < rows; r++) {
			
				String valueStr = ""; 	// Create string to hold the boolean expression
				
				// Add each character's logic meaning to the string representation of
				// 	the boolean expression to be evaluated
				// Iterate through subexpression characters with counter k starting at 0
				for (int j = 0; j < truthTable[0][i].length(); j++) {
					
					// Look for opening brackets
					if (truthTable[0][i].charAt(j) == '(') {
						valueStr = valueStr + "("; 	// Add bracket to string
						
					// Look for closing brackets
					} else if (truthTable[0][i].charAt(j) == ')') {
						valueStr = valueStr + ")"; 	// Add bracket to string
					
					// Look for AND operators
					} else if (truthTable[0][i].charAt(j) == '*') {
						valueStr = valueStr + "&&"; 	// Add AND to string
						
					// Look for OR operators
					} else if (truthTable[0][i].charAt(j) == '+') {
						valueStr = valueStr + "||"; 	// Add OR to string
					
					// Look for NOT operators
					} else if (truthTable[0][i].charAt(j) == '-') {
						valueStr = valueStr + "!"; 	// Add NOT to string
					
					// All other characters are independent variables
					} else {
						
						// Iterate over independent variable columns until
						// 	the current character has been matched in the header
						for (int k = 0; k < exprVars.size(); k++) {
							
							// Match variable to column
							if (truthTable[0][i].charAt(j) == truthTable[0][k].charAt(0)) {
								valueStr = valueStr + 	// Add variable's truth value
										truthTable[r][k];
								break;					// Break loop if match was found
							}
						}
					}
				}
				
				// After all characters have been accounted for, convert String to boolean,
				// 	and insert it into truth table
				// Use a scripting engine to convert the string expression to a boolean,
				// and evaluate it
				ScriptEngineManager mgr = new ScriptEngineManager();
			    ScriptEngine engine = mgr.getEngineByName("JavaScript");
				
			    Boolean valueBool = null; 	// Create a boolean for the result of
											// 	the evaluated expression
					
				// Convert the string expression to a boolean, and evaluate it
				valueBool = Boolean.valueOf((boolean) engine.eval(valueStr));
				
				// Insert boolean into the appropriate truth table cell as a string
				truthTable[r][i] = valueBool.toString();
			}
		}
		
		return truthTable;
	}
	
	// Method displays a given truth table (2D String array) to the screen
	// Precondition P:
	// 	~ 
	// Postcondition Q:
	// 	~
	public static void DisplayTruthTable (String[][] truthTable) {
		
		// Iterate over the truth table's rows counter i starting at 0
		for (int i = 0; i < truthTable.length; i++) {
			
			// Iterate over truth table's columns with counter j starting at 0
			for (int j = 0; j < truthTable[i].length; j++) {
				
				// Print each cell's content 
				System.out.print(truthTable[i][j] + "\t");
			}
			// Print a newline to keep table format
			System.out.println("");
		}
	}
	
	
	public static void main(String[] args) {
		
		// Use try/catch to detect any errors
		try {
			// Prompt user for a logical expression and validate it
			GetExpr ();
			
			// Create a truth table from the expression's independent
			// 	variables and subexpressions
			String[][] truthTable = CreateTruthTable (exprVars, exprSubs);
			
			// Display the truth table to the screen
			DisplayTruthTable (truthTable);
		
		// Catch any exceptions
		} catch (Exception ex) {
			
			// Print out the exception to let user know what went wrong
			System.out.println("\nError: " + ex.toString());
		
		// Inform user that program is done
		} finally {
			System.out.println("\nProgram terminated.");
		}
	}

}
