<?php
require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
	$username = mysqli_real_escape_string($conn,stripcslashes($_POST["username"]));
	$fullName = mysqli_real_escape_string($conn,stripcslashes($_POST["full_name"]));
	$phone = mysqli_real_escape_string($conn,stripcslashes($_POST["phone"]));
	$gender = mysqli_real_escape_string($conn,stripcslashes($_POST["gender"]));
	$birthYear = mysqli_real_escape_string($conn,stripcslashes($_POST["birth_year"]));
	$hobby = mysqli_real_escape_string($conn,stripcslashes($_POST["hobby"]));
	$height = mysqli_real_escape_string($conn,stripcslashes($_POST["height"]));
	$char_type = mysqli_real_escape_string($conn,stripcslashes($_POST["char_type"]));

	if(!empty($_FILES)){
		$temp = explode(".", $_FILES["file"]["name"]);	
		$filename = 'profile'.'.'.end($temp);
		$targetFile = $username."/".$filename;
		$extension = pathinfo($targetFile, PATHINFO_EXTENSION);
		if(!is_dir($username."/")){
         	     mkdir($username);
        	}
		$query = "UPDATE `users` SET `full_name`='$fullName',`phone`='$phone',`img`='$filename',`gender`='$gender',`birth_year`='$birthYear',`hobby`='$hobby',`height`='$height',`char_type`='$char_type' WHERE `username`='$username'";
	}else{
		$query = "UPDATE `users` SET `full_name`='$fullName',`phone`='$phone',`gender`='$gender',`birth_year`='$birthYear',`hobby`='$hobby',`height`='$height',`char_type`='$char_type' WHERE `username`='$username'";
	}
	if(mysqli_query($conn, $query)){
		if(!empty($_FILES)){
			if (move_uploaded_file($_FILES["file"]["tmp_name"], $targetFile))
		   {
		     $success = "success";
		     $message = "Successfully Uploaded";
		   }
			else 
		   {
		      $success = "failure";
		      $message = "Error while uploading";
		   }
		}else{
		     $success = "success";
		     $message = "Successfully Uploaded";	
		}
	}else{
	      $success = "failed";
	      $message = "Unable to update your data.";
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
