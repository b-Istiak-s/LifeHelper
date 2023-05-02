<?php
require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
	$username = mysqli_real_escape_string($conn,stripcslashes($_POST["username"]));
	$restaurant_name = mysqli_real_escape_string($conn,stripcslashes($_POST["restaurant_name"]));
	$address = mysqli_real_escape_string($conn,stripcslashes($_POST["address"]));
	$opening_hours = mysqli_real_escape_string($conn,stripcslashes($_POST["opening_hours"]));
	$closing_hours = mysqli_real_escape_string($conn,stripcslashes($_POST["closing_hours"]));
	$map_link_address = mysqli_real_escape_string($conn,stripcslashes($_POST["map_link_address"]));
	$city = mysqli_real_escape_string($conn,stripcslashes($_POST["city"]));
	$country = mysqli_real_escape_string($conn,stripcslashes($_POST["country"]));
	$phone = mysqli_real_escape_string($conn,stripcslashes($_POST["phone"]));

	$restaurant_id = (int) mysqli_num_rows(mysqli_query($conn,"SELECT * FROM `restaurants`"))+1;
    $make_dir = mkdir('specific_restaurants_data/'.$restaurant_id."_".$username);
	$temp = explode(".", $_FILES["file"]["name"]);	
	$filename = 'logo'.'.'.end($temp);
	$targetFile = 'specific_restaurants_data/'.$restaurant_id."_".$username."/".$filename;
	$extension = pathinfo($targetFile, PATHINFO_EXTENSION);

	$check_if_restaurant_exists = mysqli_num_rows(mysqli_query($conn,"SELECT * FROM `restaurants` WHERE `creator_username`='$username'"));

	$query = "INSERT INTO `restaurants`(`restaurant_name`, `creator_username`, `address`, `city`, `country`, `opening_hours`, `closing_hours`, `map_link_address`, `phone_number`, `logo`, `current_status`) VALUES ('$restaurant_name','$username','$address','$city','$country','$opening_hours','$closing_hours','$map_link_address','$phone','$targetFile','under_review')";
	if ($check_if_restaurant_exists!=0) {
      $success = "already_exists";
      $message = "Sorry! You can't create multiple restaurants from the same account.";
	}else{
		if(mysqli_query($conn, $query)){
			if (move_uploaded_file($_FILES["file"]["tmp_name"], $targetFile))
		   {
		     $success = "success";
		     $message = "Your submitted restaurant is under review.";
		   }
			else 
		   {
		      $success = "failure";
		      $message = "Error while uploading logo. But it's under review.";
		   }
		}else{
		      $success = "failed";
		      $message = "Unable to upload your data.";
		}
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
