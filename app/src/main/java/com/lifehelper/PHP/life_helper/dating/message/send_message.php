<?php
require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $messageTo = mysqli_real_escape_string($conn, stripcslashes($_POST['to']));
    $message = mysqli_real_escape_string($conn, stripcslashes($_POST['message']));

	date_default_timezone_set('Asia/Dhaka');
    $currentDateTime = new DateTime();
    $format_datetime = $currentDateTime -> format('Y-m-d H:i:s');

    $query = "INSERT INTO `chat`(`from`, `to`, `message`, `reply_to`,`timestamp`,`notified`) VALUES ('$username','$messageTo','$message','','$format_datetime','sent');";
    $query .= "UPDATE `message_between` SET `last_message`='$message',`last_message_from`='$username',`last_message_timestamp`='$format_datetime',`read`='sent' WHERE (`user_1`='$username' AND `user_2`='$messageTo') OR (`user_2`='$username' AND `user_1`='$messageTo')";

	if ($conn->multi_query($query) === TRUE) {
	    $response["value"] = "succeed";
	    $response["message"] = "Sent message successfully";
	} else {
	    $response["value"] = "failure";
	    $response["message"] = "Unable to send message, please try again.";
	}

	echo json_encode($response);
   

} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
mysqli_close($conn);
?>
