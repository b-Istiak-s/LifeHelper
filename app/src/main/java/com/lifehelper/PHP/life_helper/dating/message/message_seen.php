<?php
require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $messageTo = mysqli_real_escape_string($conn, stripcslashes($_POST['to']));

	date_default_timezone_set('Asia/Dhaka');
    $currentDateTime = new DateTime();
    $format_datetime = $currentDateTime -> format('Y-m-d H:i:s');

    $query = "UPDATE `chat` SET `notified`='seen' WHERE (`to`='$username' AND `from`='$messageTo');";
    $query .= "UPDATE `message_between` SET `read` = 'seen' WHERE (`message_between`.`user_1` = '$username' AND `message_between`.`user_2` = '$messageTo') OR(`message_between`.`user_1` = '$messageTo' AND `message_between`.`user_2` = '$username') AND `last_message_from`='$messageTo'";

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