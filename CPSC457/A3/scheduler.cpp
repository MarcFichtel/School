/**
* Marc-Andre Fichtel
* 30014709
* CPSC 457 - FS 2017
* Assignment 3 - Question 7
*
* Simulates a task scheduler with either a Round Robin or Shortest Job First
*   (or Shortest Remaining Time Next) algorithm
*
* compile with:
*   $ g++ scheduler.cpp -o scheduler
*
* run with:
*   $ ./scheduler <input file> <scheduling algorithm> <RR time quantum>
*   where the scheduling algorithm can be either "RR" or "SJF" or "SRTN"
*/
#include <algorithm>
#include <fstream>
#include <iostream>
#include <iterator>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <queue>

using namespace std;

// Number of jobs
int numJobs;

// Create job data structure
struct job {
  int id;               // Each job has a unique ID
  int arrivalTime;      // Each job has an arrival time (given)
  int burstLength;      // Each job has a burst length (given)
  int runtime;          // How long has this job already run
  int runtimeQuantum;   // How long has this job already run in regards to the quantum
  int waiting;          // How long has this job waited
  bool complete;        // Is this job complete?
};

/**
* use an RR scheduling algorithm with a given quantum to process a given set of jobs
* Arg1: an array of jobs
* Arg2: the quantum used for preemption
*/
void processRR (job jobs[], int quantum) {

  // Create a queue of jobs
  queue <job> q;

  // Create a string array for formatting purposes (each jobs current status)
  string jobStatus[numJobs];

  // Count total number of steps needed (sum of bursts)
  int totalTime = 0;
  for (int i = 0; i < numJobs; i++) {
    totalTime += jobs[i].burstLength;
  }

  // Iterate over each time step
  for (int t = 0; t <= totalTime; t++) {

    // Add new jobs to the queue when they arrive
    for (int j = 0; j < numJobs; j++) {
      if (jobs[j].arrivalTime == t) {
        q.push(jobs[j]);

        // Initialize job
        jobs[j].runtime = 0;
        jobs[j].runtimeQuantum = 0;
        jobs[j].complete = false;
        jobs[j].waiting = 0;
      }
    }

    // If running job is done, delete it from the queue and mark it as done
    for (int j = 0; j < numJobs; j++) {
      if (!q.empty() &&
          q.front().id == jobs[j].id &&
          jobs[j].runtime >= jobs[j].burstLength) {
        q.pop();                  // Delete from queue
        jobs[j].complete = true;  // Mark job as completed
        jobStatus[j] = "";        // Done jobs aren't displayed in the output
      }
    }

    // If job exceeds quantum and isn't done yet, move to back of queue
    for (int j = 0; j < numJobs; j++) {
      if (q.front().id == jobs[j].id &&
          jobs[j].runtimeQuantum >= quantum) {
        jobs[j].runtimeQuantum = 0;   // Reset quantum runtime of this job
        q.push(q.front());            // Reinsert job into back of queue
        q.pop();                      // Remove job from front o queue
      }
    }

    // Ready jobs display a "+" (job has started, but not yet finished)
    for (int j = 0; j < numJobs; j++) {
      if (t >= jobs[j].arrivalTime &&
          !jobs[j].complete &&
          jobs[j].id != q.front().id) {
        jobStatus[j] = "+";   // Waiting jobs are displayed with a "+"
        jobs[j].waiting++;    // Track one time unit of waiting for this job
      }
    }

    // If job is at front of queue, hasn't exceeded the quantum, and isn't complete, display a "."
    for (int j = 0; j < numJobs; j++) {
      if (!q.empty() &&
          q.front().id == jobs[j].id &&
          jobs[j].runtimeQuantum < quantum &&
          jobs[j].runtime < jobs[j].burstLength) {
        jobs[j].runtime++;        // Track one time unit of run time for this job
        jobs[j].runtimeQuantum++; // Track run time for quantum comparison
        jobStatus[j] = ".";       // Running jobs are displayed with a "."
      }
    }

    // Display row
    cout << t << "\t";                    // Display time step
    for (int j = 0; j < numJobs; j++) {
      cout << jobStatus[j] << "\t";       // Display each job's status
    }
    cout << endl;
  }
}

/**
* Use a RJF scheduling algorithm to process a given set of jobs
* Arg1: an array of jobs
*/
void processSJF (job jobs[]) {

  // Index of job with shortest run time
  int jobToRun = -1;

  // Create a string array for formatting purposes (each jobs current status)
  string jobStatus[numJobs];

  // Count total number of steps needed (sum of bursts)
  int totalTime = 0;
  for (int i = 0; i < numJobs; i++) {
    totalTime += jobs[i].burstLength;
  }

  // Iterate over each time step
  for (int t = 0; t <= totalTime; t++) {

    /**
    * If running job is done, delete it from the queue and mark it as done
    *
    * Note:
    * This bit is likely more complicated than it needs to be
    * When a job is done, the next shortest job has to be found
    * I do this by first finding the next non-complete job, and comparing its
    *   runtime with that of all subsequent non-complete jobs. If there is a
    *   tie between the remaining length of two or more non-complete jobs, I
    *   use FCFS in that the job next in line after the one that just finished
    *   is chosen to run next.
    */
    if (jobToRun != -1 &&
          !jobs[jobToRun].complete &&
          jobs[jobToRun].runtime >= jobs[jobToRun].burstLength) {
        jobs[jobToRun].complete = true;   // Mark this job as complete
        jobStatus[jobToRun] = "";

        // Get first non-complete job's remaining runtime and its index
        int remaining, index;
        for (int j = 0; j < numJobs; j++) {
          if (!jobs[j].complete) {
            remaining = jobs[j].burstLength - jobs[j].runtime;
            index = j;
            break;
          }
        }

        // If only the last job remains, run it (no comparison needed)
        if (index == numJobs - 1) {
          jobToRun = index;
        }

        // Else get the next shortest job index
        else {
          for (int j = index + 1; j < numJobs; j++) {                 // Iterate over subsequent non-complete jobs
            if (!jobs[j].complete &&
                jobs[j].burstLength - jobs[j].runtime < remaining) {  // If remaining runtime of this job is shorter
              index = j;                                              // Update index and remaining runtime
              remaining = jobs[j].burstLength - jobs[j].runtime;
            }

            // In case of a tie, use FCFS
            else if (!jobs[j].complete &&
              jobs[j].burstLength - jobs[j].runtime == remaining) { // Tie!
              if ((index < jobToRun && j < jobToRun) ||             // If both are before the current job, or
                  (index < jobToRun && j > jobToRun)) {             // Only the first is before the current job
                index = j;                                          // Pick the latter
                remaining = jobs[j].burstLength - jobs[j].runtime;
              }                                                     // Else no need to update variabled (they already track the next job in line)
            }
          }

          // Run the next shortest job
          jobToRun = index;
        }
    }

    // Add new jobs
    for (int j = 0; j < numJobs; j++) {
      if (jobs[j].arrivalTime == t) {         // Initialize new job
        jobs[j].runtime = 0;
        jobs[j].complete = false;
        jobs[j].waiting = 0;
        if (jobToRun == -1) {                // If this is the only active job, run it
          jobToRun = j;
        }
      }
    }

    // Ready jobs display a "+" (job has started, but not yet finished)
    for (int j = 0; j < numJobs; j++) {
      if (t >= jobs[j].arrivalTime &&
          !jobs[j].complete &&
          j != jobToRun) {
          jobStatus[j] = "+";
          jobs[j].waiting++;
      }
    }

    // If job is running, display a "."
    for (int j = 0; j < numJobs; j++) {
      if (jobToRun != -1 &&
          j == jobToRun &&
          jobs[j].runtime < jobs[j].burstLength) {
        jobs[j].runtime++;
        jobStatus[j] = ".";
      }
    }

    // Display row
    cout << t << "\t";
    for (int j = 0; j < numJobs; j++) {
      cout << jobStatus[j] << "\t";
    }
    cout << endl;
  }
}

/**
* Use a SRTN scheduling algorithm to process a given set of jobs
* Arg1: an array of jobs
*
* Note:
* This algorithm is not part of the assignment. I accidentally implemented
*   it before I realized that SJN was being asked for. I decided to leave it
*   in the file, because why not.
*/
void processSRTN (job jobs[]) {

  // Index of job with shortest run time
  int jobToRun = -1;

  // Create a string array for formatting purposes (each jobs current status)
  string jobStatus[numJobs];

  // Count total number of steps needed (sum of bursts)
  int totalTime = 0;
  for (int i = 0; i < numJobs; i++) {
    totalTime += jobs[i].burstLength;
  }

  // Iterate over each time step
  for (int t = 0; t <= totalTime; t++) {

    /**
    * If running job is done, delete it from the queue and mark it as done
    *
    * Note:
    * This bit is likely more complicated than it needs to be
    * When a job is done, the next shortest job has to be found
    * I do this by first finding the next non-complete job, and comparing its
    *   runtime with that of all subsequent non-complete jobs. If there is a
    *   tie between the remaining length of two or more non-complete jobs, I
    *   use FCFS in that the job next in line after the one that just finished
    *   is chosen to run next.
    */
    if (jobToRun != -1 &&
          !jobs[jobToRun].complete &&
          jobs[jobToRun].runtime >= jobs[jobToRun].burstLength) {
        jobs[jobToRun].complete = true;   // Mark this job as complete
        jobStatus[jobToRun] = "";

        // Get first non-complete job's remaining runtime and its index
        int remaining, index;
        for (int j = 0; j < numJobs; j++) {
          if (!jobs[j].complete) {
            remaining = jobs[j].burstLength - jobs[j].runtime;
            index = j;
            break;
          }
        }

        // If only the last job remains, run it (no comparison needed)
        if (index == numJobs - 1) {
          jobToRun = index;
        }

        // Else get the next shortest job index
        else {
          for (int j = index + 1; j < numJobs; j++) {                 // Iterate over subsequent non-complete jobs
            if (!jobs[j].complete &&
                jobs[j].burstLength - jobs[j].runtime < remaining) {  // If remaining runtime of this job is shorter
              index = j;                                              // Update index and remaining runtime
              remaining = jobs[j].burstLength - jobs[j].runtime;
            }

            // In case of a tie, use FCFS
            else if (!jobs[j].complete &&
              jobs[j].burstLength - jobs[j].runtime == remaining) { // Tie!
              if ((index < jobToRun && j < jobToRun) ||             // If both are before the current job, or
                  (index < jobToRun && j > jobToRun)) {             // Only the first is before the current job
                index = j;                                          // Pick the latter
                remaining = jobs[j].burstLength - jobs[j].runtime;
              }                                                     // Else no need to update variabled (they already track the next job in line)
            }
          }

          // Run the next shortest job
          jobToRun = index;
        }
    }

    // Add new jobs
    for (int j = 0; j < numJobs; j++) {
      if (jobs[j].arrivalTime == t) {         // Initialize new job
        jobs[j].runtime = 0;
        jobs[j].complete = false;
        jobs[j].waiting = 0;
        if (jobToRun == -1 ||                 // If this is the only active job, or its the shortest, run it
            jobs[j].burstLength < jobs[jobToRun].burstLength - jobs[jobToRun].runtime) {
          jobToRun = j;
        }
      }
    }

    // Ready jobs display a "+" (job has started, but not yet finished)
    for (int j = 0; j < numJobs; j++) {
      if (t >= jobs[j].arrivalTime &&
          !jobs[j].complete &&
          j != jobToRun) {
          jobStatus[j] = "+";
          jobs[j].waiting++;
      }
    }

    // If job is running, display a "."
    for (int j = 0; j < numJobs; j++) {
      if (jobToRun != -1 &&
          j == jobToRun &&
          jobs[j].runtime < jobs[j].burstLength) {
        jobs[j].runtime++;
        jobStatus[j] = ".";
      }
    }

    // Display row
    cout << t << "\t";
    for (int j = 0; j < numJobs; j++) {
      cout << jobStatus[j] << "\t";
    }
    cout << endl;
  }
}

/**
* Main (see top of file for calling instructions)
*/
int main (int argc, char ** argv) {

  // Initialize some variables
  string inputFileName = "";
  string schedulingAlgorithm = "";
  int rrTimeQuantum = 0;

  // Parse command line arguments
  if (argc != 4) {
      cout << "Usage: scheduler [inputFile] [schedulingAlgorithm] [RRTimeQuantum]" << endl;
      exit (-1);
  } else {
    inputFileName = argv[1];
    schedulingAlgorithm = argv[2];
    rrTimeQuantum = atoi (argv[3]);
  }

  // Handle invalid arguments
  if (rrTimeQuantum < 1 || (schedulingAlgorithm != "RR" && schedulingAlgorithm != "SJF" && schedulingAlgorithm != "SRTN")) {
      cout << "Bad arguments. rrTimeQuantum must be >= 1. schedulingAlgorithm must be RR or SJF." << endl;
      exit (-1);
  } else if (schedulingAlgorithm != "RR") {
      cout << endl << "Warning: Round Robin algorithm was not chosen. Given quantum will not be used." << endl << endl;
  }

  // Open specified input file
  ifstream inputFile;
  inputFile.open(inputFileName.c_str());
  if (!inputFile) {
    cerr << "Unable to open " << inputFileName << endl;
    exit (-1);
  }

  // Count jobs from input file
  numJobs = 0;
  string in;
  while (inputFile >> in) {
    inputFile >> in;
    numJobs++;
  }

  // Create an array of jobs
  job jobs[numJobs];

  // Go to the top of the input file
  inputFile.clear();
  inputFile.seekg(0, ios::beg);

  // Read jobs into jobs array
  string time;
  string burst;
  int counter = 0;
  while (inputFile >> time) {
    inputFile >> burst;
    jobs[counter].id = counter;                       // Set job ID
    jobs[counter].arrivalTime = atoi(time.c_str());   // Set job arrival time
    jobs[counter].burstLength = atoi(burst.c_str());  // Set job burst length
    counter++;
  }

  // Print output header
  cout << "Time\t";
  for (int i = 0; i < numJobs; i++) {
    cout << "P" << i << "\t";
  }
  cout << endl;
  cout << "---------------------------------------------------------------" << endl;

  // Process jobs based on chosen scheduling algorithm (quantum not used with SJF or SRTN)
  if (schedulingAlgorithm == "RR") {
    processRR(jobs, rrTimeQuantum);
  } else if (schedulingAlgorithm == "SRTN") {
    processSRTN(jobs);
  } else {
    processSJF(jobs);
  }

  // Display individual and average wait times
  cout << "---------------------------------------------------------------" << endl;
  int waitSum = 0;
  for (int i = 0; i < numJobs; i++) {
    waitSum += jobs[i].waiting;
    cout << "P" << i << " waited " << jobs[i].waiting << " sec." << endl;
  }
  cout << "Average waiting time = " << ((double)waitSum / (double)numJobs) << " sec." << endl;

  // Close input file, and return success
  inputFile.close();
  return 0;
}
