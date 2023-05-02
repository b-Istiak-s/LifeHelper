<?php 

require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    date_default_timezone_set('Asia/Dhaka');
    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $peopleUsername = mysqli_real_escape_string($conn,stripcslashes($_POST['people_username']));
    $interacted = mysqli_real_escape_string($conn,stripcslashes($_POST['interacted']));
    $datetime = date('Y-m-d H:i:s');

    $check_user="SELECT * FROM `interacted` WHERE `username`='$username' AND `interacted_with`='$peopleUsername'";
    $query = "INSERT INTO `interacted` (`username`, `interacted_with`,`date`, `interaction_type`) VALUES ('$username','$peopleUsername','$datetime','$interacted')";
    
    $result =  mysqli_query($conn,$check_user);
    $num_rows =mysqli_num_rows($result);

    if($num_rows>5){
        $user_row = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'"));
        $partner = $user_row['partner'];
        $user_gender = $user_row['gender'];
        if ($partner=="") {
            $people_user_s_row = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$peopleUsername'"));
            $people_s_partner = $people_user_s_row['partner'];
            $people_s_gender = $people_user_s_row['gender'];
            $peopleUsername = $people_user_s_row['username'];
            if ($people_s_partner=='') {
                if ($user_gender==$people_s_gender OR $user_gender=='' OR $people_s_gender=='') {
                    $response["value"] = "silence";
                    $response["message"] = "Silence of awkwardness. One of the user's sex is incorrect.";
                    echo json_encode($response);
                }else{
                    $currentDateTime = new DateTime();
                    $format_datetime = $currentDateTime -> format('Y-m-d H:i:s');
                    $update_user_s_partner = mysqli_query($conn,"UPDATE `users` SET `partner`='$peopleUsername' WHERE `username`='$username'");
                    $make_partner = mysqli_query($conn,"INSERT INTO `past_date`(`partner_finder`, `partner_found`, `date`,`available`) VALUES ('$username','$peopleUsername','$format_datetime','yes')");
                    $update_partner = mysqli_query($conn,"UPDATE `users` SET `partner`='$username' WHERE `username`='$peopleUsername'");
                    
                    $response["value"] = "found";
                    $response["message"] = "Congrats. From now on, ".$make_partner." is your partner.";
                    echo json_encode($response);
                }
            }else{
                $response["value"] = "taken";
                $response["message"] = "We have noticed your interaction with ".$peopleUsername.". But sadly the person isn't single.";
                echo json_encode($response);
            }
        }else{
            $response["value"] = "not_single";
            $response["message"] = "Hey, pervert. We have noticed your interaction. But you already have a partner. Stop looking at others. Spend time with your partner, not everyone is as good as them.";
            echo json_encode($response);
        }
    }
    else{
        if(mysqli_num_rows(mysqli_query($conn,"SELECT * FROM `interacted` WHERE `username`='$username' AND `interacted_with`='$peopleUsername' AND `interaction_type`='$interacted'"))>2){
            $response["value"] = "do_nothing";
            $response["message"] = "Interaction type crossed limit. The interaction isn't required.";
            echo json_encode($response);
        }else{
            if ( mysqli_query($conn, $query) ){
                $response["value"] = "success";
                $response["message"] = "We have noted your interaction";
                echo json_encode($response);
            } else {
                $response["value"] = "failure";
                $response["message"] ="Sorry, for server error, we couldn't note your interaction.";
                echo json_encode($response);
            }
        }
    
    }
    

   

} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
mysqli_close($conn);

?>
