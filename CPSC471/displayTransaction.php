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
                
                // User does have permission and is refunding a transaction   
                } else if (!empty($_POST['refund'])) {
                    
                    $tId = $_SESSION['transactionId'];
                    $userId = $_SESSION['userId'];
                    
                    // Check transaction status
                    $checkTransactionStatus = mysqli_query($conn, ""
                            . "SELECT state FROM Transaction WHERE id = '".$tId."' ");
                    $row = mysqli_fetch_array($checkTransactionStatus);
                    $state = $row['state'];
                    
                    if ($state == 'Refunded') {
                        echo "<h1>Error</h1>";
                        echo "<p>This transaction was already refunded.</p>";
                    } else {
                    
                        // Add price back to funds
                        $refund = $_SESSION['transactionPrice'];
                        $returnFundsQuery = mysqli_query($conn, ""
                                . "UPDATE Customer SET funds = funds + '".$refund."' WHERE id = '".$userId."' ");
                        if (!$returnFundsQuery) {
                            echo "<h1>Error</h1>";
                            echo "Failed to return funds.";
                            echo "<code>", mysqli_error($conn), "</code>";
                        }

                        // Delete transaction entry
                        $editTransactionStatusQuery = mysqli_query($conn, ""
                                . "UPDATE Transaction SET state = 'Refunded' WHERE id = '".$tId."' ");
                        if (!$editTransactionStatusQuery) {
                            echo "<h1>Error</h1>";
                            echo "Failed to update transaction status.";
                            echo "<code>", mysqli_error($conn), "</code>";
                        }

                        echo "<p>Transaction refunded. The transaction price was added back to your available funds.</p>";
                    }
                    echo "<br /><p><a href='transactions.php'>Back to Transactions</a></p><br />";
                    
                // User wants to view a transaction       
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
                            $tPrice = $tPrice + $price * $quantity;
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
                    echo "<br /><p><a href='transactions.php'>Back to Transactions</a></p><br />";
                    
                // User wants to get a refund    
                } else if (!empty($_POST['refundTransactionButton'])) {
                    
                    // Get transaction id
                    $tId = $_SESSION['transactionId'];
                    echo "<h3>Refunding Transaction #", $tId;
                    echo "<p>Do you want to refund the following transaction?</p>";
                
                    // Get user & transaction id
                    $userId = $_SESSION['userId'];
                    $tPrice = 0;
                    $tId = $_POST['refundTransactionButton'];
                    $_SESSION['transactionId'] = $tId;
                    $getTransactionProducts = mysqli_query($conn, ""
                            . "SELECT * FROM TransactionProducts WHERE transaction_id = '".$tId."' ");
                    
                    echo "<form method='POST' action='displayTransaction.php' name='transactions3form'>";
                        echo "<table id='transaction3table'>";
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
                            $tPrice = $tPrice + $price * $quantity;
                            echo "<tr>";
                                echo "<td>", $id, "</td>";
                                echo "<td>", $name, "</td>";
                                echo "<td>", $price, "</td>";
                                echo "<td>", $quantity, "</td>";
                            echo "</tr>";
                        }
                        echo "</table>"; 
                        echo "<input type='submit' name='refund' value='Refund'>";
                    echo "</form>";
                    
                    $_SESSION['transactionPrice'] = $tPrice;
                    echo "<h4>Overall Transaction Price: ", $tPrice, "</h4>";
                    echo "<br /><p><a href='transactions.php'>Back to Transactions</a></p><br />";
                    
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