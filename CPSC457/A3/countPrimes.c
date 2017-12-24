/**
* Marc-Andre Fichtel
* 30014709
* CPSC 457 - FS 2017
* Assignment 3 - Question 5
*/

/** counts number of primes from standard input
*
* compile with:
*   $ g++ countPrimes.c -O2 -lpthread -o countPrimes -lm
*/
#include <iostream>
#include <math.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>

using namespace std;

/**
* Note:
* The program assumes that 256 is the maximum number of threads that
* can be created.
*/

const int MAX_THREADS = 256;// Maximum number of threads
int64_t count = 0;          // count = # primes counted
int nThreads = 1;           // nThreads = # threads to count primes with
int turn = 0;               // turn = which thread's turn is it
int flag[MAX_THREADS];      // flag = which threads are interested in CS

/// primality test, if n is prime, return 1, else return 0
int isPrime(int64_t n)
{
     if( n <= 1) return 0; // small numbers are not primes
     if( n <= 3) return 1; // 2 and 3 are prime
     if( n % 2 == 0 || n % 3 == 0) return 0; // multiples of 2 and 3 are not primes
     int64_t i = 5;
     int64_t max = sqrt(n);
     while( i <= max) {
         if (n % i == 0 || n % (i+2) == 0) return 0;
         i += 6;
     }
     return 1;
}

/**
* function to be executed by newly created threads
* uses Peterson's alg'm to achieve mutual exclusion, progess, and bounded waiting
*/
void * thread_work(void * tid) {
    while(1) {

      // track whether a prime was found or not
      int prime = false;

      // parse tid to int
      int tidInt = *((int*)(&tid));

      // read next number, break if EOF reached and advance turn
      int64_t num;
      if(1 != scanf("%ld", & num)) {
        turn = (tidInt + 1) % nThreads;
        flag[tidInt] = false;
        break;
      }

      // display which thread is checking which number (used for testing)
//      cout << "Thread " << *((int*)(&tid)) << " checking number " << num << endl;

      // indicate interest in entering the critical section
      flag[tidInt] = true;

      // update turn for next thread
      if (nThreads >= 2) {
        turn = (tidInt + 1) % nThreads;

      // if there's only 1 thread, it is that thread's turn
      } else {
        turn == tidInt;
      }

      // check if num is prime
      prime = isPrime(num);

      // wait while next thread works
      while(flag[(tidInt + 1) % nThreads] && turn == tidInt + 1) {
        sleep(1);
      }

      // critical section (increment count if prime found)
      if (prime) {
        count ++;
      }

      // thread is done with critical section
      flag[tidInt] = false;
    }
    pthread_exit(0);
}

int main( int argc, char ** argv)
{
    /// parse command line arguments
    if( argc != 2) {
        cout << "Usage: countPrimes [nThreads]" << endl;
        exit(-1);
    }
    if( argc == 2) nThreads = atoi( argv[1]);

    /// handle invalid arguments
    if( nThreads < 1 || nThreads > MAX_THREADS) {
        cout << "Bad arguments. 1 <= nThreads <= " << MAX_THREADS << "!" << endl;
        exit(-1);
    }

    /**
    * create thread array with nThread threads
    * each threads executes thread_work(i)
    * where i is used to split the list of numbers evenly among threads
    */
    cout << "Counting primes using " << nThreads << " thread(s)." << endl;
    pthread_t threads[nThreads];
    long status;
    for (int i = 0; i < nThreads; i++) {
      status = pthread_create(&threads[i], NULL, thread_work, (void *)i);
      if (status != 0) {
        cerr << "Error: Creating thread " << i << " failed." << endl;
        return (-1);
      }
    }

    // Wait for threads and clean up
    for (int i = 0; i < nThreads; i++) {
      pthread_join(threads[i], NULL);
    }

    /// report results and return success
    cout << "Found " << count << " primes." << endl;
    return 0;
}
