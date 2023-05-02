<?php
require_once '../../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
	$restaurant_id = mysqli_real_escape_string($conn,stripcslashes($_POST["id"]));
	$username = mysqli_real_escape_string($conn,stripcslashes($_POST["username"]));
	$desc = mysqli_real_escape_string($conn,stripcslashes($_POST["desc"]));

	$check_if_restaurant_exists = mysqli_num_rows(mysqli_query($conn,"SELECT * FROM `restaurants` WHERE `creator_username`='$username'"));

	$query = "INSERT INTO `restaurant_feed`(`restaurant_id`, `username`, `description`) VALUES 
						('$restaurant_id','$username','$desc')";
	if (mysqli_query($conn,$query)) {
      $success = "already_exists";
      $message = "Sorry! You can't create multiple restaurants from the same account.";
	}else{

	}
	$response["value"] = $success;
	$response["message"] = $message;
	echo json_encode($response);
} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
 mysqli_close($conn);
?>
