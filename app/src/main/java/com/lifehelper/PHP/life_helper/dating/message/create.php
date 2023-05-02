<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $user_row = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'"));
    $partner_username = $user_row['partner'];

    date_default_timezone_set('Asia/Dhaka');
    $currentDateTime = new DateTime();
    $format_datetime = $currentDateTime -> format('Y-m-d H:i:s');

    $check_existence = mysqli_num_rows(mysqli_query($conn,"SELECT * FROM `message_between` WHERE (`user_1`='$username' AND `user_2`='$partner_username') OR (`user_2`='$username' AND `user_1`='$partner_username')"));
    $query = "INSERT INTO `message_between`(`user_1`, `user_2`, `last_message`, `last_message_from`,`last_message_timestamp`,`read`) VALUES ('$username','$partner_username','Hello!','$username','$format_datetime','sent');";
    $query .= "INSERT INTO `chat`(`from`, `to`, `message`, `reply_to`,`timestamp`,`notified`) VALUES ('$username','$partner_username','Hello!','','$format_datetime','sent')";

    if ($check_existence==0) {
		if ($conn->multi_query($query) === TRUE) {
		    $response["value"] = "succeed";
		    $response["message"] = "Sent message successfully";
		} else {
		    $response["value"] = "something_went_wrong";
		    $response["message"] = "Unable to send message, please try again.";
		}
    }else{
    	$response["value"] = "just_take_to_message";
		$response["message"] = "";
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