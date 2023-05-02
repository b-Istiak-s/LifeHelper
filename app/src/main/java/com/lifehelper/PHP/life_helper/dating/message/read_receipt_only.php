<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $id = mysqli_real_escape_string($conn,stripcslashes($_POST['id']));
    
    $query = mysqli_query($conn,"SELECT * FROM `chat` WHERE `id`='$id'");
    
    $row = mysqli_fetch_assoc($query);
	$response["read"]=$row['notified'];
	echo json_encode($response);


} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
mysqli_close($conn);

?>