<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    
    $query = mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'");
    
    $row = mysqli_fetch_assoc($query);
	$response["amount"]=$row['money'];
	echo json_encode($response);


} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
mysqli_close($conn);

?>