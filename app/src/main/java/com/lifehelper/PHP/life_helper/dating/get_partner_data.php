<?php 
require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
	$username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));

	date_default_timezone_set('Asia/Dhaka');
	$query = mysqli_query($conn, "SELECT * FROM `users` WHERE `username`='$username'");
	$user_row = mysqli_fetch_assoc($query);
	$partner = $user_row['partner'];

	$partner_user_query = mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$partner'");
	$partner_user_row = mysqli_fetch_assoc($partner_user_query);

	$past_date_table_query = mysqli_query($conn,"SELECT * FROM `past_date` WHERE (`partner_finder`='$username' AND `partner_found`='$partner') OR (`partner_finder`='$partner' AND `partner_found`='$username')");
	$past_date_row = mysqli_fetch_assoc($past_date_table_query);

	$num_rows_for_selector = mysqli_num_rows(mysqli_query($conn,"SELECT * FROM `past_date` WHERE `partner_finder`='$username' AND `partner_found`='$partner' AND `available`!='succeed'"));
	if (mysqli_num_rows($partner_user_query)!=0) {
		$response["username"] = $partner_user_row['username'];
		$response["img"] =$partner_user_row['img'];
		$response["relation_type"]=$past_date_row['available'];
	}else{
		$response["username"] = '';
		$response["img"] ='';
		$response["relation_type"]='';
	}
	if($num_rows_for_selector>0){
		$response["selector"]="false";
	}else{
		$response["selector"]="true";
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