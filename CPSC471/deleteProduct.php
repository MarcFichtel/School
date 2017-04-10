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
                } else if (!empty($_POST['name'])){
                    
                    // Check if product exists
                    $name = $_POST['name'];
                    $checkProductQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Product WHERE name = '".$name."'"); 
                    
                    if (!$checkProductQuery) {
                        echo "<h1>Error</h1>";
                        echo "<p>Couldn't access products. Please try again. <br /></p>";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    // Product does not exist
                    if (mysqli_num_rows($checkProductQuery) == 0) {
                        echo "<h1>Error</h1>";
                        echo "<br /><p>The product you are trying to delete does not exist.</p><br />";
                        echo "<br /><a href='deleteProduct.php'>Click here to try again</a><br /><br />";
                    
                    // Product exists    
                    } else {
                        
                        // Check if product is in any transactions
                        $row = mysqli_fetch_array($checkProductQuery);
                        $prodId = $row['id'];
                        $transactionProductsQuery = mysqli_query($conn, ""
                                . "SELECT * FROM TransactionProducts WHERE product_id = '".$prodId."' ");
                        
                        // Error accessing transaction products
                        if (!$transactionProductsQuery) {
                            echo "<h1>Error</h1>";
                            echo "<p>Couldn't access transaction products. Please try again. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";
                        
                        // Transaction products accessed successfully    
                        } else {
                        
                            // Error: Products belong to a transaction (so cannot delete)
                            if (mysqli_num_rows($transactionProductsQuery) != 0) {
                                echo "<h1>Error</h1>";
                                echo "<p>Cannot delete products belonging to transactions. <br /></p>";
                                echo "<br /><p><a href=\"deleteProduct.php\">Click here to try again.</a></p><br />";

                            // Product does not belong to any transaction    
                            } else {
                                
                                // Check if product is in any shopping carts
                                $cartProductsQuery = mysqli_query($conn, ""
                                . "SELECT * FROM ShoppingCart WHERE product_id = '".$prodId."' ");

                                // Error accessing cart products
                                if (!$cartProductsQuery) {
                                    echo "<h1>Error</h1>";
                                    echo "<p>Couldn't access shopping cart products. Please try again. <br /></p>";
                                    echo "<code>", mysqli_error($conn), "</code>";

                                // Cart products accessed successfully    
                                } else {
                                
                                    // Error: Products belong to a shopping cart (so cannot delete)
                                    if (mysqli_num_rows($cartProductsQuery) != 0) {
                                        echo "<h1>Error</h1>";
                                        echo "<p>Cannot delete products belonging to shopping carts. <br /></p>";
                                        echo "<br /><p><a href=\"deleteProduct.php\">Click here to try again.</a></p><br />";
                                    
                                    // Product does not belong to any shopping carts    
                                    } else {
                                        // Delete product query
                                        $deleteProductQuery = mysqli_query($conn, ""
                                            . "DELETE FROM Product WHERE name = '".$name."' ");

                                        // Success
                                        if ($deleteProductQuery) {
                                            echo "<h1>Success</h1>";
                                            echo "<br /><p>Product deleted.</p><br />"; 
                                            echo "<br /><p><a href=\"deleteProduct.php\">Click here to delete another product.</a></p><br />";

                                        // Error    
                                        } else {
                                            echo "<h1>Error</h1>";
                                            echo "<p>Product deletion failed. Please try again. <br /></p>";
                                            echo "<code>", mysqli_error($conn), "</code>";
                                        } 
                                    }
                                }
                            }
                        }
                    }
                
                // User has not tried to delete a product yet    
                } else {
            ?>
                    <h1>Delete a Product</h1>
                    <p>Please enter product name.</p>
                    <p>Note: To be able to delete a product, it cannot be in any transaction or shopping cart.</p><br />
                    <p>Any reviews the product might have will be deleted alongside it.</p><br />
                    
                    <!--Display department creation form-->
                    <form method="post" action="deleteProduct.php" 
                          name="productdeletionform" id="productdeletionform">
                        <fieldset>
                            <label for="name">Name *:</label>
                            <input type="text" name="name" id="name"/><br />
                            <input type="submit" name="deleteProduct" id="deleteProduct" value="Delete Product" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
                    <h4>Existing products:</h4>
                <?php
                    // Get product names
                    $products = mysqli_query($conn, ""
                            . "SELECT name FROM Product");
                    
                    // Display product names
                    if ($products) {
                        while ($row = mysqli_fetch_array($products)) {
                            $name = $row['name'];
                            echo $name, "<br />";
                        }    
                        
                    // Error getting product names    
                    } else {
                        echo "Error retrieving products.";
                    }
                }
                ?>
        </div>    
    </body>
</html>