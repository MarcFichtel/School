<?php include "base.php"; ?>

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
                
            <!--Home-->
            <a href="index.php">Home</a>

            <!--Admin Login-->
            <a href="demoPage.php">User Login</a>
        </div>
            
        <?php
            // Someone is logged in
            if (!empty($_SESSION['LoggedIn']) && !empty($_SESSION['email'])) {
        ?>
                <!--Welcome User-->
                <h1>Admin Area</h1>
                <p>Logged in as: 
                    <?=$_SESSION['email']?>.
                </p><br /> 
                <a href="logout.php">Logout</a><br /> <br /> 
        <?php    
            // Someone is logging in 
            } else if (!empty($_POST['email']) && !empty($_POST['password'])) {
                
                // Get email & password, check DB
                $email = $_POST['email'];
                $password = $_POST['password'];
                $checkLogin = mysqli_query($conn, ""
                        . "SELECT email, password FROM Admin "
                        . "WHERE email = '".$email."' "
                        . "  AND password = '".$password."' ");
                
                // If a match was found, log user in
                if (mysqli_num_rows($checkLogin) == 1) {
                    $_SESSION['email'] = $email;
                    $_SESSION['LoggedIn'] = 1;
                    echo "<h1>Success</h1>";
                    echo "<p>Redirecting to Admin Area...</p>";
                    echo "<meta http-equiv='refresh' content='2; adminLogin.php'/>";
                
                // If no match was found, let user know    
                } else {
                    echo "<h1>Error</h1>";
                    echo "<p>Admin Account was not found.</p><br /> ";
                    echo "<br /><p><a href=\"adminLogin.php\">Click here to try again.</a></p><br /> ";
                }
                 
            // Nobody is logged in 
            } else {
        ?>     
                <h1>Admin Login</h1>
                <form method="POST" action="adminLogin.php" name="adminloginform" id="adminloginform">
                    <fieldset>
                        <label for="email">Email:</label>
                        <input type="text" name="email" id="email" /><br />
                        <label for="password">Password:</label>
                        <input type="text" name="password" id="password" /><br />
                        <input type="submit" name="adminlogin" id="adminlogin" value="Login"/>
                    </fieldset>
                </form>
        <?php 
            }
        ?>
    
        </div>            
    </body>
</html>
