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
                    // Get user's id
                    $userId = $_SESSION['userId'];
                    
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
                            
                                // Check product stock
                                $checkProductStock = mysqli_query($conn, ""
                                        . "SELECT stock, name FROM Product WHERE id = '".$check."' ");
                                $row = mysqli_fetch_array($checkProductStock);
                                $stock = $row['stock'];
                                $name = $row['name'];
                                
                                if ($stock == 0) {
                                    echo "<p>The product ", $name, " is out of stock and could not be added to the Shopping Cart.</p>";
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
                        }
                    
//////////////////////////////////////////////////////////////////////////////////////////////////////////                        
                    // User wants to make an order 
                    } else if (!empty($_POST['orderSubmit'])) {    
                        
                        // Check if user funds suffice
                        $userId = $_SESSION['userId'];
                        $fundsQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Customer WHERE id = '".$userId."' ");
                        $row = mysqli_fetch_array($fundsQuery);
                        $funds = $row['funds'];
                        $price = $_SESSION['cartPrice'];
                        
                        // Not enough funds
                        if ($price > $funds) {
                            echo "<h1>Error</h1>";
                            echo "<p>Cart price exceeds available funds. Please add sufficient funds.</p><br />";
                            echo "<br /><p><a href='userFunds.php'>Add funds</a></p><br />";
                        
                        // Enough funds    
                        } else {
                            
                            // Deduct funds
                            $deductFundsQuery = mysqli_query($conn, ""
                                    . "UPDATE Customer SET funds = funds - '".$price."' WHERE id = '".$userId."' ");
                            
                            if (!$deductFundsQuery) {
                                echo "<h1>Error</h1>";
                                echo "Failed to deduct funds.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            }
                            
                            // Create transaction
                            $date = date('Y-m-d', time());
                            $createTransactionQuery = mysqli_query($conn, ""
                                    . "INSERT INTO Transaction (state, date, customer_id, admin_id) "
                                    . "VALUES ('Complete', '".$date."', '".$userId."', 1)");
                            if (!$createTransactionQuery) {
                                echo "<h1>Error</h1>";
                                echo "Failed to create a transaction.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            }
                            
                            // Create an entry in TransactionProducts for each product
                            $getTransactionIDQuery = mysqli_query($conn, ""
                                    . "SELECT id FROM Transaction "
                                    . "WHERE customer_id = '".$userId."' AND id = LAST_INSERT_ID()");
                            if (!$getTransactionIDQuery) {
                                echo "<h1>Error</h1>";
                                echo "Failed to transaction id.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            } else {
                                $row = mysqli_fetch_array($getTransactionIDQuery);
                                $transactionId = $row['id'];        // Get ID of transaction that was just created
                            }
                            
                            // Get cart contents
                            $cart = mysqli_query($conn, ""
                            . "SELECT * FROM ShoppingCart WHERE customer_id = '".$userId."' ");
                            if (!$cart) {
                                echo "<h1>Error</h1>";
                                echo "Failed to get cart contents.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            }
                            
                            // Create transactions products & decrease product stock
                            while ($row = mysqli_fetch_array($cart)) {
                                $productId = $row['product_id'];
                                $quantity = $row['quantity'];
                                $createTransactionProductsQuery = mysqli_query($conn, ""
                                    . "INSERT INTO TransactionProducts (transaction_id, product_id, quantity) "
                                    . "VALUES ('".$transactionId."', '".$productId."', '".$quantity."')");
                                if (!$createTransactionProductsQuery) {
                                    echo "<h1>Error</h1>";
                                    echo "Failed to insert transaction products.";
                                    echo "<code>", mysqli_error($conn), "</code>";
                                }
                                $decreaseProductStockQuery = mysqli_query($conn, ""
                                        . "UPDATE Product SET stock = stock - '".$quantity."' WHERE id = '".$productId."' ");
                                if (!$decreaseProductStockQuery) {
                                    echo "<h1>Error</h1>";
                                    echo "Failed to update product stock.";
                                    echo "<code>", mysqli_error($conn), "</code>";
                                }
                            }    
                            
                            // Clear shopping cart
                            $clearCartQuery = mysqli_query($conn, ""
                                    . "DELETE FROM ShoppingCart WHERE customer_id = '".$userId."' ");
                            if (!$clearCartQuery) {
                                echo "<h1>Error</h1>";
                                echo "Failed to clear shopping cart.";
                                echo "<code>", mysqli_error($conn), "</code>";
                            }
                            
                            echo "<h1>Success</h1>";
                            echo "<p>Created transaction with ID: ", $transactionId, "<p>";
                            echo "<br /><p><a href='transactions.php'>View Transactions</a></p><br />";
                        }
                        
////////////////////////////////////////////////////////////////////////////////////////////////////////////                        
                        
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
                        
                        // Is cart empty?
                        if (mysqli_num_rows($checkCartQuery) == 0) {
                            echo "<br /><p>Your Shopping Cart is currently empty. Please add products.</p><br />";
                        }
                        
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
                            $_SESSION['cartPrice'] = $finalPrice;
                            
                            echo "<form id='orderForm' action='#' method='POST'>";
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