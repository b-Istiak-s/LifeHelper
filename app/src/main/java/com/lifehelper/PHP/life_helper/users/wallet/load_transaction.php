<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    
    $query = mysqli_query($conn,"SELECT * FROM `wallet` WHERE `username` = '$username' ORDER BY `datetime` DESC");
    
	$array = array();
    while ($row = mysqli_fetch_assoc($query)) {
        array_push($array, array(
            'id' => $row['id'], 
            'username' => $row['username'], 
            'amount' => $row['amount'],
            'timestamp' => $row['datetime'],
            'timezone' => $row['timezone'],
            'type' => $row['type']));
	}
	echo json_encode($array);


} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
mysqli_close($conn);

?>