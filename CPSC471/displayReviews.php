<?php
include "base.php";
session_start();
?>

<!DOCTYPE html>
<!--CPSC 471 Project-->
<html>
	<head>
		<meta content="text/html; charset=UTF-8">
		<title>CPSC 471 Project</title>
		<link rel="stylesheet" href="style.css" type="text/css">
	</head>
	
	<body>
		<div id="main">
			<!--Header-->
			<div id="header">
				<h3>CPSC 471 --- Group 9 --- Final Project</h3><br />
				<h3>Authors:</h3>
				<h4>Mohamed-Hani Abdillah<br />Nikolai Aguierre<br />Marc-Andre Fichtel</h4>
			</div>
        
			<!--Navgation-->
			<div id="navi">
                            
                            <!--User Login-->
                            <a id="userHomeButton" href="demoPage.php">User Home</a>

                            <!--Employee Login-->
                            <a id="employeeHomeButton" href="employeeLogin.php">Employee Home</a>

                            <!--Admin Login-->
                            <a id="adminHomeButton" href="adminLogin.php">Admin Home</a>
			</div>
		
			<?php
				//checks to see if the user has submitted a review
				if (!empty($_POST['rating']) && !empty($_POST['comment'])){
					//selects the customers' id from its username
					$sql = "SELECT id FROM Customer WHERE username = '".$_SESSION['Username']."'";
					$current_username = mysqli_query($conn, $sql);
					//gets the id
					$current_row = mysqli_affected_rows($current_username);
					
					//creates a new entry in review using the informating we have
					$sql = "INSERT INTO Review (customer_id, product_id, rating, comment) VALUES ('".$current_row['id']."', '".$_SESSION['ProductId']."', '".$_POST['rating']."', '".$_POST['comment']."')";
					$createReview = mysqli_query($conn, $sql);
                                        if ($createReview) {
                                            echo "<h1>Success</h1>";
                                            echo "<p>Your review for this product was saved.</p>";
                                        }
				}
				
				//finds all the reviews with the product id the same as the session product id obtained from the new display.php
				$pId = $_SESSION['ProductId'];
                                $sql = "SELECT * FROM Review WHERE id = '".$pId."' ";
				$reviews = mysqli_query($conn, $sql);
				
				//creates the table
				echo "<table id='reviewsTable'>";
                                echo "<tr>";
                                    echo "<td></td>";
                                    echo "<td><strong>Username</strong></td>";
                                    echo "<td><strong>Rating</strong></td>";
                                    echo "<td><strong>Comment</strong></td>";
                                echo "</tr>";
				while ($row = mysqli_fetch_array($reviews)) {
                                    $sql = "SELECT username FROM Customer WHERE id = '".$row['customer_id']."'";
                                    $username_row = mysqli_query($conn, $sql);
                                    $username = mysqli_fetch_array($username_row);
                                    $rating = $row['rating'];
                                    $comment = $row['comment'];
                                    echo "<tr>";
                                    echo "<td>'".$username."'</td>";
                                    echo "<td>'".$rating."'</td>";
                                    echo "<td>'".$comment."'</td>";
                                    echo "</tr>";
                                }
                                echo "</table>";
			?>
				
				<!--the form submissions part that has a dropdown for rating out of 5-->
				<h1>Review Product</h1>
				<p>Write a review</p>
				
				<!--Display review form-->
				<form method = "post" action="displayReviews.php" name="reviewform" id="reviewsform">
					<fieldset>
						<!--dropdown out of 5-->
						<label for="rating">Rating:</label>
						<select name = "rating">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
						</select><br />
						<label for="comment">Comment:</label>
						<textarea name="comment" rows="5" cols="40"></textarea><br />
						<input type="submit" name="review" id="review" value="Submit Review" /><br />
					</fieldset>
				</form>
		</div>
	</body>
</html>