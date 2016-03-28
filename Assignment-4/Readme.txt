Student: Marc-Andre Fichtel
Course: CPSC 233, Assignment 4
University of Calgary
Tutorial 05
Instructor: Edward Chan 

README
This document explains how to compile the assignment 4 source code and how to run the program.

1.) Using Eclipse
	* Download the source code (The .java files are located in the folder MortgageCalculator)
	* Open Eclipse
	* Create a new project with an arbitrary name
	* In the Eclipse Package Explorer, navigate to the src folder of the new project
	* Open the context menu for the src folder, click "Import..."
	* In the Import Wizard, choose "File System" as an import source, then click "Next"
	* Search for and select the MortgageCalculator folder containing the source code
	* Under Options, select "Create top-level folder", then click "Finish"
	* In the Eclipse Package Explorer, navigate to MCController.java, open the context menu
	* Click "Run As", then select "Java Application", and the program will start
2.) Using Command Prompt
	* Download the source code (The .java files are located in the folder MortgageCalculator)
	* Open Command Prompt
	* Navigate to where the MortageCalculator folder is located
	* Use command "javac MortgageCalculator/*.java" to compile the program
	* Use command "java MortgageCalculator/MCController" (the file containing the 'main' method) to run the program