<?php 
require_once '../connection.php';

$username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
$user_row = mysqli_fetch_assoc(mysqli_query($conn, "SELECT * FROM `users` WHERE `username`='$username'"));
    if($user_row['partner']!=''){
        $get_partner_name = $user_row['partner'];
        if(mysqli_query($conn,"UPDATE `users` SET `partner`='' WHERE `username`='$username'")){
            $make_partner_null_2 = mysqli_query($conn,"UPDATE `users` SET `partner`='' WHERE `username`='$get_partner_name'");
            $make_partner_null_3 = mysqli_query($conn,"UPDATE `past_date` SET `available`='canceled' WHERE (`partner_finder`='$username' AND `partner_found`='$get_partner_name') OR (`partner_finder`='$get_partner_name' AND `partner_found`='$username')");
            $response["value"] = "removoed";
            $response["message"] = "The partner was removed.";
            echo json_encode($response);
        }
    }else{    
        $response["value"] = "no_data";
        $response["message"] = "You have no partner.";
        echo json_encode($response);
    }

?>