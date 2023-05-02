<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $sender = mysqli_real_escape_string($conn,stripcslashes($_POST['sender']));
    $timestamp = mysqli_real_escape_string($conn,stripcslashes($_POST['timestamp']));
    
    $query = mysqli_query($conn,"SELECT * FROM `chat` WHERE `timestamp` > '$timestamp' AND ((`from`='$username' AND `to`='$sender') OR (`from`='$sender' AND `to`='$username')) ORDER BY `timestamp` DESC");
    
	$array = array();
    while ($row = mysqli_fetch_assoc($query)) {
        array_push($array, array(
            'id' => $row['id'], 
            'from' => $row['from'], 
            'to' => $row['to'],
            'message' => $row['message'],
            'reply_to' => $row['reply_to'],
            'timestamp' => $row['timestamp'],
            'notified' => $row['notified']));
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