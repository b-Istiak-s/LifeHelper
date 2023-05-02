<?php
require_once '../../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
	$restaurant_id = mysqli_real_escape_string($conn,stripcslashes($_POST["id"]));
	$username = mysqli_real_escape_string($conn,stripcslashes($_POST["username"]));
	$desc = mysqli_real_escape_string($conn,stripcslashes($_POST["desc"]));


	
    $temp = explode(".", $_FILES["file"]["name"]);	
    $filename = 'logo'.'.'.end($temp);
    $targetFile = '../../specific_restaurants_data/'.$username."/".$filename;
    $extension = pathinfo($targetFile, PATHINFO_EXTENSION);
    if(!is_dir($username."/")){
                mkdir($username);
        }
    $query = "UPDATE `users` SET `full_name`='$fullName',`phone`='$phone',`img`='$filename',`gender`='$gender',`birth_year`='$birthYear',`hobby`='$hobby',`height`='$height',`char_type`='$char_type' WHERE `username`='$username'";
	
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
