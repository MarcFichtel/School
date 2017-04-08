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
                <h4>CPSC 471 - Group 9 - Final Project</h1>
                <h5>Authors: Mohamed-Hani Abdillah, Nikolai Aguierre, and Marc-Andre Fichtel</h2>
            </div>
                
            <!--Navgation-->
            <div id="navi">

                <!--Home-->
                <a href="index.php">Home</a>
                
                <!--User Login-->
                <a href="demoPage.php.php">User Login</a>
                
                <!--Admin Login-->
                <a href="adminLogin.php">Admin Login</a>
            </div>
                 
            <?php
                
                // User is trying to register
                if (!empty($_POST['username']) && !empty($_POST['password']) &&
                    !empty($_POST['firstname']) && !empty($_POST['lastname'])) {
                    
                    // Get form contents
                    $username = mysqli_real_escape_string($_POST['username']);
                    $password = md5(mysqli_real_escape_string($_POST['password']));
                    $firstname = mysqli_real_escape_string($_POST['firstname']);
                    $lastname = mysqli_real_escape_string($_POST['lastname']);
                    $birthdate = mysqli_real_escape_string($_POST['birthdate']);
                    
                    // Check if username is already taken
                    $checkUsername = mysqli_query($conn, ""
                            . "SELECT * FROM Customer"
                            . "WHERE username = '".$username."'");
                    
                    // Username already taken
                    if (mysqli_num_rows($checkUsername) == 1) {
                        echo "<h1>Error</h1>";
                        echo "<p>Username already taken. Please try again.</p>";
                        
                    // Username available, create account
                    } else {
                        $registerQuery = mysqli_query($conn, ""
                                . "INSERT INTO "
                                . "Customer (id, username, password, firstname, lastname, birthdate, funds, admin_id)"
                                . "VALUES ( NULL, "                 // id auto-increments
                                . "         '".$username."', "      // insert username
                                . "         '".$password."', "      // insert password
                                . "         '".$firstname."', "     // insert first name
                                . "         '".$lastname."', "      // insert last name
                                . "         '".$birthdate."'), "    // insert birth date
                                . "         0, "                    // default funds = 0
                                . "         1");                    // default admin id = 1
                                
                    
                        // Success
                        if ($registerQuery) {
                            echo "<h1>Success</h1>";
                            echo "<p>Account created. <a href=\"demoPage.php\">Click here to login.</a></p>";
                        
                        // Error    
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Registration failed. Please try again.</p>";
                        }
                    }
                
                // User has not tried to register yet  
                } else {
            ?>
                    <h1>Register</h1>
                    <p>Enter details to register.</p>
                    
                    <!--Display registration form-->
                    <form method="post" action="newUser.php" 
                          name="userregisterform" id="userregisterform">
                        <fieldset>
                            <label for="username">Username:</label>
                            <input type="text" name="username" id="username"/><br />
                            <label for="password">Password:</label>
                            <input type="text" name="password" id="password"/><br />
                            <label for="firstname">First Name:</label>
                            <input type="text" name="firstname" id="firstname"/><br />
                            <label for="lastname">Last Name:</label>
                            <input type="text" name="lastname" id="lastname"/><br />
                            <label for="birthdate">Birth Date:</label>
                            <input type="text" name="birthdate" id="birthdate"/><br />
                            <input type="submit" name="register" id="register" value="Register" />
                        </fieldset>
                    </form>
            <?php        
                }   
            ?>

        </div>    
    </body>
</html>