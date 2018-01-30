from browser import document as doc
from browser import window
from browser import alert
import collections

class Results:
    pass

def filterAlnum(txt):
    res = ""
    for c in txt:
        if c.isalpha() or c.isdigit():
            res += c
        else:
            res += ' '
    return res

def pyStats(orig):
    txt = orig.lower()
    lines = txt.split("\n")
    words = [ w for w in filterAlnum(txt).split(" ") if len(w) > 0 ]
    nonEmptyLines = [ l for l in lines if len(l.strip()) > 0]

    seen = set()
    uniqueWords = [x for x in words if not (x in seen or seen.add(x))]

    palisd = [ w for w in uniqueWords if len(w) > 2 and w == w[::-1] ]
    freqs = collections.Counter(words)
    lmfq = sorted( uniqueWords, key=lambda w:(-freqs[w],w))
    lmfq2 = [ (w+'('+str(freqs[w])+')') for w in lmfq]

    res = Results()
    res.nChars = len(orig)
    res.nWords = len(words)
    res.nLines = 0 if orig == "" else len(lines)
    res.nonEmptyLines = len(nonEmptyLines)
    res.maxLineLength = max(map(len,lines))
    res.averageWordLength = sum(map(len,words))/res.nWords if res.nWords > 0 else 0
    res.palindromes = palisd
    res.longestWords = sorted(uniqueWords, key=lambda x:(-len(x),x))[:10]
    res.mostFrequentWords = lmfq2[:10]
    return res

#doc["test"].bind("click", echo)

print("Hello 1")
window.pyStats = pyStats
