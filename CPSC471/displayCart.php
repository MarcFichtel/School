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
                
                <!--Admin Login-->
                <a id="adminHomeButton" href="adminLogin.php">Admin Home</a>
            </div>
            
            <?php
                // User does not have permission
                if(!$_SESSION['customeruser']) {
                    echo "<h1>Error</h1>";
                    echo "<p>You must be logged in as a customer to access this page.</p>";
                    echo "<br /><a href='demoPage.php'>Go to Customer Login</a><br /><br />";
                
                // User does have permission    
                } else {
                    // Get user's id & cart contents
                    $userId = $_SESSION['userId'];
                    $cart = mysqli_query($conn, ""
                            . "SELECT * FROM ShoppingCart WHERE customer_id = '".$userId."' ");
                    
                    // User added products to cart
                    if(!empty($_POST['productSelectCB'])) {
                        foreach ($_POST['productSelectCB'] as $check) {
                            
                            // Product is already in cart
                            $checkCartQuery = mysqli_query($conn, ""
                                . "SELECT * FROM ShoppingCart WHERE product_id = '".$check."' ");
                            if (mysqli_num_rows($checkCartQuery) > 0) {
                                echo "Product with ID ", $check, " is already in shopping cart. ";
                                echo "Remove it or edit the quantity.<br />";
                            
                            // Product is not yet in cart    
                            } else {
                            
                                // Add one of each checked product to user's shopping cart
                                $addProductsToCartQuery = mysqli_query($conn, ""
                                    . "INSERT INTO ShoppingCart (customer_id, product_id, quantity)"
                                    . "VALUES ('".$userId."', '".$check."', 1)");

                                // Error
                                if (!$addProductsToCartQuery) {
                                    echo "<h1>Error</h1>";
                                    echo "Failed to add product to cart.";
                                    echo "<code>", mysqli_error($conn), "</code>";
                                
                                // Success    
                                } else {
                                    echo "<p>Product with ID ", $check, " was added shopping cart.</p><br />";
                                }
                            }
                        }
                    
                    // User incremented product quantity   
                    } else if (!empty($_POST['incQ'])) {
                        $pId = $_POST['incQ'];
                        
                        // Increment quantity only if it does not exceed available product stock
                        $checkStockQuery = mysqli_query($conn, ""
                                . "SELECT stock FROM Product WHERE id = '".$pId."' ");
                        $row = mysqli_fetch_array($checkStockQuery);
                        $pStock = $row['stock'];
                        $checkQuantityQuery = mysqli_query($conn, ""
                                . "SELECT quantity FROM ShoppingCart WHERE product_id = '".$pId."' ");
                        $row = mysqli_fetch_array($checkQuantityQuery);
                        $pQuantity = $row['quantity'];
                        
                        if ($pQuantity < $pStock) {
                            $addQuantityQuery = mysqli_query($conn, ""
                                    . "UPDATE ShoppingCart SET quantity = quantity + 1 WHERE product_id = '".$pId."' ");
                            if ($addQuantityQuery) {
                                echo "Incremented quantity of product with ID: ", $pId;
                            } else {
                                echo "<h1>Error</h1>";
                                echo "Failed to increment product quantity.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            }
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Quantity cannot exceed available product stock.</p>";
                        }
                        
                    // User decremented product quantity     
                    } else if (!empty($_POST['decQ'])) {
                        $pId = $_POST['decQ'];
                        
                        // Decrement quantity only if quatity remains greater than 0
                        $checkQuantityQuery = mysqli_query($conn, ""
                            . "SELECT quantity FROM ShoppingCart WHERE product_id = '".$pId."' ");
                        $row = mysqli_fetch_array($checkQuantityQuery);
                        $pQuantity = $row['quantity'];
                        
                        if ($pQuantity > 1) {
                            $decQuantityQuery = mysqli_query($conn, ""
                                    . "UPDATE ShoppingCart SET quantity = quantity - 1 WHERE product_id = '".$pId."' ");
                            if ($decQuantityQuery) {
                                echo "Decremented quantity of product with ID: ", $pId;
                            } else {
                                echo "<h1>Error</h1>";
                                echo "Failed to decrement product quantity.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            }
                        } else {
                            echo "<h1>Error</h1>";
                            echo "<p>Quantity cannot be 0. Please remove the product instead.</p>";
                        }
                        
                    // User removed products from cart     
                    } else {
                        foreach ($_POST['cartSelectCB'] as $check) {
                            
                            // Product is already in cart
                            $checkCartQuery = mysqli_query($conn, ""
                                . "SELECT * FROM ShoppingCart WHERE product_id = '".$check."' ");
                            
                            // Error
                            if (!$checkCartQuery) {
                                echo "<h1>Error</h1>";
                                echo "Failed to retrieve cart product.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            
                            // Success  
                            } else {
                            
                                // Remove checked product from shopping cart
                                $removeProductFromCartQuery = mysqli_query($conn, ""
                                    . "DELETE FROM ShoppingCart WHERE product_id = '".$check."' ");
                                
                                // Error
                                if (!$removeProductFromCartQuery) {
                                    echo "<h1>Error</h1>";
                                    echo "Failed to remove product from cart.";
                                    echo "<code>", mysqli_error($conn), "</code>";
                                
                                // Success    
                                } else {
                                    echo "<p>Product with ID ", $check, " was removed from shopping cart.</p><br />";
                                }
                            }
                        }
                    }
                    
                    // Display cart
                    $checkCartQuery = mysqli_query($conn, ""
                        . "SELECT * FROM ShoppingCart WHERE customer_id = '".$userId."' ");
                    
                    // Error
                    if (!$checkCartQuery) {
                        echo "<h1>Error</h1>";
                        echo "Failed to retrieve cart.";
                        echo "<code>", mysqli_error($conn), "</code>"; 
                    
                    // Success    
                    } else {
                        // User incremented product quantity 
                        // Display table with cart contents, keep track of overall price
                        $finalPrice = 0;
                        echo "<form id='cartForm' action='#' method='POST'>";
                        echo "<table id='cartTable'>";
                        echo ""
                            . "<tr>"            // Header row
                                    . "<td></td>"
                                    . "<td><strong>ID</strong></td>"
                                    . "<td><strong>Name</strong></td>"
                                    . "<td><strong>Price</strong></td>"
                                    . "<td><strong>Stock</strong></td>"
                                    . "<td><strong>Quantity</strong></td>"
                                    . "<td><strong>Description</strong></td>"
                            . "</tr>";
                        while ($row = mysqli_fetch_array($checkCartQuery)) {
                            $productId = $row['product_id'];
                            $quantity = $row['quantity'];
                            $cartProductsQuery = mysqli_query($conn, ""
                                . "SELECT * FROM Product WHERE id = '".$productId."' ");
                            
                            // Error
                            if (!$cartProductsQuery) {
                                echo "<h1>Error</h1>";
                                echo "Failed to retrieve cart product.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            
                            // Success    
                            } else {
                                $row = mysqli_fetch_array($cartProductsQuery);
                                $id = $row['id'];
                                $name = $row['name'];
                                $price = $row['price'];
                                $finalPrice = $finalPrice + $price * $quantity;     // Track overall price
                                $stock = $row['stock'];
                                $description = $row['description'];
                                echo ""
                                . "<tr>"
                                    . "<td id='cartSelectCB'><input type='checkbox' name='cartSelectCB[]' value='".$productId."' /></td>"
                                    . "<td>".$id."</td>"
                                    . "<td>".$name."</td>"
                                    . "<td>".$price."</td>"
                                    . "<td>".$stock."</td>"
                                    . "<td>".$quantity." "
                                        . "     (+1 Quantity: <input name='incQ' type='submit' value='".$id."' />)"
                                        . "     (-1 Quantity: <input name='decQ' type='submit' value='".$id."' />)</td>"   
                                    . "<td>".$description."</td>"
                                . "</tr>";
                            }
                        }
                        echo "</table>";
                        echo "<input type='submit' name='cartSelectSubmit' value='Remove Selected Products from Shopping Cart'/>";
                        echo "</form>";
                        
                        // User can make an order, get current funds
                        $userId = $_SESSION['userId'];
                        $fundsQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Customer WHERE id = '".$userId."' ");

                        // Error
                        if (!$fundsQuery) {
                            echo "<h1>Error</h1>";
                            echo "<p>Failed accessing funds. <br /></p>";
                            echo "<code>", mysqli_error($conn), "</code>";

                        // Success    
                        } else {
                            $row = mysqli_fetch_array($fundsQuery);
                            $funds = $row['funds'];
                            $funds = ltrim($funds, '0');
                            
                            echo "<form id='orderForm' action='transaction.php' method='POST'>";
                            echo "<br /><p>Final price: $", number_format($finalPrice, 2), "</p>";
                            if ($funds == 0) {
                                echo "<br /><p>No funds available.</p><br />";
                            } else {
                                echo "<br /><p>Available funds: $", $funds, "</p><br />";
                            }
                            echo "<input type='submit' name='orderSubmit' value='Make Order'/>";
                            echo "<br /><br /><p>Note: If you have enough funds, clicking this button will finalize this order.</p>";
                            echo "</form>";
                        }
                    }
                }
            ?>
        </div>
    </body>
</html>