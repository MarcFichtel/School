# Marc-Andre Fichtel
# 30014709
# CPSC 457 - FS 2017
# Assignment 2 - Question 5

# Program finds the X biggest files in the current directory and
#   prints their size sum
# Arg 1: File Suffix (string)
# Arg 2: X - Positive integer

#!/bin/bash

# Find files and print their names and sizes
find . -type f -name '*.'$1 -printf '%p,%s\n' |

# Sort files by file size
sort -t',' -k 2 -n -r |

# Take only the first X entries
head -$2 |

# Print file name and size and the total size sum
awk -F, '{ x += $2 ; print $1 " " $2 } END { print "Total: " x }'
