//
// this is just a stub for a function you need to implement
//
function getStats(txt) {

  let result = {
    nChars: countChars(txt),
    nWords: countWords(txt),
    nLines: countLines(txt),
    nNonEmptyLines: countNonEmptyLines(txt),
    maxLineLength: findMaxLineLength(txt),
    averageWordLength: findAvgWordLength(txt),
    palindromes: findPalindromes(txt),
    longestWords: findLongestWords(txt),
    mostFrequentWords: findMostFrequentWords(txt)
  }

    return result;
}

//
// Count characters in a string
//
function countChars(txt) {
  return txt.length;
}

//
// Count words in a string
//
function countWords(txt) {
  // Convert newlines to spaces and trim the string
  txt = txt.replace(/\n/g, " ").trim();
  // Split at spaces
  let words = txt.split(" ");

  // TODO solve example 3

  return words.length;
}

//
// Count lines in a string
//
function countLines(txt) {
  return txt.split("\n").length;
}

//
// TODO
//
function countNonEmptyLines(txt) {
  let countNL = 0;
  for (let i = 0; i < txt.length; i++) {
    if (txt[i] === "\n") {
      countNL++;
    }
  }
  return countNL;
}

//
// Find the length of the longest line in a string
//
function findMaxLineLength(txt) {
  return null;
}

//
// Find the average word length in a string
//
function findAvgWordLength(txt) {
  return null;
}

//
// Find all palindromes in a string
//
function findPalindromes(txt) {
  return null;
}

//
// Find the longest words in a string
//
function findLongestWords(txt) {
  return null;
}

//
// Find the most frequent words in a string
//
function findMostFrequentWords(txt) {
  return null;
}
