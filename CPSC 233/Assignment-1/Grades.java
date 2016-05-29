/* Author: Marc-Andre Fichtel, UCID: 30014709
 * Course: CPSC 233
 * Assignment 1 due January 29, 2016 */ 

import java.util.Scanner;

// Class prompts user for grades, calculates grade statistics, and displays them. */
public class Grades {
	
	public static void main(String[] args) {
		
		String courseCode; 
		Scanner input = new Scanner(System.in);
		
		int gradeInput = 0;				 // Current grade input
		int numOfGradesEntered = 0;		 // Number of grades entered		
		int gradesMax = 0;				 // Highest grade (integer)
		int gradesMin = 0;				 // Lowest grade (integer)
		int gradesSum = 0;				 // Sum of entered grades
		double gradesAverage = 0.0;		 // Grades average (integer)
		String gradesMaxLetter = ""; 	 // Highest grade (letter)
		String gradesMinLetter = "";	 // Lowest grade (letter)
		String gradesAverageLetter = ""; // Grades average (letter)
		
		// Letter grade distribution variables
		int gradeA = 0, gradeAMinus = 0, gradeBPlus = 0, gradeB = 0, 
		gradeBMinus = 0, gradeCPlus = 0, gradeC = 0, gradeCMinus = 0, 
		gradeDPlus = 0, gradeD = 0, gradeF = 0;
		
		// Declare letter grade strings
		String A = "A", AMinus = "A-", BPlus = "B+", B = "B", BMinus = "B-",
				CPlus = "C+", C = "C", CMinus = "C-", DPlus = "D+", D = "D", 
				F = "F";
		
		// Prompt user for course code
		System.out.println("Please enter a course code.");
		courseCode = input.nextLine();
		System.out.println("\n");
		
		/* Prompt user for student grades 
		 * Valid student grades: Any integer between 0 & 100
		 * End input with: -1 or any negative integer */
		while (gradeInput >= 0) {
			System.out.print("Please enter student grades (0 - 100), "
					+ "or -1 to finish input. ");
			gradeInput = input.nextInt();
			
			/* Validate user input
			* Note: Expects an integer input
			* Does not work with other types of input */
			if (gradeInput > 100) {
				System.out.println("Invalid student grade.");
			
			// Let user know, if a sentinel value was entered
			} else if (gradeInput <= -1){
				System.out.println("Entered Sentinel: Done entering grades." + "\n");
			
			} else {
				// Keep track of number of grades entered
				numOfGradesEntered += 1;
				
				// Count letter grades
				if (gradeInput >= 90) {
					gradeA += 1;
				} else if (gradeInput >= 85 && gradeInput < 90) {
					gradeAMinus += 1;
				} else if (gradeInput >= 80 && gradeInput < 85) {
					gradeBPlus += 1;
				} else if (gradeInput >= 75 && gradeInput < 80) {
					gradeB += 1;
				} else if (gradeInput >= 70 && gradeInput < 75) {
					gradeBMinus += 1;
				} else if (gradeInput >= 65 && gradeInput < 70) {
					gradeCPlus += 1;
				} else if (gradeInput >= 60 && gradeInput < 65) {
					gradeC += 1;
				} else if (gradeInput >= 55 && gradeInput < 60) {
					gradeCMinus += 1;
				} else if (gradeInput >= 50 && gradeInput < 55) {
					gradeDPlus += 1;
				} else if (gradeInput >= 45 && gradeInput < 50) {
					gradeD += 1;
				} else {
					gradeF += 1;
				}
				
				
				// Keep track of maximum and minimum grade entered 
				// Assign highest and lowest grade on first input
				if (numOfGradesEntered == 1) {
					gradesMax = gradeInput;
					gradesMin = gradeInput;
				}
				else if (gradeInput > gradesMax) {
					gradesMax = gradeInput;
				} else if (gradeInput < gradesMin) {
					gradesMin = gradeInput;
				}
				
				// Keep track of sum of entered grades
				gradesSum += gradeInput;
				
				// Keep track of average of entered grades as double
				gradesAverage = (double)gradesSum / (double)numOfGradesEntered;
			}
		}
		
		// Convert grade percentages to letter grades
		// 3 loops: Highest grade, Lowest grade, and Average
		for (int i = 0; i < 3; i++) {
			
			int grade;			// Integer holds the grade to be converted
			String gradeLetter; // String holds converted letter grade
			
			// Get integer that should be converted for each loop
			if (i == 0) {
				grade = gradesMax;
			} else if (i == 1) {
				grade = gradesMin;
			} else {
				// Cast average to an integer, since 
				// only the letter equivalent is of interest
				grade = (int)gradesAverage;
			}
			
			if (grade >= 90) {
				gradeLetter = A; 
			} else if (grade >= 85 && grade < 90) {
				gradeLetter = AMinus; 
			} else if (grade >= 80 && grade < 85) {
				gradeLetter = BPlus; 
			} else if (grade >= 75 && grade < 80) {
				gradeLetter = B; 
			} else if (grade >= 70 && grade < 75) {
				gradeLetter = BMinus; 
			} else if (grade >= 65 && grade < 70) {
				gradeLetter = CPlus; 
			} else if (grade >= 60 && grade < 65) {
				gradeLetter = C; 
			} else if (grade >= 55 && grade < 60) {
				gradeLetter = CMinus; 
			} else if (grade >= 50 && grade < 55) {
				gradeLetter = DPlus; 
			} else if (grade >= 45 && grade < 50) {
				gradeLetter = D; 
			} else {
				gradeLetter = F; 
			}
			
			// Set converted letter grade for each loop
			if (i == 0) {
				gradesMaxLetter = gradeLetter;
			} else if (i == 1) {
				gradesMinLetter = gradeLetter;
			} else {
				gradesAverageLetter = gradeLetter;
			}
		}
		
		// After input, display course code and grade statistics
		System.out.println("Course code: " + courseCode + "\n");
		System.out.println("A: " + "\t" + gradeA);
		System.out.println("A-: " + "\t" + gradeAMinus);
		System.out.println("B+: " + "\t" + gradeBPlus);
		System.out.println("B: " + "\t" + gradeB);
		System.out.println("B-: " + "\t" + gradeBMinus);
		System.out.println("C+: " + "\t" + gradeCPlus);
		System.out.println("C: " + "\t" + gradeC);
		System.out.println("C-: " + "\t" + gradeCMinus);
		System.out.println("D+: " + "\t" + gradeDPlus);
		System.out.println("D: " + "\t" + gradeD);
		System.out.println("F: " + "\t" + gradeF + "\n");
		System.out.println("Highest grade: " + "\t\t" + gradesMax + "(" + gradesMaxLetter + ")");
		System.out.println("Lowest grade: " + "\t\t" + gradesMin + "(" + gradesMinLetter + ")");
		System.out.println("Sum of entered grades: " + "\t" + gradesSum);
		System.out.println("# of grades entered: " + "\t" + numOfGradesEntered);
		System.out.println("Average grade: " + "\t\t" + gradesAverage + "(" + gradesAverageLetter + ")");
		
		// Close input scanner
		input.close();
	}
}
