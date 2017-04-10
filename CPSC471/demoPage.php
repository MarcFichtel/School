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
                
            <!--Admin Login-->
            <a id="adminHomeButton" href="adminLogin.php">Admin Home</a>
        </div>
            
        <?php
            // Someone is logged in
            if (!empty($_SESSION['LoggedIn']) && !empty($_SESSION['Username'])) {
        ?>
                <!--Welcome User-->
                <h1>User Area</h1>
                <p>Welcome 
                    <?=$_SESSION['Username']?>.
                </p><br /><br />
                <p>Select a department to start shopping.</p><br />
                
                <?php
                    // Get department names
                    $dropdownquery = mysqli_query($conn, "SELECT name FROM Department");
                    
                    // Populate dropwdown with department names
                    // Redirect and POST chosen department to display.php
                    echo "<form id='displaydropdownform' action='/cpsc471.com/display.php' method='POST'>";
                    echo "<select id='displaydropdown' name='displaydropdown'>";
                    echo "<option selected>Select Department</option>";
                        while ($row = mysqli_fetch_array($dropdownquery)) {
                            echo "<option value='{$row['name']}'>{$row['name']}</option>";
                        }
                    echo "</select>";
                    echo "<input type='submit' name='submit' value='Submit' />";
                    echo "</form>";

                    // Shopping Cart
//                    <div id="cart" style="text-align: right">
//                        <img src="https://d30y9cdsu7xlg0.cloudfront.net/png/28468-200.png"
//                         alt="Shopping Cart"
//                         style="width: 100px; height: 100px">
//                        <br/>
//                        <a>Items in Cart: X</a>
//                    </div>
                    ?>  
                
                <br /><a href="transactions.php">My Transactions</a><br /><br />
                <br /><a href="logout.php">Logout</a><br /><br />
        <?php    
            // Someone is logging in 
            } else if (!empty($_POST['username']) && !empty($_POST['password'])) {
               
                // Get username & password, check DB
                $username = $_POST['username'];
                $password = $_POST['password'];
                $checkLogin = mysqli_query($conn, ""
                        . "SELECT username, password FROM Customer "
                        . "WHERE username = '".$username."' "
                        . "  AND password = '".$password."' ");
                
                // If a match was found, log user in
                if (mysqli_num_rows($checkLogin) == 1) {
                    $_SESSION['Username'] = $username;      // Set username
                    $_SESSION['LoggedIn'] = 1;              // LoggedIn = true
                    $_SESSION['customeruser'] = true;       // Usertype = customer
                    $_SESSION['activeDepartment'] = "None"; // Initialize active department
                    echo "<h1>Success</h1>";
                    echo "<p>Redirecting to User Area...</p>";
                    echo "<meta http-equiv='refresh' content='2; demoPage.php'/>";
                
                // If no match was found, let user know    
                } else {
                    echo "<h1>Error</h1>";
                    echo "<br /><p>User Account was not found.<p/><br />";
                    echo "<br /><p><a href=\"demoPage.php\">Click here to try again.</a></p><br />";
                }
                
            // Nobody is logged in 
            } else {
        ?>     
                <h1>User Login</h1>
                <form method="POST" action="demoPage.php" name="userloginform" id="userloginform">
                    <fieldset>
                        <label for="username">Username:</label>
                        <input type="text" name="username" id="username" /><br />
                        <label for="password">Password:</label>
                        <input type="text" name="password" id="password" /><br />
                        <input type="submit" name="userlogin" id="userlogin" value="Login"/>
                    </fieldset>
                </form>
                <br /><p>Not a user yet? <a href="newUser.php">Click here to register.</a></p><br />
        <?php 
            }
        ?>
    
        </div>            
    </body>
</html>
