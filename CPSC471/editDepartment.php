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
                <h4>CPSC 471 - Group 9 - Final Project</h1>
                <h5>Authors: Mohamed-Hani Abdillah, Nikolai Aguierre, and Marc-Andre Fichtel</h2>
            </div>
                
            <!--Navgation-->
            <div id="navi">
                
                <!--User Login-->
                <a href="demoPage.php">User Home</a>
                
                <!--Admin Login-->
                <a href="adminLogin.php">Admin Home</a>
            </div>
                 
            <?php
                // User does not have permission
                if(!$_SESSION['adminuser']) {
                    echo "<h1>Error</h1>";
                    echo "<br /><p>You must be logged in as an admin to access this page.</p><br />";
                    echo "<br /><a href='adminLogin.php'>Go to Admin Login</a><br /><br />";
                    
                // User does have permission (i.e. is an admin) and all required fields are filled in   
                } else if (!empty($_POST['name']) && !empty($_POST['rename'])){
                    
                    // Check if department exists
                    $name = $_POST['name'];
                    $rename = $_POST['rename'];
                    $checkDepartmentQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Department WHERE name = '".$name."'"); 
                    
                    if (!$checkDepartmentQuery) {
                        echo "<h1>Error</h1>";
                        echo "<p>Couldn't access department. Please try again. <br /></p>";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    // Department does not exist
                    if (mysqli_num_rows($checkDepartmentQuery) == 0) {
                        echo "<h1>Error</h1>";
                        echo "<br /><p>The department you are trying to edit does not exist.</p><br />";
                        echo "<br /><a href='editDepartment.php'>Click here to try again</a><br /><br />";
                    
                    // Department exists    
                    } else {
                        
                        // Rename department query
                        $renameDepartmentQuery = mysqli_query($conn, ""
                            . "UPDATE Department "
                            . "SET name = '".$rename."' "
                            . "WHERE name = '".$name."' ");

                        // Success
                        if ($renameDepartmentQuery) {
                            echo "<h1>Success</h1>";
                            echo "<br /><p>Department renamed.</p><br />"; 
                            echo "<br /><p><a href=\"editDepartment.php\">Click here to rename another department.</a></p><br />";

                        // Error    
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Department edit failed. Please try again. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";
                        } 
                    }
                
                // User has not tried to edit a department yet    
                } else {
            ?>
                    <h1>Edit a Department</h1>
                    <p>Please enter department name you want to edit, and a new name.</p>
                    
                    <!--Display department creation form-->
                    <form method="post" action="editDepartment.php" 
                          name="departmenteditform" id="departmenteditform">
                        <fieldset>
                            <label for="name">Name *:</label>
                            <input type="text" name="name" id="name"/><br />
                            <label for="rename">New Name *:</label>
                            <input type="text" name="rename" id="rename"/><br />
                            <input type="submit" name="editDepartment" id="editDepartment" value="Rename Department" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
                    <h4>Existing departments:</h4>
                <?php
                    // Get department names
                    $deps = mysqli_query($conn, ""
                            . "SELECT name FROM Department");
                    
                    // Display department names
                    if ($deps) {
                        while ($row = mysqli_fetch_array($deps)) {
                            $name = $row['name'];
                            echo $name, "<br />";
                        }    
                        
                    // Error getting department names    
                    } else {
                        echo "Error retrieving departments.";
                    }
                }
                ?>
        </div>    
    </body>
</html>