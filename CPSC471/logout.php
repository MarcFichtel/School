<?php 
    include "base.php";     // Include base
    $_SESSION = array();    // Reset $_SESSION
    session_destroy();      // Destroy session
?>

<meta http-equiv="refresh" content="0; demoPage.php"> 