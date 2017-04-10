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
                <a id="userHomeButton" href="demoPage.php">User Home</a>
                
                <!--Admin Login-->
                <a id="adminHomeButton" href="adminLogin.php">Admin Home</a>
            </div>
                 
            <?php
                // User does not have permission
                if(!$_SESSION['adminuser']) {
                    echo "<h1>Error</h1>";
                    echo "<br /><p>You must be logged in as an admin to access this page.</p><br />";
                    echo "<br /><a href='adminLogin.php'>Go to Admin Login</a><br /><br />";
                    
                // User does have permission (i.e. is an admin) and all required fields are filled in   
                } else if (!empty($_POST['name'])){
                    
                    // Check if department exists
                    $name = $_POST['name'];
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
                        echo "<br /><p>The department you are trying to delete does not exist.</p><br />";
                        echo "<br /><a href='deleteDepartment.php'>Click here to try again</a><br /><br />";
                    
                    // Department exists    
                    } else {
                        
                        // Check if any products belong to department
                        $row = mysqli_fetch_array($checkDepartmentQuery);
                        $depId = $row['id'];
                        $productsQuery = mysqli_query($conn, ""
                                . "SELECT * FROM Product WHERE department_id = '".$depId."' ");
                        
                        // Error accessing products
                        if (!$productsQuery) {
                            echo "<h1>Error</h1>";
                            echo "<p>Couldn't access department products. Please try again. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";
                        
                        // Products accessed successfully    
                        } else {
                        
                            // Error: Products belong to department (so cannot delete)
                            if (mysqli_num_rows($productsQuery) != 0) {
                                echo "<h1>Error</h1>";
                                echo "<p>Cannot delete department with products in it. <br /></p>";
                                echo "<br /><p><a href=\"deleteDepartment.php\">Click here to try again.</a></p><br />";

                            // Department is empty    
                            } else {
                                // Delete department query
                                $deleteDepartmentQuery = mysqli_query($conn, ""
                                    . "DELETE FROM Department WHERE name = '".$name."' ");

                                // Success
                                if ($deleteDepartmentQuery) {
                                    echo "<h1>Success</h1>";
                                    echo "<br /><p>Department deleted.</p><br />"; 
                                    echo "<br /><p><a href=\"deleteDepartment.php\">Click here to delete another department.</a></p><br />";

                                // Error    
                                } else {
                                    echo "<h1>Error</h1>";
                                    echo "<p>Department deletion failed. Please try again. <br /></p>";
                                    echo "<code>", mysqli_error($conn), "</code>";
                                } 
                            }
                        }
                        
                    }
                
                // User has not tried to delete a department yet    
                } else {
            ?>
                    <h1>Delete a Department</h1>
                    <p>Please enter department name.</p>
                    <p>Note: To be able to delete a department, it must be empty (i.e. no products belong to it).</p><br />
                    
                    <!--Display department creation form-->
                    <form method="post" action="deleteDepartment.php" 
                          name="departmentdeletionform" id="departmentdeletionform">
                        <fieldset>
                            <label for="name">Name *:</label>
                            <input type="text" name="name" id="name"/><br />
                            <input type="submit" name="deleteDepartment" id="deleteDepartment" value="Delete Department" />
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