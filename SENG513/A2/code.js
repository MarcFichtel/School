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
// TODO check why I get slightly less words than expected in example 3. What is counted as a word???
//
function countChars(txt) {
  return txt.length;
}

//
// Count words in a string
//
function countWords(txt) {
  let words = getWords(txt);
  return words.length;
}

//
// Count lines in a string
//
function countLines(txt) {
  return txt.split("\n").length;
}

//
// Count nonempty lines in a string
//
function countNonEmptyLines(txt) {
  let count = 0;
  
  // Isolate lines
  let lines = txt.split("\n");
  for (let i = 0; i < lines.length; i++) {
  	
  	// Remove all whitespace
  	lines[i] = lines[i].replace(/\s/g, "");

  	// If line has anything left in it, it is nonempty
  	if (lines[i].length != 0) {
  		count++;
  	}
  }
  return count;
}

//
// Find the length of the longest line in a string
//
function findMaxLineLength(txt) {
  
  // Isolate lines
  let lines = txt.split("\n");
  let result = lines[0].length;

  // Find maximum line length
  for (let i = 1; i < lines.length; i++) {
  	if (lines[i].length > result) {
  		result = lines[i].length;
  	}
  }
  return result;
}

//
// Find the average word length in a string
//
function findAvgWordLength(txt) {
  let words = getWords(txt);
  let count = 0;
  for (let i = 0; i < words.length; i++) {
  	count += words[i].length;
  }

  return count / words.length;
}

//
// Find all palindromes in a string
//
function findPalindromes(txt) {
  let words = getWords(txt);
  let palindromes = [];
	
  // Check each word
  for (let i = 0; i < words.length; i++) {

  	// If word is at least 2 characters long and is the same as its reverse, then its a palindrome
  	if (words[i].toLowerCase() === words[i].toLowerCase().split("").reverse().join("") && words[i].length >= 2) {
  		palindromes.push(words[i].toLowerCase());
  	}
  }

  return palindromes;
}

//
// Find the longest words in a string (at most 10)
//
function findLongestWords(txt) {
  let words = getWords(txt);
  let longestWords = [];  
  let longestWordCount = words[0].length;

  // Find longest word count
  for (let i = 1; i < words.length; i++) {
  	if (words[i].length > longestWordCount) {
  		longestWordCount = words[i].length;
  	}
  }

  // Find unique longest words (may record more than 10 through the for loop)
  while (longestWordCount > 0 && longestWords.length < 10) {
  	for (let i = 0; i < words.length; i++) {
  	  if (words[i].length >= longestWordCount && 
  		longestWords.indexOf(words[i].toLowerCase()) <= -1) {
  		longestWords.push(words[i].toLowerCase());
  	  }
  	}
  	longestWordCount--;  
  }

  // Sort alphabetically (2nd order) and by length (1st order)
  longestWords.sort();
  longestWords.sort(function(a,b) {
  	return b.length - a.length;
  });

  // If there are more than 10, remove items until there are only 10
  while (longestWords.length > 10) {
  	longestWords.pop();
  }

  return longestWords;
}

//
// Find the most frequent words in a string
//
function findMostFrequentWords(txt) {
  let words = getWords(txt);
  let wordCount = {};
  let result = [];
  let highestCount = 0;

  // Convert all words to lowercase
  for (let i = 0; i < words.length; i++) {
  	words[i] = words[i].toLowerCase();
  }

  // Sort words alphabetically
  words.sort();

  // Count how often each word appears
  for (let i = 0; i < words.length; i++) {
  	if (!(words[i] in wordCount)) {
  		wordCount[words[i]] = 1;
  	} else {
  		wordCount[words[i]]++;
  	}
  }

  // Find highest word count
  for (let i = 0; i < words.length; i++) {
  	if (wordCount[words[i]] > highestCount) {
  	  	highestCount = wordCount[words[i]];
  	}
  }

  // Find 10 most frequent words in order
  while (highestCount > 0 && result.length < 10) {
  	for (let i = 0; i < words.length; i++) {
  	  if (wordCount[words[i]] === highestCount && 
  	  	result.indexOf(words[i] + "(" + wordCount[words[i]] + ")") <= -1) {
  		result.push(words[i] + "(" + wordCount[words[i]] + ")");
  	  }
  	}
  	highestCount--;
  }

  // If there are more than 10, remove items until there are only 10
  while (result.length > 10) {
  	result.pop();
  }

  return result;
}

//
// Isolate all words into an array
//
function getWords(txt) {

  // Remove leading and trailing whitespace
  txt = txt.trim();

  // Replace non-word characters with spaces
  txt = txt.replace(":-)", " ");
  txt = txt.replace(/\n/g, " ");
  txt = txt.replace(/\./g, " "); 
  txt = txt.replace(/\!/g, " "); 	
  txt = txt.replace(/\?/g, " ");
  txt = txt.replace(/\,/g, " ");
  txt = txt.replace(/\+/g, " ");
  txt = txt.replace(/\:/g, " ");
  txt = txt.replace(/\"/g, " ");

  // Remove consecutive spaces
  txt = txt.replace(/\s+/g, " ");
  let words = txt.split(" ");

  // If last word is empty, remove it
  if (words[words.length-1] === "") {
  	words.splice(words.length-1, 1);
  }

  return words;
}
