<!DOCTYPE html>
<!--
CPSC 471 Group 9 Project
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title>CPSC 471 Project - Group 9</title>
    </head>
    <body>
        <h1>Demo for 471</h1>
        <?php
            // put your code here
            $servername = "localhost";          //should be same for you
            $username = "root";                 //same here
            $password = "secret";             //your localhost root password
            $db = "temp";                     //your database name
            
            $conn = new mysqli($servername, $username, $password, $db);
            
            if($conn->connect_error){
                die("Connection failed".$conn->connect_error);
            }else{
                echo "Connected<br>";
            }
            
            //sql query
            //$sql = "INSERT INTO names (names) VALUES ('John')";
            //echo "<br><br>Inserting  into db: ";
            //if($conn->query($sql)==TRUE){       //try executing the query 
            //    echo "Query executed<br>";
            //}
            //else{
            //    echo "Query did not execute<br>";
            //}
            
            //sql query
            $sql = "SELECT Id, names FROM names";
            echo "<br>Printing IDs and names:<br>";
            $result = $conn->query($sql);       //execute the query
            
            if($result->num_rows >0){           //check if query results in more than 0 rows
                while($row = $result->fetch_assoc()){   //loop until all rows in result are fetched
                    echo "Entry " .$row["Id"].": ".$row["names"]."<br>";     // print ID
                }
            }
            
            $conn-> close();            //close the connection to database
        ?>
    </body>
</html>
