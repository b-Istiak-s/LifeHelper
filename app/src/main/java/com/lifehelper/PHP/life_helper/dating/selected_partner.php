<?php
	
require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
	date_default_timezone_set('Asia/Dhaka');
    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $row = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'"));
    $partner = $row['partner'];
    $row_for_datetime = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `past_date` WHERE `partner_found`='$username' AND `partner_finder`='$partner'"));
    $match_datetime = $row_for_datetime['date'];
    $today = date("Y-m-d");
    $date = date('Y-m-d', strtotime($match_datetime));
	$interval = $today->diff($date);

    $query = "UPDATE `past_date` SET `available`='succeed' WHERE `partner_found`='$username' AND `partner_finder`='$partner'";
    if($interval->days>59){
	    if(mysqli_query($conn,$query)){
	    	$response["value"]="succeed";
	    	$response["message"]="Successfully submitted to server.";
	    }else{
	    	$response["value"]="failure";
	    	$response["message"]="Sorry, we were unable to submit your data in server. Please, try again.";
	    }
	}else{
    	$response["value"]="unable_to_select";
    	$response["message"]="We are sorry to say that, you can't select the the person as life partner before 60 days.";
	}
}else{
	$response["value"]=0;
	$response["message"]="Suspicious attempt.";
}
echo json_encode($response);


 mysqli_close($conn);
?>