<!DOCTYPE html>
<!--CPSC 471 Project-->
<html>
    <head>
        <meta charset="UTF-8">
        <title>CPSC 471 Project</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        
        <!--Header-->
        <div id="header">
            <h3>CPSC 471 - Group 9 - Final Project</h1>
            <h4>Authors: Mohamed, Nikolai, and Marc-Andre</h2>
        </div>
        
        <!--Navgation-->
        <form id="navi">
            <!--Home-->
            <input formaction="demoPage.php"
                   style="width: 150px; float: left" 
                   type="submit" 
                   value="Home"/>
                
            <!--Admin Login-->
            <input formaction="adminLogin.php"
                   style="width: 150px; float: right" 
                   type="submit" 
                   value="Admin Login"/><br/><br/>
                
            <!--Departments Dropdown-->
            <select style="width: 150px; float: left">
                <option href="demoPage.php">Department 1</option>
                <option href="demoPage.php">Department 2</option>
                <option href="demoPage.php">Department 3</option>
                <input type="submit" value="Go"/>
            </select>
            
            <!--User Login-->
            <input formaction="userLogin.php"
                   style="width: 150px; float: right" 
                   type="submit" 
                   value="User Login"/>
        </form>
        
        <div id="cart" style="text-align: right">
            <img src="https://d30y9cdsu7xlg0.cloudfront.net/png/28468-200.png"
                 alt="Shopping Cart"
                 style="width: 100px; height: 100px">
            <br/>
            <a>Items in Cart: X</a>
        </div>
            
        <?php
        //    $servername = "localhost";        // Server: Localhost
        //    $username = "root";               // User: Root
        //    $password = "secret";             // Insert localhost root PW
        //    $db = "temp";                     // Insert DB name
            
            // Connect to specified DB
        //    $conn = new mysqli($servername, $username, $password, $db);
            
            // Check if connection was established successfully
        //    if($conn->connect_error){
        //        die("Connection failed".$conn->connect_error);
        //    }
            
            //sql query
        //    $sql = "INSERT INTO names (names) VALUES ('John')";
        //    echo "<br><br>Inserting  into db: ";
        //    if($conn->query($sql)==TRUE){       //try executing the query 
        //        echo "Query executed<br>";
        //    } else {
        //        echo "Query did not execute<br>";
        //    }
            
            //sql query
        //    $sql = "SELECT Id, names FROM names";
        //    echo "<br>Printing IDs and names:<br>";
        //    $result = $conn->query($sql);       //execute the query
            
        //    if($result->num_rows >0){           //check if query results in more than 0 rows
        //        while($row = $result->fetch_assoc()){   //loop until all rows in result are fetched
        //            echo "Entry " .$row["Id"].": ".$row["names"]."<br>";     // print ID
        //        }
        //   }
            
            // Close DB connection
        //    $conn-> close();
        ?>
        
    </body>
</html>
