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
            
            <!--Admin Login-->
            <a id="adminHomeButton" href="adminLogin.php">Admin Home</a>
        </div>
            
        <?php
            // Someone is logged in
            if (!empty($_SESSION['LoggedIn']) && !empty($_SESSION['employeeemail'])) {
        ?>
                <!--Employee User-->
                <h1>Employee Area</h1>
                <p>Logged in as:  
                    <?=$_SESSION['employeeemail']?>.
                </p><br /><br />
                <br /><a href="editProduct.php">Edit a product</a><br /><br />
                <br /><a href="logout.php">Logout</a><br /><br />
        <?php    
            // Someone is logging in 
            } else if (!empty($_POST['employeeemail']) && !empty($_POST['password'])) {
               
                // Get email & password, check DB
                $employeeemail = $_POST['employeeemail'];
                $password = $_POST['password'];
                $checkLogin = mysqli_query($conn, ""
                        . "SELECT id, email, password FROM Employee "
                        . "WHERE email = '".$employeeemail."' "
                        . "  AND password = '".$password."' ");
                
                // If a match was found, log employee in
                if (mysqli_num_rows($checkLogin) == 1) {
                    $row = mysqli_fetch_array($checkLogin);
                    $_SESSION['employeeemail'] = $employeeemail;// Set email
                    $_SESSION['employeeId'] = $row['id'];       // Set id
                    $_SESSION['LoggedIn'] = 1;                  // LoggedIn = true
                    $_SESSION['employeeuser'] = true;           // Usertype = employee
                    echo "<h1>Success</h1>";
                    echo "<p>Redirecting to Employee Area...</p>";
                    echo "<meta http-equiv='refresh' content='2; employeeLogin.php'/>";
                
                // If no match was found, let user know    
                } else {
                    echo "<h1>Error</h1>";
                    echo "<br /><p>Employee Account was not found.<p/><br />";
                    echo "<br /><p><a href=\"employeeLogin.php\">Click here to try again.</a></p><br />";
                }
                
            // Nobody is logged in 
            } else {
        ?>     
                <h1>Employee Login</h1>
                <form method="POST" action="employeeLogin.php" name="employeeloginform" id="employeeloginform">
                    <fieldset>
                        <label for="employeeemail">Email:</label>
                        <input type="email" name="employeeemail" id="employeeemail" value="" /><br />
                        <label for="password">Password:</label>
                        <input type="password" name="password" id="password" value="" /><br />
                        <input type="submit" name="employeelogin" id="employeelogin" value="Login"/>
                    </fieldset>
                </form>
        <?php 
            }
        ?>
    
        </div>            
    </body>
</html>
