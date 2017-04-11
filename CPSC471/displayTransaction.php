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
                
                // User does have permission and wants to view a transaction   
                } else if (!empty($_POST['viewTransactionButton'])) {
                    
                    // Get user & transaction id
                    $userId = $_SESSION['userId'];
                    $tPrice = 0;
                    $tId = $_POST['viewTransactionButton'];
                    echo "<h3>Viewing Transaction #", $tId;
                    $getTransactionProducts = mysqli_query($conn, ""
                            . "SELECT * FROM TransactionProducts WHERE transaction_id = '".$tId."' ");
                    
                    echo "<form method='POST' action='displayTransaction.php' name='transactions2form'>";
                        echo "<table id='transaction2table'>";
                        echo "<tr>";
                            echo "<th>Product ID</th>";
                            echo "<th>Product Name</th>";
                            echo "<th>Price</th>";
                            echo "<th>Quantity</th>";
                        echo "</tr>";

                        while ($row = mysqli_fetch_assoc($getTransactionProducts)){
                            $id = $row['product_id'];
                            $quantity = $row['quantity'];
                            $getProductDetails = mysqli_query($conn, ""
                            . "SELECT * FROM Product WHERE id = '".$id."' ");
                            $row2 = mysqli_fetch_array($getProductDetails);
                            $name = $row2['name'];
                            $price = $row2['price'];
                            $tPrice = $tPrice + $price;
                            echo "<tr>";
                                echo "<td>", $id, "</td>";
                                echo "<td>", $name, "</td>";
                                echo "<td>", $price, "</td>";
                                echo "<td>", $quantity, "</td>";
                            echo "</tr>";
                        }
                        echo "</table>";    
                    echo "</form>";
                    
                    echo "<h4>Overall Transaction Price: ", $tPrice, "</h4>";
                    
                // User wants to get a refund    
                } else if (!empty($_POST['refundTransactionButton'])) {
                    
                    // Get user & transaction id
                    $userId = $_SESSION['userId'];
                    $tId = $_POST['refundTransactionButton'];
                    echo "<h3>Refunding Transaction #", $tId;
                
                // Error: No transaction given    
                } else {
                    echo "<h1>Error</h1>";
                    echo "<p>No transaction chosen.</p>";
                    echo "<br /><p><a href='transactions.php'>Go to Transactions</a></p><br />";
                }
            
            ?>
        </div>
    </body>
</html>