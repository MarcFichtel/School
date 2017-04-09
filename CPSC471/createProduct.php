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
                
                <!--User Login-->
                <a href="demoPage.php">User Home</a>
                
                <!--Admin Login-->
                <a href="adminLogin.php">Admin Home</a>
            </div>
                 
            <?php
                // User does not have permission
                if(!$_SESSION['adminuser']) {
                    echo "<h1>Error</h1>";
                    echo "<p>You must be logged in as an admin to access this page.</p>";
                
                // User does have permission (i.e. is an admin) and all required fields are filled in   
                } else if (!empty($_POST['name']) && 
                        !empty($_POST['price']) &&
                        !empty($_POST['stock'])) {
                    
                    // Get form inputs & create SQL query
                    $name = $_POST['name'];
                    $price = $_POST['price'];
                    $stock = $_POST['stock'];
                    if (!empty($_POST['description'])) {
                        $description = $_POST['description'];   // Description may be blank
                    }
                    
                    // Description was given
                    if (!empty($_POST['description'])) {
                        $createProductQuery = mysqli_query($conn, ""
                            . "INSERT INTO Product (name, price, stock, description, admin_id)"
                            . "VALUES ('".$name."', '".$price."', '".$stock."', '".$description."', 1)");
                    
                    // Description was left blank    
                    } else {
                        $createProductQuery = mysqli_query($conn, ""
                            . "INSERT INTO Product (name, price, stock, admin_id)"
                            . "VALUES ('".$name."', '".$price."', '".$stock."', 1)");
                    }

                    
                    // Success
                    if ($createProductQuery) {
                        echo "<h1>Success</h1>";
                        echo "<br /><p>Product created.</p><br />"; 
                        echo "<br /><p><a href=\"createProduct.php\">Click here to create another product.</a></p><br />";
                    
                    // Error    
                    } else {
                        echo "<h1>Error</h1>";
                        echo "<p>Product creation failed. Please try again.</p>";
                        echo mysqli_error($conn);
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
                            <label for="name">* Name:</label>
                            <input type="text" name="name" id="name"/><br />
                            <label for="price">* Price:</label>
                            <input type="text" name="price" id="price"/><br />
                            <label for="stock">* Stock:</label>
                            <input type="text" name="stock" id="stock"/><br />
                            <label for="description">Description:</label>
                            <input type="text" name="description" id="description"/><br />
                            <input type="submit" name="createProduct" id="createProduct" value="Create Product" />
                            <p>Note: Fields with * are required.</p>
                        </fieldset>
                    </form>
                
                <?php
                }
                ?>
        </div>    
    </body>
</html>