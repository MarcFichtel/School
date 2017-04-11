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
                    
                    // Get current admin's email and id
                    $adminId = $_SESSION['adminId'];
                    
                    // Check if department exists
                    $email = $_POST['email'];
                    $password = $_POST['password'];
                    $firstname = $_POST['firstname'];
                    $lastname = $_POST['lastname'];
                    $wage = $_POST['wage'];
                    $permissions = $_POST['permissions'];
                    $checkEmployeeQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Employee WHERE email = '".$email."'");
                    
                    if (!$checkEmployeeQuery) {
                        echo "<h1>Error</h1>";
                        echo "<p>Employee check failed. Please try again. <br /></p>";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    if (mysqli_num_rows($checkEmployeeQuery) > 0) {
                        echo "<h1>Error</h1>";
                        echo "<br /><p>An employee with that email already exists.</p><br />";
                        echo "<br /><p><a href=\"createEmployee.php\">Click here to try again.</a></p><br />";
                        
                    // Employee does not exist yet    
                    } else {
                        
                        $createEmployeeQuery = mysqli_query($conn, ""
                        . "INSERT INTO Employee (email, password, firstname, lastname, wage, permissions, manager_id)"
                        . "VALUES ('".$email."', '".$password."', '".$firstname."', '".$lastname."', '".$wage."', '".$permissions."', '".$adminId."')");

                        // Success
                        if ($createEmployeeQuery) {
                            echo "<h1>Success</h1>";
                            echo "<br /><p>Employee created.</p><br />"; 
                            echo "<br /><p><a href=\"createEmployee.php\">Click here to create another employee.</a></p><br />";

                        // Error    
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Employee creation failed. Please try again. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";
                        }
                    }
                    
                // User has not tried to create an employee yet    
                } else {
            ?>
                    <h1>Create an Employee</h1>
                    <p>Please enter employee details.</p>
                    
                    <!--Display employee creation form-->
                    <form method="post" action="createEmployee.php" 
                          name="employeecreationform" id="employeecreationform">
                        <fieldset>
                            <label for="email">Email *:</label>
                            <input type="email" name="email" id="email"/><br />
                            <label for="password">Password *:</label>
                            <input type="text" name="password" id="password"/><br />
                            <label for="firstname">First Name *:</label>
                            <input type="text" name="firstname" id="firstname"/><br />
                            <label for="lastname">Last Name *:</label>
                            <input type="text" name="lastname" id="lastname"/><br />
                            <label for="wage">Wage *:</label>
                            <input type="text" name="wage" id="wage"/><br />
                            <label for="permissions">Role *:</label>
                            <input type="text" name="permissions" id="permissions"/><br />
                            <input type="submit" name="createEmployee" id="createEmployee" value="Create Employee" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
                    <h4>Existing employee accounts:</h4>
                <?php
                    // Get employee names
                    $emps = mysqli_query($conn, ""
                            . "SELECT email FROM Employee");
                    
                    // Display employee names
                    if ($emps) {
                        while ($row = mysqli_fetch_array($emps)) {
                            $email = $row['email'];
                            echo $email, "<br />";
                        }    
                        
                    // Error getting employee names    
                    } else {
                        echo "Error retrieving employee.";
                    }
                }
                ?>
        </div>    
    </body>
</html>