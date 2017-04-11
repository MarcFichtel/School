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
                <!--User Login-->
                <a id="userHomeButton" href="demoPage.php">User Home</a>
                
                <!--Employee Login-->
                <a id="employeeHomeButton" href="employeeLogin.php">Employee Home</a>
        </div>
            
        <?php
            // Someone is logged in
            if (!empty($_SESSION['LoggedIn']) && !empty($_SESSION['email'])) {
        ?>
                <!--Welcome Admin-->
                <h1>Admin Area</h1>
                <p>Logged in as: 
                    <?=$_SESSION['email']?>.
                </p><br /> 
                <table id="adminNaviTable">
                    <tr id="adminNaviHeader">
                        <td>Products</td>
                        <td>Departments</td>
                        <td>Employees</td>
                        <td>Other</td>
                    </tr>
                    <tr>
                        <td><br /><a href="createProduct.php">Create a product</a><br /><br /></td>
                        <td><br /><a href="createDepartment.php">Create a department</a><br /><br /></td>
                        <td><br /><a href="createEmployee.php">Create an employee</a><br /><br /></td>
                        <td><br /><a href="logout.php">Logout</a><br /><br /></td>
                    </tr>
                    <tr>
                        <td><br /><a href="editProduct.php">Edit a product</a><br /><br /></td>
                        <td><br /><a href="editDepartment.php">Edit a department</a><br /><br /></td>
                        <td><br /><a href="editEmployee.php">Edit an employee</a><br /><br /></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><br /><a href="deleteProduct.php">Delete a product</a><br /><br /></td>
                        <td><br /><a href="deleteDepartment.php">Delete a department</a><br /><br /></td>
                        <td><br /><a href="deleteEmployee.php">Delete an employee</a><br /><br /></td>
                        <td></td>
                    </tr>
                </table>
        <?php    
            // Someone is logging in 
            } else if (!empty($_POST['email']) && !empty($_POST['password'])) {
                
                // Get id, email & password, check DB
                $email = $_POST['email'];
                $password = $_POST['password'];
                $checkLogin = mysqli_query($conn, ""
                        . "SELECT id FROM Admin "
                        . "WHERE email = '".$email."' "
                        . "  AND password = '".$password."' ");
                
                $row = mysqli_fetch_array($checkLogin);
                $id = $row['id'];
                
                // If a match was found, log user in
                if (mysqli_num_rows($checkLogin) == 1) {
                    $_SESSION['adminId'] = $id;
                    $_SESSION['email'] = $email;
                    $_SESSION['LoggedIn'] = 1;
                    $_SESSION['adminuser'] = true;      // Usertype = admin
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
                        <input type="email" name="email" id="email" /><br />
                        <label for="password">Password:</label>
                        <input type="password" name="password" id="password" /><br />
                        <input type="submit" name="adminlogin" id="adminlogin" value="Login"/>
                    </fieldset>
                </form>
        <?php 
            }
        ?>
    
        </div>            
    </body>
</html>
