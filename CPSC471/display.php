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
                
                <!--Admin Login-->
                <a id="adminHomeButton" href="adminLogin.php">Admin Home</a>
            </div>
                 
            <?php
                // User does not have permission
                if(!$_SESSION['customeruser']) {
                    echo "<h1>Error</h1>";
                    echo "<p>You must be logged in as a customer to access this page.</p>";
                    echo "<br /><a href='demoPage.php'>Go to Customer Login</a><br /><br />";
                 } else
                  
                // Active department was supplied by POST
                if (isset($_POST['displaydropdown'])) {
                    $_SESSION['activeDepartment'] = $_POST['displaydropdown'];
                    $depName = $_SESSION['activeDepartment'];
                    echo "<h3>Showing products for department:";
                    echo "<h2>", $depName, "</h2>";

                    // Get department id
                    $depId = mysqli_query($conn, ""
                        . "SELECT id FROM Department WHERE name = '$depName' ");
                    
                    // Department ID retrieved
                    if ($depId) {
                        $row = mysqli_fetch_array($depId);
                        echo "<br />Department ID: ", $row['id'];
                        
                        // Get products belonging to this department
                        $productQuery = mysqli_query($conn, ""
                            . "SELECT * FROM Product WHERE department_id = '".$row['id']."' ");
                    
                        // Products retrieved
                        if ($productQuery) {
                            
                            // Display table with products
                            echo "<form id='displayToCartForm' action='/cpsc471.com/displayCart.php' method='POST'>";
                            echo "<table id='productDisplayTable'>";
                            echo ""
                            . "<tr>"
                                    . "<td></td>"
                                    . "<td><strong>Name</strong></td>"
                                    . "<td><strong>Price</strong></td>"
                                    . "<td><strong>Stock</strong></td>"
                                    . "<td><strong>Description</strong></td>"
                            . "</tr>";
                            while ($row = mysqli_fetch_array($productQuery)) {
                                $productId = $row['id'];
                                $name = $row['name'];
                                $price = $row['price'];
                                $stock = $row['stock'];
                                $description = $row['description'];
                                echo ""
                                . "<tr>"
                                     . "<td id='productSelectCB'><input type='checkbox' name='productSelectCB[]' value='".$productId."' /></td>"
                                     . "<td>".$name."</td>"
                                     . "<td>".$price."</td>"
                                     . "<td>".$stock."</td>"
                                     . "<td>".$description."</td>"
                                . "</tr>";
                            }
                            echo "</table>";
                            echo "<input type='submit' name='productSelectSubmit' value='Add to Shopping Cart'/>";
                            echo "</form>";
                            
                        // Error retrieving products    
                        } else {
                            echo "Error getting products for ", $depName;
                            echo "<code>", mysqli_error($conn), "</code>";
                        }
                    // Error retrieving department ID
                    } else {
                        echo "Error getting department ID for ", $depName;
                        echo "<code>", mysqli_error($conn), "</code>";
                    }
                    
                    
                    
                // No active department was supplied by POST    
                } else {
                    echo "<h1>Error</h1>";
                    echo "No department was chosen.";
                }
                
                
            ?>
        </div>
    </body>
</html>