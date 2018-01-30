/**
* Marc-Andre Fichtel
* 30014709
* CPSC 457 - FS 2017
* Assignment 2 - Question 8
*/

#include <iostream>
#include <iomanip>
#include <fstream>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <unistd.h>

using namespace std;

/**
* Program then reads integers from the the specified file, divides them into
* T groups, assigns each to a thread, and displays the sum of each thread and
* the total sum.
*
* Compile with 'g++ -pthread sum.cpp -o sum' to ensure multithreading works.
*/

int T, numsLength;
int nums[1000000];

/**
* Method is assigned to newly created threads. Each thread
* is given an equal amount of numbers to sum up (implemented
* with the modulo operator since the assignment description
* is ambiguous about how threads should be given numbers to sum up).
*
* Note:
* The assignment is also ambigious about whether threads should
* wait for younger threads or not in order to create exactly the
* expected out. This function lets each thread sleep equal to the
* thread's tid. If this is not required, simply commenting this
* line out will create the same output, but threads may activate
* at inopportune times and screw up the output formatting.
*/
void * thread_work(void * tid) {

  // Sleep to ensure correct output formatting
  sleep(*((int*)(&tid)));

  // Sum up numbers corresponding to this thread's tid
  int sum = 0;
  for (int i = 0; i < numsLength; i++) {
    if (nums[i] % T == *((int*)(&tid))) {
      sum += nums[i];
    }
  }

  // Print thread's tid and the sum of its corresponding numbers
  cout << "Thread " << *((int*)(&tid))+1 << ": " << sum << endl;
}

/**
* Program takes two cmd arguments:
* 1) A file name (string)
* 2) A number T of threads to be created (positive integer)
*/
int main (int argc, char *argv[]) {

  // Validate number of cmd args
  if (argc != 3) {
    cerr << "Error: Incorrect number of arguments." << endl;
    return (-1);
  }

  // Get file name and parse number of threads to be created
  string fileName = argv[1];
  T = atoi(argv[2]);

  // Validate number of threads
  if (T == 0) {
    cerr << "Error: Second argument must be a positive integer." << endl;
    return (-1);
  }

  // Open input file and validate
  ifstream inputFile(fileName.c_str());
  if (!inputFile) {
    cerr << "Error: Unable to open " << fileName << "." << endl;
    return (-1);
  }

  // Read numbers in input file into array of integers
  string line;
  while (getline (inputFile, line)) {
    nums[numsLength] = atoi(line.c_str());
    numsLength++;
  }
  inputFile.close();

  // Create T threads
  pthread_t threads[T];
  long status;
  for (int i = 0; i < T; i++) {
    status = pthread_create (&threads[i], NULL, thread_work, (void *)i);
    if (status != 0) {
      cerr << "Error: Creating thread " << i << " failed." << endl;
      return (-1);
    }
  }

  // Wait for threads and clean up
  for (int i = 0; i < T; i++) {
    pthread_join(threads[i], NULL);
  }

  // Print total sum
  int total = 0;
  for (int i = 0; i < numsLength; i++) {
    total += nums[i];
  }
  cout << "Sum = " << total << endl;

  // Return success
  return (0);
}
