--
-- Starting code for CPSC 449 Assignment 1
--
-- Generate and output a Mondrian-style image as an SVG tag within an HTML
-- document.
--
import System.IO
import Control.Monad (replicateM)
import System.Random (randomRIO)

--
-- The width and height of the image being generated.
--
width :: Int
width = 1024

height :: Int
height = 768

--
-- The minimum dimensions at which a region can be split
--
minWidth :: Int
minWidth = 128

minHeight :: Int
minHeight = 128

--
-- Probability with which a region will be colored
--
chanceOfColor :: Float
chanceOfColor = 0.5

--
-- Generate and return a list of 20000 random floating point numbers between
-- 0 and 1.  (Increase the 20000 if you ever run out of random values).
--
randomList :: IO [Float]
randomList = replicateM 20000 $ randomRIO (0.0, 1.0 :: Float)

--
-- Compute an integer between low and high from a (presumably random) floating
-- point number between 0 and 1.
--
randomInt :: Int -> Int -> Float -> Int
randomInt low high x = round ((fromIntegral (high - low) * x) + fromIntegral low)

--
-- Adjust a floating point number to be between 0.2 and 0.8
--
adjustFloat :: Float -> Float
adjustFloat x
  | x < 0.1   = 0.1
  | x > 0.9   = 0.9
  | otherwise = x

--
-- Calculate and round some integer w * some random number m
--
roundIntTimesFloat :: Int -> Float -> Int
roundIntTimesFloat w m = floor(fromIntegral(w)*adjustFloat m)

--
-- Draw a rectangle with random color to emulate a piece of random Mondrian art.
-- Note that a perfect square will not be divided. This is unlikely, but creates
-- the possibility of vast size differences between regions, which makes the
-- final result more varied and appealing.
--
-- Parameters:
--   x, y: The upper left corner of the region
--   w, h: The width and height of the region
--   r:s:t:m:n:q:rs: A list of random floating point values between 0 and 1
--   ~~~ r, s, t are used for RGB values when coloring in a region
--   ~~~ m, n are used to create a point at which to divide regions
--   ~~~ q is used to determine whether or not a region is colored
--
-- Returns:
--   [Float]: The remaining, unused random values
--   String: The SVG tags that draw the image
--
mondrian :: Int -> Int -> Int -> Int -> [Float] -> ([Float], String)
mondrian _ _ _ _ [] = error "Can't compute mondrian: Not enough random values (0)."
mondrian _ _ _ _ (_:[]) = error "Can't compute mondrian: Not enough random values (1)."
mondrian x y w h (m:n:rs)

  -- Do not divide region if it is a perfect square (not very likely)
  | w == h = (rs, region x y w h rs)

  -- If region is big enough to be split horizontally and vertically
  | w > minWidth && h > minHeight = (rs4, upL ++ upR ++ dnL ++ dnR)

  -- If region is big enough to be split horizontally
  | w > minWidth                  = (rs6, left ++ right)

  -- If region is big enough to be split vertically
  | h > minHeight                 = (rs8, top ++ bottom)

  -- Else, region cannot be split - Draw it in white or a random color
  | otherwise                     = (rs, region x y w h rs)

  -- Define dimension calculations for region divisions
  where
    (rs1, upL)    = mondrian
                      x y (roundIntTimesFloat w m)
                          (roundIntTimesFloat h n) rs
    (rs2, upR)    = mondrian
                      (x + roundIntTimesFloat w m) y
                      (w - roundIntTimesFloat w m)
                      (roundIntTimesFloat h n) rs1
    (rs3, dnL)    = mondrian
                      x (y + roundIntTimesFloat h n)
                        (roundIntTimesFloat w m)
                        (h - roundIntTimesFloat h n) rs2
    (rs4, dnR)    = mondrian
                      (x + roundIntTimesFloat w m)
                      (y + roundIntTimesFloat h n)
                      (w - roundIntTimesFloat w m)
                      (h - roundIntTimesFloat h n) rs3
    (rs5, left)   = mondrian
                      x y (roundIntTimesFloat w m) h rs
    (rs6, right)  = mondrian
                      (x + roundIntTimesFloat w m) y
                      (w - roundIntTimesFloat w m) h rs5
    (rs7, top)    = mondrian
                      x y w (roundIntTimesFloat h n) rs
    (rs8, bottom) = mondrian
                      x (y + roundIntTimesFloat h n)
                      w (h - roundIntTimesFloat h n) rs7

--
-- Draw a region at (x, y) with dimensions w & h in some color scheme.
-- The original color theme makes a region orange-ish if it is horizontal,
-- blue-ish if it is vertical, and grey if it is a perfect square. Borders
-- are also grey, so that grey regions flow into each other. This makes orange
-- and blue regions stand out.
-- Some other color schemes that I found to be interesting were included in
-- the comments within the function.
--
region :: Int -> Int -> Int -> Int -> [Float] -> String
region _ _ _ _ [] = error "Can't draw region: Not enough random values (0)"
region _ _ _ _ (_:[]) = error "Can't draw region: Not enough random values (1)"
region _ _ _ _ (_:_:[]) = error "Can't draw region: Not enough random values (2)"
region _ _ _ _ (_:_:_:[]) = error "Can't draw region: Not enough random values (3)"
region x y w h (a:b:c:d:q:xs)

  -- If width > height, fill with some orange color
  | q < chanceOfColor  && w > h = style ++ (show (round (a * 255))) ++ "," ++
                                           (show (round (a * 255 * 0.3))) ++ "," ++
                                           (show (round (a * 255 * 0.1))) ++ ")\" />\n"

  -- If width < height, fill with some blue color
  | q < chanceOfColor  && w < h = style ++ (show (round (a * 255 * 0.2))) ++ "," ++
                                           (show (round (a * 255 * 0.2))) ++ "," ++
                                           (show (round (a * 255 * 0.9))) ++ ")\" />\n"

------------------------------------------------------------------------------------
-- Some more color combinations that I found to be visually appealing (to test out,
-- uncomment one of the below, amd comment out the two guards above)
--
-- Blue / Yellow
--  | q < chanceOfColor = style ++ (show (round (a * 255))) ++ "," ++
--                                 (show (round (a * 255))) ++ "," ++
--                                 (show (round (b * 255))) ++ ")\" />\n"
-- Green / Pink
--  | q < chanceOfColor = style ++ (show (round (a * 255))) ++ "," ++
--                                 (show (round (b * 255))) ++ "," ++
--                                 (show (round (a * 255))) ++ ")\" />\n"
-- Red / Turqoise
--  | q < chanceOfColor = style ++ (show (round (b * 255))) ++ "," ++
--                                 (show (round (a * 255))) ++ "," ++
--                                 (show (round (a * 255))) ++ ")\" />\n"
-- Blue / Yellow
--  | q < chanceOfColor = style ++ (show (round (a * 255 * c))) ++ "," ++
--                                 (show (round (a * 255 * c))) ++ "," ++
--                                 (show (round (b * 255))) ++ ")\" />\n"
-- Green / Blue / Yellow
--  | q < chanceOfColor = style ++ (show (round (a * 255 * c))) ++ "," ++
--                                 (show (round (a * 255))) ++ "," ++
--                                 (show (round (b * 255 * c))) ++ ")\" />\n"
-- Orange / Purple
--  | q < chanceOfColor = style ++ (show (round (a * 255))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ "," ++
--                                 (show (round (b * 255 * 0.5))) ++ ")\" />\n"
-- Blue / Purple / Green
--  | q < chanceOfColor = style ++ (show (round (a * 255 * c))) ++ "," ++
--                                 (show (round (b * 255 * c))) ++ "," ++
--                                 (show (round (a * 255))) ++ ")\" />\n"
-- Green / Purple
--  | q < chanceOfColor = style ++ (show (round (a * 255 * 0.5))) ++ "," ++
--                                 (show (round (b * 255))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ ")\" />\n"
-- Pink / Olive
--  | q < chanceOfColor = style ++ (show (round (a * 255))) ++ "," ++
--                                 (show (round (b * 255 * 0.5))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ ")\" />\n"
-- Blue / Brown
--  | q < chanceOfColor = style ++ (show (round (b * 255 * 0.5))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ "," ++
--                                 (show (round (a * 255))) ++ ")\" />\n"
-- Green / Brown
--  | q < chanceOfColor = style ++ (show (round (b * 255 * 0.5))) ++ "," ++
--                                 (show (round (a * 255))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ ")\" />\n"
-- Crimson / Cadet Blue
--  | q < chanceOfColor = style ++ (show (round (b * 255))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ "," ++
--                                 (show (round (a * 255 * 0.5))) ++ ")\" />\n"
------------------------------------------------------------------------------------

  -- Else fill the region grey
  | otherwise = style ++ (show (130)) ++ "," ++
                         (show (130)) ++ "," ++
                         (show (130)) ++ ")\" />\n"

  -- Define region tag descriptions
  where style = "<rect x=" ++ (show x) ++
                " y="      ++ (show y) ++
                " width="  ++ (show w) ++
                " height=" ++ (show h) ++
                " stroke=\"Grey\"" ++
                " fill=\"rgb("

--
-- The main program which generates and outputs mondrian.html.
--
main :: IO ()
main = do
  randomValues <- randomList

  let prefix = "<html><head></head><body>\n" ++
               "<svg width=\"" ++ (show width) ++
               "\" height=\"" ++ (show height) ++ "\">"
      image = snd (mondrian 0 0 width height randomValues)
      suffix = "</svg>\n</html>"

  writeFile "mondrian2.html" (prefix ++ image ++ suffix)
