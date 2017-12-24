/**********************************************
 * Last Name:   Fichtel
 * First Name:  Marc-Andre
 * Student ID:  30014709
 * Course:      CPSC 457
 * Tutorial:    01
 * Assignment:  1
 * Question:    Q4
 *
 * File name:   myWC.cpp
 *********************************************/

#include <unistd.h>
#include <string>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <fstream>

using namespace std;

int main (int argc, char *const argv[]) {

  // Get file name from command line
  string filename;                                      // Declare string variable to hold filename
  if (argc != 2) {                                      // If command doesn't specify exactly one input file
    cerr << "Usage: readFile <input file> " << endl;    // Display an error
    return -1;                                          // Return -1 (error code)
  }
  else {
    filename = argv[1];                                 // Else get filename string
  }

  // Open file for reading
  ifstream fd (filename.c_str());                       // Open file
  if (fd < 0) {                                         // If there is an error
    cerr << "Could not open file " << filename << "\n"; // Display it and
    exit (-1);                                          // Return -1 (error code)
  }

  // Read file line by line
  int count = 0;                            // Declare int variable for line count
  string line;                              // Declare string variable for current line
  while (getline(fd, line)) {               // While another line can be read
    count++;                                // Increase line count
  }

  // Report results
  cout << count << " " << filename << "\n"; // Display line count and filename
  return 0;                                 // Return 0 (program terminated normally)
}
