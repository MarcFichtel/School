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
                // Retrieves the transaction id from the customer id
                $username = $_SESSION['Username'];
                $sql = "SELECT Transaction.id, Transaction.date "
                        . "FROM Transaction, Customer "
                        . "WHERE Customer.username = '".$username."' "
                        . "AND Customer.id = Transaction.customer_id";
                $retrieveId = mysqli_query($conn, $sql);
                if (!$retrieveId) {
                    echo "<h1>Error</h1>";
                    echo "Failed to retrieve transaction ids.";
                    echo "<code>", mysqli_error($conn), "</code>";
                } else {
                    echo "<h1>Your Transactions</h1>";
                }

                // Checks if the form from the select buttons are pressed
                if (!empty($_POST['t_id'])) {
                    
                    // Fetches the product ids from the transaction id
                    $t_id = $_POST['t_id'];
                    $t_products = mysqli_query($conn, ""
                            . "SELECT TransactionProducts.product_id FROM TransactionProducts "
                            . "WHERE TransactionProducts.transaction_id = '".$t_id."' ");
                    if (!$t_products) {
                        echo "<h1>Error</h1>";
                        echo "Failed to retrieve product ids.";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    // Takes the first row of that table since we're setting 
                    // the admin id as the first admin id in the list of products
                    $p_row = mysqli_fetch_assoc($t_products);
                    
                    // Finds the product with the same product_id as the first 
                    // product in the transaction and finds the admin_id from it
                    $pId = $p_row['product_id'];
                    $sql = "SELECT admin_id, price FROM Product WHERE id = '".$pId."'";
                    $admin_id = mysqli_query($conn, $sql);
                    if (!$admin_id) {
                        echo "<h1>Error</h1>";
                        echo "Failed to retrieve admin ids.";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    $a_row = mysqli_fetch_assoc($admin_id);

                    // Updates the transaction admin_id to the one obtained from 
                    // first product in transaction
                    // TODO Not sure if we can update the admin_id directly 
                    // because it's a foreign key
                    $aId = $a_row['admin_id'];
                    $sql = "UPDATE Transaction SET admin_id = '".$aId."' WHERE id = '".$t_id."' ";
                    $insert_id = mysqli_query($conn, $sql);
                    if (!$insert_id) {
                        echo "<h1>Error</h1>";
                        echo "Failed to update transaction's admin ids.";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    // Updates the stock of the product
                    // TODO Don't know if I can do the + 1 to Product.stock 
                    // in the sqli query
                    while($stock = mysqli_fetch_assoc($t_products)){
                        $pId = $stock['product_id'];
                        $sql = "UPDATE Product SET stock = stock+1 WHERE id = '".$pId."' ";
                        $update_stock = mysqli_query($conn, $sql);
                        if (!$update_stock) {
                            echo "<h1>Error</h1>";
                            echo "Failed to update product stock.";
                            echo "<code>", mysqli_error($conn), "</code>";
                        }
                    }
                    
                    // Add the funds back to the customers wallet
                    // Gets a table full of prices of products that were in the transactions
                    $sql = "SELECT Product.price FROM Product, TransactionProducts "
                            . "WHERE TransactionProducts.transaction_id = '".$t_id."' "
                            . "AND TransactionProducts.product_id = Product.id";
                    $product_list = mysqli_query($conn, $sql);
                    if (!$update_stock) {
                        echo "<h1>Error</h1>";
                        echo "Failed to retrieve product prices.";
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                        
                    // Adds the prices in the list with the matching list id and product id
                    while ($prices = mysqli_fetch_assoc($product_list)) {
                        $price = $prices['price'];
                        $sql = "UPDATE Customer "
                                . "SET funds = funds+'".$price."' "
                                . "WHERE username = '".$username."'";
                        $update_product = mysqli_query($conn, $sql);
                        if (!$update_stock) {
                            echo "<h1>Error</h1>";
                            echo "Failed to add up prices.";
                            echo "<code>", mysqli_error($conn), "</code>";
                        }
                    }
                }
                
                // Creates a table that shows each transaction
                if (mysqli_num_rows($retrieveId) == 0){
                    echo "There are no transactions at the moment. "
                        . "Once you make a purchase, the transaction will appear here.";
                }
                echo "<table id='transactiontable'>";
                    echo "<tr>";
                        echo "<th>ID</th>";
                        echo "<th>Date</th>";
                        echo "<th>Refund</th>";
                    echo "</tr>";

                while ($row = mysqli_fetch_assoc($retrieveId)){
                    $id = $row['id'];
                    $date = $row['date'];
                    echo "<tr>";
                        echo "<td>", $id, "</td>";
                        echo "<td>", $date, "</td>";
                        echo "<td>''</td>"; 

                    echo "<form method='POST' action='transactions.php' name='transactionsform'>";
                        echo "<fieldset>";
                            echo "<input value='".$id."' type='hidden' name='t_id'>";
                            echo "<input type='submit'  value='Select'>";
                        echo "</fieldset>";
                    echo "</form>";
                }
            ?>  
                
        </div>
    </body>
</html>
