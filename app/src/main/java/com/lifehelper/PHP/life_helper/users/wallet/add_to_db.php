<?php
	
require_once '../../connection.php';
if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $amount = (int) mysqli_real_escape_string($conn,stripcslashes($_POST['amount']));
    $password = mysqli_real_escape_string($conn,stripcslashes($_POST['password']));
    $datetime = mysqli_real_escape_string($conn,stripcslashes($_POST['datetime']));
    $timezone = mysqli_real_escape_string($conn,stripcslashes($_POST['timezone']));

    $user_select_query = mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username' AND `password`='$password'");
    if (mysqli_num_rows($user_select_query)>0) {
    	$fetch_user = mysqli_fetch_assoc($user_select_query);
    	$money = (int) $fetch_user['money'];
        $wallet_query = "INSERT INTO `wallet`(`username`, `amount`, `datetime`, `timezone`, `type`) VALUES ('$username','$amount','$datetime','$timezone','add')";
    	$amount = $amount+$money;
    	$user_query = "UPDATE `users` SET `money`='$amount' WHERE `username`='$username'";
    	if(mysqli_query($conn,$wallet_query)){
    		if (mysqli_query($conn,$user_query)) {
		    	$response['value']="completed";
		    	$response['message']="Successfully added data to database.";
    		}else{
		    	$response['value']="inserted_to_wallet_only";
		    	$response['message']="Unable to update user data.";
    		}
    	}else{
		    	$response['value']="not_inserted";
		    	$response['message']="Unable to insert to database.";
    	}

    }else{
    	$response['value']="password_not_matched";
    	$response['message']="Your password didn't match";
    }
    echo json_encode($response);
}

 mysqli_close($conn);
?>