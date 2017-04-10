<?php 
include "base.php";     // Include base
session_start();        // Start session
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
            // Someone is logged in
            if (!empty($_SESSION['LoggedIn']) && !empty($_SESSION['Username'])) {
                
                // User does not have permission
                if(!$_SESSION['customeruser']) {
                    echo "<h1>Error</h1>";
                    echo "<p>You must be logged in as a customer to access this page.</p>";
                    echo "<br /><a href='demoPage.php'>Go to Customer Login</a><br /><br />";
                
                // User does have permission and funds were added   
                } else if (!empty($_POST['addFunds'])) {
                    
                    // Get current funds, and add given funds
                    $addFunds = $_POST['addFunds'];
                    $userId = $_SESSION['userId'];
                    $currentFundsQuery = mysqli_query($conn, ""
                        . "SELECT funds FROM Customer WHERE id = '".$userId."' ");
                    
                    // Error
                    $row = mysqli_fetch_array($currentFundsQuery);
                    $funds = $row['funds'];

                    // Add given number to funds, update DB    
                    $newFunds = $funds + $addFunds;
                    $addFundsQuery = mysqli_query($conn, ""
                            . "UPDATE Customer "
                            . "SET funds = '".$newFunds."' "
                            . "WHERE id = '".$userId."' ");
                    // Error
                    if (!$addFundsQuery) {
                        echo "<h1>Error</h1>";
                        echo "<p>Adding funds failed. Please try again. <br /></p>";
                        echo "<code>", mysqli_error($conn), "</code>";

                    // Success    
                    } else {
                        echo "<h1>Success</h1>";
                        echo "<p>Funds were added.</p><br /><br />";
                    }
                }    
                
                // Get current funds
                $userId = $_SESSION['userId'];
                $fundsQuery = mysqli_query($conn, ""
                    . "SELECT * FROM Customer WHERE id = '".$userId."' ");
                
                // Error
                if (!$fundsQuery) {
                    echo "<h1>Error</h1>";
                    echo "<p>Failed accessing funds. <br /></p>";
                    echo "<code>", mysqli_error($conn), "</code>";
                
                // Success    
                } else {
                    $row = mysqli_fetch_array($fundsQuery);
                    $funds = $row['funds'];
                    $funds = ltrim($funds, '0');
                    
                    // Display currently availble funds
                    if ($funds == 0) {
                        echo "<h3>No funds available.</h3>";
                    } else {
                        echo "<h3>Currently available funds:</h3>";
                        echo "<p>$", $funds, "</p><br /><br />"; 
                    }
                }
        
                // Display text field and button for adding new funds
        ?>        
                <h1>Add Funds</h1>
                    <p>Please enter the amount of money you would like to add to your funds.</p>
                    
                    <!--Display add funds form-->
                    <form method="post" action="userFunds.php" 
                          name="addfundsform" id="addfundsform">
                        <fieldset>
                            <label for="addFunds">Add funds *:</label>
                            <input type="number" step='0.01' name="addFunds" id="addFunds"/><br /><br />
                            <input type="submit" name="addFundsSubmit" id="addFundsSubmit" value="Add" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
        
        <?php
            }
        ?>
 
        </div>            
    </body>
</html>