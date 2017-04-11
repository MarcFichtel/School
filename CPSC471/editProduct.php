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
                    echo "<br /><p>You must be logged in as an admin or employee to access this page.</p><br />";
                    echo "<br /><a href='adminLogin.php'>Go to Admin Login</a><br /><br />";
                    echo "<br /><a href='employeeLogin.php'>Go to Employee Login</a><br /><br />";
                    
                // User does have permission (i.e. is an admin) and all required fields are filled in   
                } else if (!empty($_POST['name']) && !empty($_POST['rename']) &&
                        !empty($_POST['reprice']) && !empty($_POST['restock']) &&
                        !empty($_POST['redepartment'])){
                    
                    // Check if product exists
                    $name = $_POST['name'];
                    $rename = $_POST['rename'];
                    $reprice = $_POST['reprice'];
                    $restock = $_POST['restock'];
                    $redepartment = $_POST['redepartment'];
                    if (!empty($_POST['redescription'])) {
                        $redescription = $_POST['redescription'];
                    }
                    $checkProductQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Product WHERE name = '".$name."'"); 
                    
                    if (!$checkProductQuery) {
                        echo "<h1>Error</h1>";
                        echo "<p>Couldn't access product. Please try again. <br /></p>";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    // Product does not exist
                    if (mysqli_num_rows($checkProductQuery) == 0) {
                        echo "<h1>Error</h1>";
                        echo "<br /><p>The product you are trying to edit does not exist.</p><br />";
                        echo "<br /><a href='editProduct.php'>Click here to try again</a><br /><br />";
                    
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
                        
                            // Error: Products belong to a transaction (so cannot edit)
                            if (mysqli_num_rows($transactionProductsQuery) != 0) {
                                echo "<h1>Error</h1>";
                                echo "<p>Cannot edit products belonging to transactions. <br /></p>";
                                echo "<br /><p><a href=\"editProduct.php\">Click here to try again.</a></p><br />";

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
                                
                                    // Error: Products belong to a shopping cart (so cannot edit)
                                    if (mysqli_num_rows($cartProductsQuery) != 0) {
                                        echo "<h1>Error</h1>";
                                        echo "<p>Cannot edit products belonging to shopping carts. <br /></p>";
                                        echo "<br /><p><a href=\"editProduct.php\">Click here to try again.</a></p><br />";
                                    
                                    // Product does not belong to any shopping carts    
                                    } else {
                        
                                        // Get product department id
                                        $depIDQuery = mysqli_query($conn, ""
                                                . "SELECT id FROM Department WHERE name = '".$redepartment."' ");
                                        if (!$depIDQuery) {
                                            echo "Error getting dep ID";
                                        }
                                        $row = mysqli_fetch_array($depIDQuery);
                                        $depID = $row['id'];
                                        
                                        // Edit product query (with or without description)
                                        if (!empty($_POST['redescription'])) {
                                            $editProductQuery = mysqli_query($conn, ""
                                                . "UPDATE Product "
                                                . "SET name = '".$rename."', price = '".$reprice."', stock = '".$restock."', department_id = '".$depID."', description = '".$redescription."' "
                                                . "WHERE name = '".$name."' ");
                                        } else {
                                           $editProductQuery = mysqli_query($conn, ""
                                            . "UPDATE Product "
                                            . "SET name = '".$rename."', price = '".$reprice."', stock = '".$restock."', department_id = '".$depID."' "
                                            . "WHERE name = '".$name."' ");
                                        }
                                        // Success
                                        if ($editProductQuery) {
                                            echo "<h1>Success</h1>";
                                            echo "<br /><p>Product edited.</p><br />"; 
                                            echo "<br /><p><a href=\"editProduct.php\">Click here to edit another product.</a></p><br />";

                                        // Error    
                                        } else {
                                            echo "<h1>Error</h1>";
                                            echo "<p>Product edit failed. Please try again. <br /></p>";
                                            echo "<code>", mysqli_error($conn), "</code>";
                                        } 
                                    }
                                }
                            }
                        }
                    }
                
                // User has not tried to edit a product yet    
                } else {
            ?>
                    <h1>Edit a Product</h1>
                    <p>Please enter the name of the product you want to edit, and the new details.</p>
                    
                    <!--Display product edit form-->
                    <form method="post" action="editProduct.php" 
                          name="producteditform" id="producteditform">
                        <fieldset>
                            <label for="name">Name *:</label>
                            <input type="text" name="name" id="name"/><br /><br />
                            <label for="rename">New Name *:</label>
                            <input type="text" name="rename" id="rename"/><br />
                            <label for="reprice">New Price *:</label>
                            <input type="text" name="reprice" id="reprice"/><br />
                            <label for="restock">New Stock *:</label>
                            <input type="text" name="restock" id="restock"/><br />
                            <label for="redepartment">New Department *:</label>
                            <input type="text" name="redepartment" id="redepartment"/><br />
                            <label for="redescription">New Description:</label>
                            <input type="text" name="redescription" id="redescription"/><br />
                            <input type="submit" name="editProduct" id="editProduct" value="Edit Product" />
                            <p><br /><br />Note: Fields with * are required.</p><br />
                        </fieldset>
                    </form>
                    <h4>Existing products:</h4>
                <?php
                    // Get product details
                    $prods = mysqli_query($conn, ""
                            . "SELECT * FROM Product");
                    
                    // Display product details
                    if ($prods) {
                        // Display table with products
                        echo "<table id='productDisplayTable'>";
                        echo ""
                            . "<tr>"
                                . "<td><strong>Name</strong></td>"
                                . "<td><strong>Price</strong></td>"
                                . "<td><strong>Stock</strong></td>"
                                . "<td><strong>Department</strong></td>"
                                . "<td><strong>Description</strong></td>"
                            . "</tr>";
                        while ($row = mysqli_fetch_array($prods)) {
                            $depId = $row['department_id'];
                            $name = $row['name'];
                            $price = $row['price'];
                            $stock = $row['stock'];
                            $description = $row['description'];
                            $depNameQuery = mysqli_query($conn, ""
                                    . "SELECT name FROM Department WHERE id = '".$depId."' ");
                            $row2 = mysqli_fetch_array($depNameQuery);
                            $depName = $row2['name'];
                            echo ""
                            . "<tr>"
                                 . "<td>".$name."</td>"
                                 . "<td>".$price."</td>"
                                 . "<td>".$stock."</td>"
                                 . "<td>".$depName."</td>"  
                                 . "<td>".$description."</td>"
                            . "</tr>";
                        }
                        echo "</table>";
                        
                    // Error getting product infos   
                    } else {
                        echo "Error retrieving departments.";
                    }
                    // Display only available departments for managers (not logged in as admin)
                    if ($_SESSION['employeeuser'] && !$_SESSION['adminuser']) {
                        
                        // Get managing department names
                        $empId = $_SESSION['employeeId'];
                        $deps = mysqli_query($conn, ""
                            . "SELECT name FROM Department WHERE manager_id = '".$empId."' ");
                        echo "<h4>Departments you are managing:</h4>";
                    
                    // Display all departments for admin (may be logged in as employee)   
                    } else {
                        
                        $deps = mysqli_query($conn, ""
                            . "SELECT name FROM Department");
                        echo "<h4>Existing departments:</h4>";
                    }
                    
                    
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