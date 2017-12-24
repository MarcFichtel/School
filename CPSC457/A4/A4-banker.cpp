/*
 * banker.cpp
 *
 * Student Name: Marc-Andre Fichtel
 * Student Number: 30014709
 *
 * Class: CPSC 457 Spring 2017
 * Instructor: Pavol Federl
 *
 * Copyright 2017 University of Calgary. All rights reserved.
 */

#include <iostream>
#include <fstream>
#include <sstream>
#include <stdlib.h>
#include <algorithm>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

class Banker
{
private:
    int numProc;      // the number of processes
    int numResources; // the number of resources
    int * available;  // number of available instances of each resource
    int ** max;       // the max demand of each process, e.g., max[i][j] = k
                      // means process i needs at most k instances of resource j
    int ** allocation;// the number of resource instances already allocated
    int ** need;      // the number of resource isntances needed by each process

public:

    /* Initializing the vectors and matrixes for the Banker's Algorithm. Takes ownership of
     * all arrays.
     * @param avail  The available vector
     * @param m      The max demand matrix
     * @param alloc  The allocation matrix
     * @param p      The number of processes
     * @param r      The number of resources
     */
    Banker (int * avail, int ** m, int ** alloc, int p, int r) {
        numProc = p;
        numResources = r;

        // Setup the available vector, the max matrix, and the allocation matrix
        available = avail;
        max = m;
        allocation = alloc;

        // Initialize the need matrix
        need = new int*[numProc];
        for (int i = 0; i < numProc; i++)
            need[i] = new int[numResources];
    }

    /* Destroy the vectors and matrixes
     */
    ~Banker() {
        numProc = 0;
        numResources = 0;

        // Free all allocated memory space
        delete[] available;
        for (int i = 0; i < numProc; i++)
        {
            delete[] need[i];
            delete[] max[i];
            delete[] allocation[i];
        }
        delete[] need;
        delete[] max;
        delete[] allocation;
    }

    /* Check whether it is safe to grant the request
     * @param pid    The process that is making the request
     * @param req    The request
     * @param sequenceOrReason  The safe execution sequence returned by the algorithm
     * @return Whether granting the request will lead to a safe state.
     */
    bool isSafe (int pid, int * req, string & sequenceOrReason) {

        // Check if request exceeds declared maximum
        for (int i = 0; i < numResources; ++i) {
          if (req[i] > (max[pid][i] - allocation[pid][i])) {
            sequenceOrReason = "request is invalid (exceeding declared max for process)";
            return false;
          }
        }

        // Check if request exceeds available resources
        for (int i = 0; i < numResources; ++i) {
          if (req[i] > available[i]) {
            sequenceOrReason = "not enough resources available";
            return false;
          }
        }

        // Save state of available (deep copy)
        int * tmpAvailable = new int[numResources];
        for (int i = 0; i < numResources; ++i) {
          tmpAvailable[i] = available[i];
        }

        // Save state of allocation & need (deep copies)
        int ** tmpAlloc = new int*[numProc];
        int ** tmpNeed = new int*[numProc];
        for (int i = 0; i < numProc; ++i) {
          tmpAlloc[i] = new int[numResources];
          tmpNeed[i] = new int[numResources];
        }
        for (int i = 0; i < numProc; ++i) {
          for (int j = 0; j < numResources; ++j) {
            tmpAlloc[i][j] = allocation[i][j];
            tmpNeed[i][j] = max[i][j] - allocation[i][j];
          }
        }

        // Pretend to grant request
        for (int i = 0; i < numResources; ++i) {
          tmpAvailable[i] -= req[i];
          tmpAlloc[pid][i] += req[i];
          tmpNeed[pid][i] -= req[i];
        }

        // Initialize work, need, and alloc vectors from corresp. arrays
        vector<int> WorkV(tmpAvailable, tmpAvailable + sizeof tmpAvailable / tmpAvailable[0]);
        vector<int> tmp1(3), tmp2(3);
        vector< vector<int> > NeedV;
        vector< vector<int> > AllocV;
        for (int i = 0; i < numProc; ++i) {
          for (int j = 0; j < numResources; ++j) {
            tmp1[j] = tmpNeed[i][j];
            tmp2[j] = tmpAlloc[i][j];
          }
          NeedV.push_back(tmp1);
          AllocV.push_back(tmp2);
        }

        // Initialize Finish array of bools (all false)
        bool * Finish = new bool[numProc];
        for (int i = 0; i < numProc; ++i) {
          Finish[i] = false;
        }

        // Update Work and Finish
        sequenceOrReason = "";                                // Reset sequence
        for (int i = 0; i < numProc; ++i) {                   // Iterate over processes
          if (!Finish[i] && NeedV[i] <= WorkV) {              // If process can run

            // Convert i to string and append to sequence
            ostringstream pidString;
            pidString << i;
            sequenceOrReason += "P" + pidString.str() + ", ";

            // Update Work
            for (int j = 0; j < numResources; ++j) {
              WorkV[j] += AllocV[i][j];
            }

            // Update Finish
            Finish[i] = true;

            // Restart from the top
            i = -1;
          }
        }

        // If state is unsafe, return failure
        for (int i = 0; i < numProc; ++i) {
          if (!Finish[i]) {
            sequenceOrReason = "request would result in an unsafe state";
            return false;
          }
        }

        // Else format sequence string ending and return success
        sequenceOrReason = sequenceOrReason.substr(0, numProc*4-2);
        return true;
    }
};

int main (int argc, char * const argv[])
{
    ifstream config;       // Configuration file
    string conffile;       // The configuration file name
    int numProc;           // The number of processes
    int numResources;      // The number of resources
    string sequenceOrReason;       // The execution sequence returned by the Banker's Algorithm
    int i, j, index;       // Indices for the vectors and matrixes
    int pid;               // The ID of the process that is making the request
    string reqStr;         // The request vector in string format

    // Read in the config file name from the commanda-line arguments
    if (argc < 2)
    {
        cout << "Usage: banker <config file>\n";
        return 0;
    }
    else
    {
        conffile = argv[1];
    }

    // Open the file
    config.open(conffile.c_str());

    // Get the number of process and the number of resources
    string line, var, equal;    // strings for parsing a line in the config file
    getline(config, line);
    istringstream iss(line);
    iss >> var >> equal >> numProc;     // Get the number of processes
    iss.clear();

    getline(config, line);
    iss.str(line);
    iss >> var >> equal >> numResources;    // Get the number of resources
    iss.clear();

    // Create the available vector, the max matrix, and the allocation matrix
    // according to the number of processes and the number of resources
    int * available = new int[numResources];
    int ** max = new int*[numProc];
    int ** allocation = new int*[numProc];
    for (int i = 0; i < numProc; i++)
    {
        max[i] = new int[numResources];
        allocation[i] = new int[numResources];
    }

    // Get the available vector
    getline(config, line);
    replace(line.begin(), line.end(), '<', ' ');  // Remove "<" and ">"
    replace(line.begin(), line.end(), '>', ' ');
    iss.str(line);
    iss >> var >> equal;
    for (j = 0; j < numResources; j++)        // Read in the "available" vector
        iss >> available[j];
    iss.clear();

    // Get the max matrix and the allocation matrix
    for (i = 0; i < numProc; i++)
    {
        getline(config, line);
        replace(line.begin(), line.end(), '<', ' ');
        replace(line.begin(), line.end(), '>', ' ');
        iss.str(line);
        iss >> var;
        index = atoi(&var.at(1));            // Get the process ID
        if (index < 0 || index >= numProc)
        {
            cerr << "Invalid process ID: " << var << endl;
            return 0;
        }

        // Get the number of resources allocated to process "index".
        for (j = 0; j < numResources; j++)
            iss >> allocation[index][j];

        // Get the max allocation to process "index".
        for (j = 0; j < numResources; j++)
            iss >> max[index][j];

        iss.clear();
    }

    // Get the request vector
    int * request = new int[numResources];
    getline(config, line);
    reqStr = line.substr(line.find('<'), line.find('>') - line.find('<') + 1);
    replace(line.begin(), line.end(), '<', ' ');
    replace(line.begin(), line.end(), '>', ' ');
    iss.str(line);
    iss >> var >> pid >> equal;
    for (j = 0; j < numResources; j++)          // Read in the "request" vector
        iss >> request[j];
    iss.clear();

    // Check the request using the Banker's algorithm.
    Banker * banker = new Banker(available, max, allocation, numProc, numResources);
    if (banker -> isSafe(pid, request, sequenceOrReason))
        cout << "Grant request " << reqStr << " from P" << pid << ".\n"
             << "Sequence: " << sequenceOrReason << ".\n";
    else
        cout << "Reject request " << reqStr << " from P" << pid << ".\n"
             << "Reason: " << sequenceOrReason << "\n";
}
