<?php
    session_start();                  // Start or resume user session

    $servername = "localhost";        // Server: Localhost
    $username = "root";               // User: Root
    $password = "secret";             // Insert localhost root PW
    $db = "CPSC471_Group9_DB";        // Insert DB name
            
    // Connect to specified DB
    $conn = mysqli_connect($servername, $username, $password, $db);
            
    // Check if connection was established successfully
    if(mysqli_connect_errno()){
        echo "Error connecting to DB.";
    }
?>