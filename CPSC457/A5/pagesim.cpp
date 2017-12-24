/*
 * pagesim.cpp
 * Compile with:
 *
 * Student Name: Marc-Andre Fichtel
 * Student Number: 30014709
 *
 * Class: CPSC 457 Spring 2017
 * Instructor: Pavol Federl
 */

 #include <iostream>
 #include <fstream>
 #include <sstream>
 #include <stdlib.h>
 #include <algorithm>
 #include <list>
 #include <string>
 #include <cstring>
 #include <sstream>
 #include <deque>
 #include <queue>

using namespace std;

int numFrames = 0;  // The number of frames
list<int> pageRef;  // The list of page reference numbers
int pageFaults = 0; // The number of page faults

int main (int argc, char * const argv[]) {

  ///////////////////////////////////////////////////////////
  // PARSING ////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////

  // Validate command line arguments
  if (argc < 2) {
    cout << "Error. Usage: pagesim <int> (# of frames) [<int>] (page reference string)";
    return 0;
  }

  // Parse numFrames
  numFrames = atoi(argv[1]);

  // If argc == 2, it is assumed that the page reference string is passed via file redirection
  if (argc == 2) {
    string x;

    // Read string from file
    while (getline(cin, x)) {
      cin >> x;
    }

    // Convert each number to an int and add it to the list of page references
    for (int i = 0; i < x.size(); i++) {
      if (x[i] != ' ') pageRef.push_back(x[i] - '0');
    }

  // Parse page reference string directly from command line
  } else {
    for (int i = 2; i < argc; i++) {
      pageRef.push_back(atoi(argv[i]));
    }
  }

  ///////////////////////////////////////////////////////////
  // DONE PARSING ///////////////////////////////////////////
  ///////////////////////////////////////////////////////////
  // LRU ////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////

  // Create array to track frame contents
  int frames[numFrames];
  queue<int> count;

  // Initialize frames
  for (int i = 0; i < numFrames; ++i) {
    frames[i] = -1;
  }

  // Iterate over page reference numbers
  for (list<int>::const_iterator it = pageRef.begin(), end = pageRef.end(); it != end; ++it) {
    bool done = false;

    // Iterate over frames
    for (int i = 0; i < numFrames; ++i) {

      // If frame is empty, insert page, track page fault, and go to next page reference
      if (frames[i] == -1 && !done) {
        frames[i] = *it;    // Load new page reference into frame
        pageFaults++;       // Track page fault
        done = true;        // Mark this page reference as done
        count.push(*it);    // Track new reference
        break;              // Done (Go to next reference)

      // If frame already contains reference, just change LRU
      } else if (frames[i] == *it) {

        // Reorder LRU queue
        int x = count.size();           // Iterate over queue
        for (int j = 0; j < x; j++) {
          int z = count.front();        // Get front
          count.pop();                  // Then pop front
          if (z != *it) count.push(z);  // Push it back in if its not ref
        }
        count.push(*it);                // Push ref after rest was reordered

        // Done (Go to next reference)
        break;
      }

      // All frames are full and none contains reference
      if (i == numFrames-1 && !done) {

        // Iterate over frames and switch out the least recently used one
        for (int i = 0; i < numFrames; ++i) {
          if (frames[i] == count.front()) {
            frames[i] = *it;  // Load new page reference into frame
            pageFaults++;     // Track page fault
            count.push(*it);  // Track new reference
            count.pop();      // Remove LRU
            break;            // Done (Go to next reference)
          }
        }
      }
    }
  }

  cout << "LRU:" << endl;
  cout << "- Frames: ";
  for (int i = 0; i < numFrames; i++) {
    cout << frames[i];
  }
  cout << endl;
  cout << "- Page Faults: " << pageFaults << endl;

  ///////////////////////////////////////////////////////////
  // DONE LRU ///////////////////////////////////////////////
  ///////////////////////////////////////////////////////////
  // OPT ////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////

  // Reset frames & page faults
  for (int i = 0; i < numFrames; ++i) {
    frames[i] = -1;
  }
  pageFaults = 0;

  // Create variable to track what reference in sequence in being looked at
  int ref = -1;

  // Iterate over page reference numbers
  for (list<int>::const_iterator it = pageRef.begin(), end = pageRef.end(); it != end; ++it) {
    ref++;
    bool done = false;

    // Iterate over frames
    for (int i = 0; i < numFrames; ++i) {

      // If frame is empty, insert page, track page fault, and go to next page reference
      if (frames[i] == -1 && !done) {
        frames[i] = *it;    // Load new page reference into frame
        pageFaults++;       // Track page fault
        done = true;        // Mark this page reference as done
        break;              // Done (Go to next reference)

      // If frame already contains reference, do nothing
      } else if (frames[i] == *it) {
        done = true;
        break;
      }

      // All frames are full and none contains reference, so find OPT
      if (i == numFrames-1 && !done) {

        // If this is the last reference, switch into first frame
        if (ref == pageRef.size()-1) {
          frames[0] = *it;  // Load new page reference into frame
          pageFaults++;     // Track page fault
          break;            // Done (Go to next reference)
        }

        // Create variables to track OPT frame
        int f = 0;
        int counter = 0;
        int refsToLookAt = pageRef.size() - ref - 1;

        // Iterate over numbers left in sequence
        for (int j = 0; j < refsToLookAt-1; j++) {
          ++it;
          ++counter;

          // Iterate over frames
          for (int k = 0; k < numFrames; k++) {

            // If a frame contains next num in sequence, track its location
            if (frames[k] == *it) {
              f = k;
              break;
            }
          }

          // If the next numFrames frames have been checked, OPT is done
          if (counter == numFrames) {
            break;
          }
        }

        // Reset it
        for (int j = 0; j < counter; j++) {
          --it;
        }

        frames[f] = *it;  // Load new page reference into frame
        pageFaults++;     // Track page fault
        break;            // Done (Go to next reference)
      }
    }
  }

  cout << "OPT:" << endl;
  cout << "- Frames: ";
  for (int i = 0; i < numFrames; i++) {
    cout << frames[i];
  }
  cout << endl;
  cout << "- Page Faults: " << pageFaults << endl;

  ///////////////////////////////////////////////////////////
  // DONE OPT ///////////////////////////////////////////////
  ///////////////////////////////////////////////////////////
  // CLOCK //////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////

  // TODO

  // I did not understand the CLOCK algorithm well enough to
  // implement it. Also, it is 10:45pm and I still need to
  // start Question 7. So I am stopping here in favor of
  // working on assignment question 7.
}
