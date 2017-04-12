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
                
                <!--Employee Login-->
                <a id="employeeHomeButton" href="employeeLogin.php">Employee Home</a>
                
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
                } else if (!empty($_POST['email'])){
                    
                    // Check if employee exists
                    $email = $_POST['email'];
                    $checkEmployeeQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Employee WHERE email = '".$email."'"); 
                    
                    if (!$checkEmployeeQuery) {
                        echo "<h1>Error</h1>";
                        echo "<p>Couldn't access employees. Please try again. <br /></p>";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    // Employee does not exist
                    if (mysqli_num_rows($checkEmployeeQuery) == 0) {
                        echo "<h1>Error</h1>";
                        echo "<br /><p>The employee you are trying to delete does not exist.</p><br />";
                        echo "<br /><a href='deleteEmployee.php'>Click here to try again</a><br /><br />";
                    
                    // Employee exists    
                    } else {
                        // Delete Employee query
                        $deleteEmployeeQuery = mysqli_query($conn, ""
                            . "DELETE FROM Employee WHERE email = '".$email."' ");

                        // Success
                        if ($deleteEmployeeQuery) {
                            echo "<h1>Success</h1>";
                            echo "<br /><p>Employee deleted.</p><br />"; 
                            echo "<br /><p><a href=\"deleteEmployee.php\">Click here to delete another employee.</a></p><br />";

                        // Error    
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Employee deletion failed. Please try again. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";
                        } 
                    }
                
                // User has not tried to delete an employee yet    
                } else {
            ?>
                    <h1>Delete an Employee</h1>
                    <p>Please enter employee email.</p>
                    
                    <!--Display employee deletion form-->
                    <form method="post" action="deleteEmployee.php" 
                          name="employeedeletionform" id="employeedeletionform">
                        <fieldset>
                            <label for="email">Email *:</label>
                            <input type="email" name="email" id="email"/><br />
                            <input type="submit" name="deleteEmployee" id="deleteEmployee" value="Delete Employee" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
                <?php
                    // Display employees
                    $emps = mysqli_query($conn, ""
                            . "SELECT * FROM Employee");
                    echo "<h4>Existing employee accounts:</h4>";
                    echo "<table id = employeesTable>";
                        echo "<tr>";
                            echo "<td>Email Address</td>";
                            echo "<td>First Name</td>";
                            echo "<td>Last Name</td>";
                            echo "<td>Annual Wage</td>";
                            echo "<td>Permission Status</td>";
                            echo "<td>Manager ID</td>";
                        echo "</tr>";
                    if ($emps) {
                        while ($row = mysqli_fetch_array($emps)) {
                            $email = $row['email'];
                            $fname = $row['firstname'];
                            $lname = $row['lastname'];
                            $wage = $row['wage'];
                            $role = $row['permissions'];
                            $mgrId = $row['manager_id'];
                            echo "<tr>";
                                echo "<td>".$email."</td>";
                                echo "<td>".$fname."</td>";
                                echo "<td>".$lname."</td>";
                                echo "<td>$".$wage."</td>";
                                echo "<td>".$role."</td>";
                                echo "<td>".$mgrId."</td>";
                            echo "</tr>";
                        }    
                        echo "</table>";
                        
                    // Error getting employee names    
                    } else {
                        echo "Error retrieving employees.";
                    }
                }
                ?>
        </div>    
    </body>
</html>