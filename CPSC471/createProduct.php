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
                } else if (!empty($_POST['name']) && 
                        !empty($_POST['price']) &&
                        !empty($_POST['stock']) && 
                        !empty($_POST['department'])){
                    
                    // Get form inputs & create SQL query
                    $name = $_POST['name'];
                    $price = $_POST['price'];
                    $stock = $_POST['stock'];
                    $department = $_POST['department'];
                    if (!empty($_POST['description'])) {
                        $description = $_POST['description'];   // Description may be blank
                    }
                    
                    // Description was given
                    if (!empty($_POST['description'])) {
                        $createProductQuery = mysqli_query($conn, ""
                            . "INSERT INTO Product (name, price, stock, description, admin_id, department_id)"
                            . "VALUES ('".$name."', '".$price."', '".$stock."', '".$description."', 1, '".$department."')");
                    
                    // Description was left blank    
                    } else {
                        $createProductQuery = mysqli_query($conn, ""
                            . "INSERT INTO Product (name, price, stock, admin_id, department_id)"
                            . "VALUES ('".$name."', '".$price."', '".$stock."', 1, '".$department."')");
                    }

                    // Success
                    if ($createProductQuery) {
                        echo "<h1>Success</h1>";
                        echo "<br /><p>Product created.</p><br />"; 
                        echo "<br /><p><a href=\"createProduct.php\">Click here to create another product.</a></p><br />";
                    
                    // Error    
                    } else {
                        echo "<h1>Error</h1>";
                        echo "<p>Product creation failed. Please try again. <br />Did you enter the department name instead of the ID?</p>";
                        echo "<code>", mysqli_error($conn), "</code>";
                    } 
                
                // User has not tried to create a product yet    
                } else {
            ?>
                    <h1>Create a Product</h1>
                    <p>Please enter product details.</p>
                    
                    <!--Display product creation form-->
                    <form method="post" action="createProduct.php" 
                          name="productcreationform" id="productcreationform">
                        <fieldset>
                            <label for="name">Name *:</label>
                            <input type="text" name="name" id="name"/><br />
                            <label for="price">Price *:</label>
                            <input type="text" name="price" id="price"/><br />
                            <label for="stock">Stock *:</label>
                            <input type="text" name="stock" id="stock"/><br />
                            <label for="department">Department ID*:</label>
                            <input type="text" name="department" id="department"/><br />
                            <label for="description">Description:</label>
                            <input type="text" name="description" id="description"/><br />
                            <input type="submit" name="createProduct" id="createProduct" value="Create Product" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
                    <h4>Available departments:</h4>
                <?php
                    // Get department names
                    $deps = mysqli_query($conn, ""
                            . "SELECT id, name FROM Department");
                    
                    // Display department names
                    if ($deps) {
                        while ($row = mysqli_fetch_array($deps)) {
                            $id = $row['id'];
                            $name = $row['name'];
                            echo $name, " = ID: ", $id, "<br />";
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