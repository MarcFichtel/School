--------------------------------------------------------------------------------
--
-- CPSC 449 - Programming Paradigms
-- Assignment 2
-- Author: Marc-Andre Fichtel
--
--------------------------------------------------------------------------------
--
-- Note
--
-- The bonus was attempted for this assignment.
--
-- In order to simplify testing, I have chosen to make two separate versions of
-- most required functions. The first handles square images, the second handles
-- rectangular images. The functions handling rectangular images also work
-- with square images (tested). Functions for handling rectangular images
-- do not work perfectly: Wide images appear distorted, and high images throw
-- an error.
--
-- The code in this assignment successfully handles the first few images.
-- The "ThisSideUp" image is handled, but looks slightly pixelated. I am not
-- sure if this is expected or not. The "Mondrian" picture is the only square
-- image that is not processed correctly - the output looks different from the
-- input. I have not been able to find out why that is, as I couldn't reproduce
-- this behavior with any of the other square images.
--
--------------------------------------------------------------------------------

import qualified Data.ByteString.Lazy as BS
import Data.Word
import Data.Bits
import Data.Char
import Codec.Compression.Zlib as Z
import Numeric (showHex)

--------------------------------------------------------------------------------
--
-- Q1
--
-- Define a recursive algebraic type for a quad tree. The base case will be a
-- leaf node that contains an (Int, Int, Int) tuple where the first Int
-- represents an amount of red, the second represents and amount of green and
-- the third represents an amount of blue. Each color level will be a value
-- between 0 and 255. The recursive case is an internal node that has four
-- children. Define a second algebraic type for an image that consists of an
-- integer and a quad tree. The integer will represent the width (and height)
-- of the image (since it is square) and the quad tree will contain the color
-- data for the image.
--
--------------------------------------------------------------------------------
--
-- Bonus
--
-- Extend your implementation so that it correctly handles rectangular images of
-- arbitrary width and height (you can no longer assume that the width and
-- height, are the same, or that they are powers of 2). You’ll need to change
-- the algebraic types defined in Part 1 so that the type that stores the image
-- includes the width and height, along with the implementations of your
-- functions for working with a quad tree representation of an image when
-- completing this part of the assignment.
--
--------------------------------------------------------------------------------

-- A quadtree consists of leafs and nodes with four children each
data Quadtree = Leaf (Int, Int, Int) | Node Quadtree Quadtree Quadtree Quadtree

-- An image consists of a (square) size and a quadtree
data Image    = SizeColor Int Quadtree

-- An image2 consists of a width, a height, and a quadtree
data Image2   = WidthHeightColor Int Int Quadtree

-- The following were used for testing
instance Show Quadtree where
  show (Leaf x)               = "\tLeaf " ++ show x
  show (Node qt1 qt2 qt3 qt4) = "Node:\n" ++ show qt1 ++ "\n" ++ show qt2 ++ "\n" ++ show qt3 ++ "\n" ++ show qt4
instance Show Image where
  show (SizeColor size qt) = "Size: " ++ show size ++ ", QT:\n" ++ show qt
instance Show Image2 where
  show (WidthHeightColor w h qt) = "Width: " ++ show w ++ "Height: " ++ show h ++ ", Quadtree:\n" ++ show qt

--------------------------------------------------------------------------------
--
-- The following functions are a simple PNG file loader.  Note that these
-- functions will not load all PNG files.  They makes some assumptions about
-- the structure of the file that are not required by the PNG standard.
--
--------------------------------------------------------------------------------

--
-- Convert 4 8-bit words to a 32 bit (or larger) integer
--
make32Int :: Word8 -> Word8 -> Word8 -> Word8 -> Int
make32Int a b c d = ((((fromIntegral a) * 256) +
                       (fromIntegral b) * 256) +
                       (fromIntegral c) * 256) +
                       (fromIntegral d)

--
-- Get a list of all of the PNG blocks out of a list of bytes
--
getBlocks :: [Word8] -> [(String, [Word8])]
getBlocks [] = []
getBlocks (a:b:c:d:e:f:g:h:xs) = (name, take (size+12) (a:b:c:d:e:f:g:h:xs)) : getBlocks (drop (size + 4) xs)
  where
    size = make32Int a b c d
    name = (chr (fromIntegral e)) : (chr (fromIntegral f)) :
           (chr (fromIntegral g)) : (chr (fromIntegral h)) : []

--
-- Extract the information out of the IHDR block
--
getIHDRInfo :: [(String, [Word8])] -> (Int, Int, Int, Int)
getIHDRInfo [] = error "No IHDR block found"
getIHDRInfo (("IHDR", (_:_:_:_:_:_:_:_:w1:w2:w3:w4:h1:h2:h3:h4:bd:ct:_)) : _) = (make32Int w1 w2 w3 w4, make32Int h1 h2 h3 h4, fromIntegral bd, fromIntegral ct)
getIHDRInfo (x : xs) = getIHDRInfo xs

--
-- Extract and decompress the data in the IDAT block.  Note that this function
-- only handles a single IDAT block, but the PNG standard permits multiple
-- IDAT blocks.
--
getImageData :: [(String, [Word8])] -> [Word8]
getImageData [] = error "No IDAT block found"
getImageData (("IDAT", (_:_:_:_:_:_:_:_:xs)) : _) = BS.unpack (Z.decompress (BS.pack (take (length xs - 4) xs)))
getImageData (x:xs) = getImageData xs

--
-- Convert a list of bytes to a list of color tuples
--
makeTuples :: [Word8] -> [(Int, Int, Int)]
makeTuples [] = []
makeTuples (x : y : z : vals) = (fromIntegral x, fromIntegral y, fromIntegral z) : makeTuples vals

--
-- Convert a list of bytes that have been decompressed from a PNG file into
-- a two dimensional list representation of the image
--
imageBytesToImageList :: [Word8] -> Int -> [[(Int, Int, Int)]]
imageBytesToImageList [] _ = []
imageBytesToImageList (_:xs) w = makeTuples (take (w * 3) xs) : imageBytesToImageList (drop (w * 3) xs) w

--
-- Determine how many IDAT blocks are in the PNG file
--
numIDAT :: [(String, [Word8])] -> Int
numIDAT vals = length (filter (\(name, dat) -> name == "IDAT") vals)

--
-- Convert the entire contents of a PNG file (as a ByteString) into
-- a two dimensional list representation of the image
--
decodeImage :: BS.ByteString -> [[(Int, Int, Int)]]
decodeImage bytes
  | header == [137,80,78,71,13,10,26,10] &&
    colorType == 2 &&
    bitDepth == 8 = imageBytesToImageList imageBytes w
  | numIDAT blocks > 1 = error "The image contained too many IDAT blocks"
  | otherwise = error ("Invalid header\ncolorType: " ++ (show colorType) ++ "\nbitDepth: " ++ (show bitDepth) ++ "\n")
  where
    header = take 8 $ BS.unpack bytes
    (w, h, bitDepth, colorType) = getIHDRInfo blocks
    imageBytes = getImageData blocks
    blocks = getBlocks (drop 8 $ BS.unpack bytes)

--------------------------------------------------------------------------------
--
-- Q2
--
-- I have provided code that loads a PNG image file and stores it in a two
-- dimension lists. The two dimensional list has one entry in it for each pixel
-- in the image. Each pixel is represented by an (Int, Int, Int) tuple
-- consisting of the red, green and blue components of the pixel. The type of
-- such an image is [[(Int, Int, Int)]]. Write a function named createTree that
-- takes a list representation of an image and stores it in a quad tree. Your
-- function will take one parameter of type [[(Int, Int, Int)]], and it will
-- return one result that is a quad tree image you defined in Part 1 consisting
-- of both the image’s width and the tree. Your function should report an error
-- (and quit your program) if the provided image is not square. Hint: A general
-- approach that works for solving this problem is to examine the current image.
-- If it’s homogenous, meaning that all of the pixels are the same color, then
-- create a leaf node of that color. Otherwise, break the image into 4 smaller
-- square sub-images (all of which will have the same size) and construct an
-- internal node by recursively constructing four child nodes for the four
-- smaller sub-images
--
--------------------------------------------------------------------------------

--
-- Create an Image from a 2D list of integer 3-tuples [[(Int, Int, Int)]]
--
createTree :: [[(Int, Int, Int)]] -> Image
createTree []   = error "Empty outer list (createTree)."
createTree [[]] = error "Empty inner list (createTree)."
createTree list
  | isSquare list = SizeColor (length list) (l2qt list)
  | otherwise     = error "2D list is not square (createTree)."

--
-- This implementation works with rectangular Image2s
--
createTree2 :: [[(Int, Int, Int)]] -> Image2
createTree2 [] = error "Empty outer list (createTree2)."
createTree2 [[]] = error "Empty inner list (createTree2)."
createTree2 list = WidthHeightColor (length (head list)) (length list) (l2qt list)

--
-- Create a Quadtree from a [[(Int, Int, Int)]]
-- This should work for both square and rectangular images
--
l2qt :: [[(Int, Int, Int)]] -> Quadtree
l2qt list
  -- If there's only one value, or the list is homogenous, create a leaf
  | (length list == 1 && length (head list) == 1) || isHomogenous list
              = Leaf (head (head list))
  -- Else if there are exactly 4 values, create a node with four leafs
  | length list == 1 && length (head list) == 4
              = Node (Leaf (head (head list)))
                     (Leaf (head (drop 1 (head list))))
                     (Leaf (head (drop 2 (head list))))
                     (Leaf (head (drop 3 (head list))))
  -- Else create a node and split the list into 4 quadrants
  | otherwise = Node (q1) (q2) (q3) (q4)
  where
    -- Quadrant 1 (top left)
    q1 = l2qt (map (take (length list `div` 2)) (take (length list `div` 2) list))
    -- Quadrant 2 (top right)
    q2 = l2qt (map (drop (length list `div` 2)) (take (length list `div` 2) list))
     -- Quadrant 3 (bottom right)
    q3 = l2qt (map (drop (length list `div` 2)) (drop (length list `div` 2) list))
    -- Quadrant 4 (bottom left)
    q4 = l2qt (map (take (length list `div` 2)) (drop (length list `div` 2) list))

--
-- Check whether a given [[(Int, Int, Int)]] is square or not
--
isSquare :: [[(Int, Int, Int)]] -> Bool
isSquare list = length list == length (head list)

--
-- Checks whether a given [[(Int, Int, Int)]] is homogenous or not
-- Reference: https://stackoverflow.com/questions/6121256/efficiently-checking-that-all-the-elements-of-a-big-list-are-the-same
--
isHomogenous :: [[(Int, Int, Int)]] -> Bool
isHomogenous list = and $ map (== head list) (tail list)

--------------------------------------------------------------------------------
--
-- Q3
--
-- Write 3 functions for rotating an image in quad tree form: rotate90cw,
-- rotate180 and rotate90ccw. They will rotate the image 90 degrees clockwise,
-- rotate the image 180 degrees, and rotate the image 90 degrees counter
-- clockwise respectively. Each function will take a quad tree representation
-- of an image as its only parameter and return a quad tree representation of
-- an image as its only result, with the resulting image rotated by the correct
-- amount. Note that your implementations must operate on the quad tree
-- directly. It is not acceptable to convert the image to a list representation,
-- rotate the list representation, and then convert it back to a quad tree as
-- such an implementation is much slower and more memory intensive than
-- necessary. Ideally, two of your functions should be constructed as
-- compositions of the third. Hint: A image stored in a quad tree can be
-- rotated using the following recursive approach. If the node is a leaf node
-- then there is no work to do (this is the base case). If the node is an
-- internal node then change the order of the children so that they are rotated
-- by the desired amount, and then recursive rotate each child.
--
--------------------------------------------------------------------------------

-- Rotate an image 90 degrees clockwise
rotate90cw :: Image -> Image
rotate90cw (SizeColor size qt) = SizeColor size (rotateQT90cw qt)

-- Rotate an image2 90 degrees clockwise
rotate90cw2 :: Image2 -> Image2
rotate90cw2 (WidthHeightColor w h qt) = WidthHeightColor w h (rotateQT90cw qt)

-- Rotate an image 180 degrees
rotate180 :: Image -> Image
rotate180 (SizeColor size qt) = SizeColor size ((rotateQT90cw . rotateQT90cw) qt)

-- Rotate an image2 180 degrees
rotate1802 :: Image2 -> Image2
rotate1802 (WidthHeightColor w h qt) = WidthHeightColor w h ((rotateQT90cw . rotateQT90cw) qt)

-- Rotate an image 90 degrees counterclockwise
rotate90ccw :: Image -> Image
rotate90ccw (SizeColor size qt) = SizeColor size ((rotateQT90cw . rotateQT90cw . rotateQT90cw) qt)

-- Rotate an image2 90 degrees counterclockwise
rotate90ccw2 :: Image2 -> Image2
rotate90ccw2 (WidthHeightColor w h qt) = WidthHeightColor w h ((rotateQT90cw . rotateQT90cw . rotateQT90cw) qt)

-- Rotate a quadtree 90 degrees clockwise (used for the aboverotation functions)
rotateQT90cw :: Quadtree -> Quadtree
rotateQT90cw (Leaf (r,g,b)) = Leaf (r,g,b)
rotateQT90cw (Node a b c d) = Node (rotateQT90cw d)
                                   (rotateQT90cw a)
                                   (rotateQT90cw b)
                                   (rotateQT90cw c)
--------------------------------------------------------------------------------
--
-- Q4
--
-- Write a function named toHTML that generates the HTML SVG tags necessary to
-- render a quad tree representation of an image in a browser. To do this,
-- traverse your tree so that you visit every leaf node and generate a <rect>
-- tag for each leaf node that fills the correct region with the correct color.
-- The order in which the tags are generated is not important because none of
-- the rectangles that you are generating should overlap.
--
--------------------------------------------------------------------------------

-- Create a basic html structure for a square image document
toHTML :: Image -> String
toHTML (SizeColor size qt) = "<html>\n" ++
                               "\t<head></head>\n" ++
                               "\t<body>\n" ++
                                 "\t\t<svg width=\"" ++ (show size) ++ "\" height=\"" ++ (show size) ++ "\">" ++
                                   makeRects qt 0 0 size ++
                                 "\n\t\t</svg>\n" ++
                               "\t</body>\n" ++
                             "</html>"

-- Create a basic html structure for a square image document
toHTML2 :: Image2 -> String
toHTML2 (WidthHeightColor w h qt) = "<html>\n" ++
                                      "\t<head></head>\n" ++
                                      "\t<body>\n" ++
                                        "\t\t<svg width=\"" ++ (show w) ++ "\" height=\"" ++ (show h) ++ "\">" ++
                                          makeRects2 qt 0 0 w h ++
                                        "\n\t\t</svg>\n" ++
                                      "\t</body>\n" ++
                                    "</html>"

-- Create <rect>s corresponding to a square image's quadtree
makeRects :: Quadtree -> Int -> Int -> Int -> String
makeRects (Leaf color) x y size = "\n\t\t\t" ++
                                  "<rect x=" ++ (show x) ++
                                  " y="      ++ (show y) ++
                                  " width="  ++ (show size) ++
                                  " height=" ++ (show size) ++
                                  " fill=\"rgb" ++ (show color) ++ "\"/>"
makeRects (Node qt1 qt2 qt3 qt4) x y size = makeRects qt1 x y hs ++
                                            makeRects qt2 (x+hs) y hs ++
                                            makeRects qt3 (x+hs) (y+hs) hs ++
                                            makeRects qt4 x (y+hs) hs
  where hs = size `div` 2

-- Create <rect>s corresponding to a rectangular image's quadtree
makeRects2 :: Quadtree -> Int -> Int -> Int -> Int -> String
makeRects2 (Leaf color) x y w h = "\n\t\t\t" ++
                                    "<rect x=" ++ (show x) ++
                                    " y="      ++ (show y) ++
                                    " width="  ++ (show w) ++
                                    " height=" ++ (show h) ++
                                    " fill=\"rgb" ++ (show color) ++ "\"/>"
makeRects2 (Node qt1 qt2 qt3 qt4) x y w h = makeRects2 qt1 x y hw hh ++
                                            makeRects2 qt2 (x+hw) y hw hh ++
                                            makeRects2 qt3 (x+hw) (y+hh) hw hh ++
                                            makeRects2 qt4 x (y+hh) hw hh
  where
    hw = w `div` 2
    hh = h `div` 2

--------------------------------------------------------------------------------
--
-- Load a PNG file, convert it to a quad tree, rotate it 90, 180 and 270
-- degrees, and write all four images to an .html file.
--
-- Note: In order to test rectangular images, please uncomment the appropriate
-- lines below.
--
--------------------------------------------------------------------------------

main :: IO ()
main = do
  -- Change the name inside double quotes to load a different file
  input <- BS.readFile "Test_2x2.png"
--  input <- BS.readFile "Test_512x512.png"
--  input <- BS.readFile "TwoSquares.png"
--  input <- BS.readFile "ThisSideUp.png"
--  input <- BS.readFile "Mondrian.png"
--  input <- BS.readFile "Test_Rectangle_1.png"
--  input <- BS.readFile "Test_Rectangle_2.png"

  -- image is the list representation of the image stored in the .png file
  let image = decodeImage input

  -- Convert the list representation of the square image into a tree representation
  let qtree = createTree image

  -- Convert the list representation of the rectangular image into a tree representation
  --let qtree2 = createTree2 image

  -- Rotate the tree representation of the square image
  let rot90 = rotate90cw qtree
  let rot180 = rotate180 qtree
  let rot270 = rotate90ccw qtree

  -- Rotate the tree representation of the rectangular image
  --let rot90rect = rotate90cw2 qtree2
  --let rot180rect = rotate1802 qtree2
  --let rot270rect = rotate90ccw2 qtree2

  -- Write the original square image and the rotated images to quadtree.html
  writeFile "quadtree.html" ((toHTML qtree) ++ "\n\n<br><br><br>\n\n" ++
                             (toHTML rot90) ++ "\n\n<br><br><br>\n\n" ++
                             (toHTML rot180) ++ "\n\n<br><br><br>\n\n" ++
                             (toHTML rot270) ++ "\n\n<br><br><br>\n\n")

  -- Write the original rectangular image and the rotate images to quadtree2.html
  --writeFile "quadtree2.html" ((toHTML2 qtree2) ++ "\n\n<br><br><br>\n\n" ++
  --                           (toHTML2 rot90rect) ++ "\n\n<br><br><br>\n\n" ++
  --                           (toHTML2 rot180rect) ++ "\n\n<br><br><br>\n\n" ++
  --                           (toHTML2 rot270rect) ++ "\n\n<br><br><br>\n\n")
