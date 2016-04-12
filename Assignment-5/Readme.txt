Student: Marc-Andre Fichtel
Course: CPSC 233, Assignment 5
University of Calgary
Tutorial 05
Instructor: Edwin Chan 

README
This document explains how to compile the assignment 5 source code and how to run the program.

1.) Using Eclipse
	* Download & extract the zip folder containing the source code (.java files are located in the folder GreenhouseSimulator)
	* Open Eclipse
	* Create a new project with an arbitrary name
	* In the Eclipse Package Explorer, navigate to the src folder of the new project
	* Open the context menu for the src folder, click "Import..."
	* In the Import Wizard, choose "File System" as an import source, then click "Next"
	* Search for and select the GreenhouseSimulator folder containing the source code
	* Under Options, select "Create top-level folder", then click "Finish"
	* Move the image files (red.gif and green.jpg) into the project's project folder (i.e. the folder containing the "src" folder)
	* In the Eclipse Package Explorer, navigate to Controller.java, open the context menu
	* Click "Run As", then select "Java Application", and the program will start
2.) Using Command Prompt
	* Download & extract the zip folder containing the source code (.java files are located in the folder GreenhouseSimulator)
	* Move the image files (red.gif and green.jpg) into the folder containing the GreenhouseSimulator folder
	* Open Command Prompt
	* Navigate to where the GreenhouseSimulator folder is located
	* Use command "javac GreenhouseSimulator/* to compile the program
	* Use command "java GreenhouseSimulator/Controller" (the file containing the 'main' method) to run the program