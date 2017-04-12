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
                if(!$_SESSION['adminuser'] && !$_SESSION['employeeuser']) {
                    echo "<h1>Error</h1>";
                    echo "<br /><p>You must be logged in as an admin or a manager to access this page.</p><br />";
                    echo "<br /><a href='adminLogin.php'>Go to Admin Login</a><br /><br />";
                    echo "<br /><a href='employeeLogin.php'>Go to Employee Login</a><br /><br />";
                    
                // User does have permission (i.e. is an admin or manager) and all required fields are filled in   
                } else if (!empty($_POST['email']) && !empty($_POST['reemail']) &&
                        !empty($_POST['refname']) && !empty($_POST['relname']) &&
                        !empty($_POST['rewage']) && !empty($_POST['rerole']) && !empty($_POST['remgrid'])){
                    
                    // Check if employee exists
                    $email = $_POST['email'];
                    $reemail = $_POST['reemail'];
                    $refname = $_POST['refname'];
                    $relname = $_POST['relname'];
                    $rewage = $_POST['rewage'];
                    $rerole = $_POST['rerole'];
                    $remgrid = $_POST['remgrid'];
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
                        echo "<br /><p>The employee you are trying to edit does not exist.</p><br />";
                        echo "<br /><a href='editEmployee.php'>Click here to try again</a><br /><br />";
                    
                    // Employee exists    
                    } else {
                        
                        // Edit employee query
                        $renameDepartmentQuery = mysqli_query($conn, ""
                            . "UPDATE Employee "
                            . "SET email = '".$reemail."', firstname = '".$refname."', lastname = '".$relname."', wage = '".$rewage."', permissions = '".$rerole."', manager_id = '".$remgrid."' "
                            . "WHERE email = '".$email."' ");

                        // Success
                        if ($renameDepartmentQuery) {
                            echo "<h1>Success</h1>";
                            echo "<br /><p>Employee edited.</p><br />"; 
                            echo "<br /><p><a href=\"editEmployee.php\">Click here to edit another employee.</a></p><br />";

                        // Error    
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Employee edit failed. Please try again. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";
                        } 
                    }
                
                // Required information missing
                } else {
            ?>
                    <h1>Edit an Employee</h1>
                    <p>Please enter employee name you want to edit, and new details.</p>
                    
                    <!--Display employee edit form-->
                    <form method="post" action="editEmployee.php" 
                          name="employeeeditform" id="employeeeditform">
                        <fieldset>
                            <label for="email">Email *:</label>
                            <input type="email" name="email" id="email"/><br /><br />
                            <label for="reemail">New Email *:</label>
                            <input type="email" name="reemail" id="reemail"/><br />
                            <label for="refname">New First Name *:</label>
                            <input type="text" name="refname" id="refname"/><br />
                            <label for="relname">New Last Name *:</label>
                            <input type="text" name="relname" id="relname"/><br />
                            <label for="rewage">New Wage *:</label>
                            <input type="text" name="rewage" id="rewage"/><br />
                            <label for="rerole">New Status *:</label>
                            <input type="text" name="rerole" id="rerole"/><br />
                            <label for="remgrid">New Manager ID *:</label>
                            <input type="text" name="remgrid" id="remgrid"/><br />
                            <input type="submit" name="editEmployee" id="editEmployee" value="Edit Employee" />
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