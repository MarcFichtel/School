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
                //retrieves the transaction id from the customer id
                    $retrieveId = mysqli_query($conn, ""
                           . "SELECT Transaction.id, Transaction.date FROM Transaction, Customer "
                           . "WHERE  Customer.username = '".$_SESSION['Username']."' "
                           . "  AND Customer.id = Transaction.customer_id ");
                ?>
                
                <?php
                //checks if the form from the select buttons are pressed
                if (!empty($_POST['t_id'])){
                    //fetches the product ids from the transaction id
                    $int_id = (int)($_POST['t_id']);
                    $t_products = mysqli_query($conn, ""
                            . "SELECT Transactionproducts.product_id FROM Transactionproducts "
                            . "WHERE Transactionproducts.transaction_id = '$int_id' ");
                    
                    //takes the first row of that table since we're setting the admin id as the first admin id in the list of products
                    $p_row = mysqli_fetch_assoc($t_products);
                    
                    //finds the product with the same product_id as the first product in the transaction and finds the admin_id from it
                    $admin_id = mysqli_query($conn, ""
                            . "SELECT Product.admin_id, Product.price FROM Product "
                            . "WHERE Product.id = '".$p_row["product_id"]."'");
                    $a_row = mysqli_fetch_assoc($admin_id);
                    
                    //updates the transaction admin_id to the one obtained from first product in transaction
                    //not sure if we can update the admin_id directly because it's a foreign key
                    $insert_id = mysqli_query($conn, ""
                            . "UPDATE Transaction"
                            . "SET Transaction.admin_id = '".$a_row["admin_id"]."' "
                            . "WHERE Transaction.id = '$int_id'");
                    
                    //updates the stock of the product
                    //don't know if I can do the + 1 to Product.stock in the sqli query
                    $update_stock = mysqli_query($conn, ""
                            . "UPDATE Product"
                            . "SET Product.stock = Product.stock+1"
                            . "WHERE Product.id = '".$p_row["product_id"]."'");
                    while($stock = mysqli_fetch_assoc($t_products)){
                        $update_stock = mysqli_query($conn, ""
                            . "UPDATE Product"
                            . "SET Product.stock = Product.stock+1"
                            . "WHERE Product.id = '".$p_row["product_id"]."'");
                    }
                    
                    //add the funds back to the customers wallet
                    //gets a table full of prices of products that were in the transactions
                    $product_list = mysqli_query($conn, ""
                            . "SELECT Product.price FROM Product, Transactionproducts"
                            . "WHERE Transactionproducts.transaction_id = '$int_id' "
                            . "  AND Transactionproducts.product_id = Product.id");
                    //adds the prices in the list with the matching list id and product id
                    while($prices = mysqli_fetch_assoc($product_list)){
                        $update_product = mysqli_query($conn, ""
                                . "UPDATE Customer"
                                . "SET Customer.funds = Customer.funds+'".$prices['price']."'"
                                . "WHERE Customer.username = '".$_SESSION['Username']."'" );
                        
                    }
                    
                }
                //creates a table that shows each transaction
                if (mysqli_num_rows($retrieveId) > 0){
                    echo "<table border='2' class='stats' cellspacing='2'>
                        <tr>
                        <td class='hed' colspan='8'>Transactions</td>
                        </tr>
                        <tr>
                        <th>Id</th>
                        <th>Date</th>
                        <th>Refund</th>
                        </tr>";

                    while ($row = mysqli_fetch_assoc($retrieveId)){
                        echo "<tr>";
                        echo "<td>".$row["id"];
                        echo "<td>".$row["date"];
                        echo "<td>"; 
            ?>
                        <form method="POST" action="transactions.php" name = "form">
                        <fieldset>
                            <input value="<?php echo $row["id"];?>" type="hidden" name="t_id">
                            <input type="submit"  value="Select">
                        </fieldset>
                        </form>
            <?php
                        echo "</td></tr>";
                    }
                }
            ?>  
                
            </div>
	</body>
</html>
