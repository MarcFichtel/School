/**
* Marc-Andre Fichtel
* 30014709
* CPSC 457 - FS 2017
* Assignment 2 - Question 6
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

/**
* Program finds the X biggest files in the current directory and
*   prints their size sum
* Arg 1: File Suffix (string)
* Arg 2: X - Positive integer
*/

// Define some constants
#define MAX_FNAME_SIZE 512
#define MAX_FILES 1024

// Define a struct for a file and its size
typedef struct {
  char name[MAX_FNAME_SIZE];
  long int size;
} fileNameAndSize;

// Compare the size of two files
// Used for qsort()ing an array of fileNameAndSize structs in descending order
// Reference: http://stackoverflow.com/questions/6105513/need-help-using-qsort-with-an-array-of-structs
int compareFunction (const void * a, const void * b) {
  fileNameAndSize *fileNameAndSizeA = (fileNameAndSize *)a;   // Get first struct
  fileNameAndSize *fileNameAndSizeB = (fileNameAndSize *)b;   // Get second struct
  return ( fileNameAndSizeB->size - fileNameAndSizeA->size ); // Return B.size - A.size
}

int main (int argc, char ** argv) {

  // Handle command line arguments
  int num;

  // Validate number of cmd line args
  if (argc != 3) {
    fprintf (stderr, "Incorrect number of arguments.\n");
    exit (-1);

  // Cast second arg to an int and validate result
  } else {
    num = atoi(argv[2]);
  }
  if (num == 0) {
    fprintf (stderr, "Error. Please enter a positive integer as the second argument.\n");
    exit (-1);
  }

  // Open 'find' to scan the current directory recursively for files with the given suffix
  FILE * fp;
  char command[100];
  int length;
  length = snprintf (command, sizeof (command), "find . -type f -name '*.'%s", argv[1]);
  if (length <= sizeof(command)) {
    fp = popen (command, "r");
  } else {
    fprintf (stderr, "Command buffer size is too short.\n");
    exit (-1);
  }
  if (fp == NULL) {
    perror ("popen failed: ");
    exit (-1);
  }

  // Read in file names and track number of files with given suffix
  char buff[MAX_FNAME_SIZE];
  int nFiles = 0;
  char * fileNames[MAX_FILES];
  while (fgets (buff, MAX_FNAME_SIZE, fp)) {
    int len = strlen (buff) - 1;
    fileNames[nFiles] = strndup (buff, len);
    nFiles++;
  }
  fclose (fp);

  // Declare struct array to track files and their sizes
  fileNameAndSize fnas[nFiles];

  // Get all files' size with stat
  long int fileSize[nFiles];
  struct stat st;
  for (int i = 0; i < nFiles; i++) {
    if (0 != stat (fileNames[i], &st)) {
      perror ("stat failed: ");
      exit (-1);
    } else {
      fileSize[i] = st.st_size;
    }
  }

  // Insert all files' names and sizes into struct array
  for (int i = 0; i < nFiles; i++) {
    strcpy (fnas[i].name, fileNames[i]);
    fnas[i].size = fileSize[i];
  }

  // Sort struct array by file size
  qsort (fnas, nFiles, sizeof(fileNameAndSize), compareFunction);

  // Print out N biggest files' names, sizes, and their total sum
  long long totalSize = 0;
  for (int i = 0; i < num; i++) {
    totalSize += fnas[i].size;
    printf("%s %ld\n", fnas[i].name, fnas[i].size);
  }
  printf ("Total: %lld\n", totalSize);

  // Clean up
  for (int i = 0; i < nFiles; i++) {
    free (fileNames[i]);
  }

  // Return success
  return 0;
}
