<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $sender = mysqli_real_escape_string($conn,stripcslashes($_POST['sender']));
	$limit = mysqli_real_escape_string($conn,stripcslashes($_POST['limit']));
	$off_set = mysqli_real_escape_string($conn,stripcslashes($_POST['off_set']));

    $query = mysqli_query($conn,"SELECT * FROM `chat` WHERE (`from`='$username' AND `to`='$sender') OR (`from`='$sender' AND `to`='$username') ORDER BY `timestamp` DESC LIMIT $limit OFFSET $off_set");
    
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