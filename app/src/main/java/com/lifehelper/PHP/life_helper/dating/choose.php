<?php


require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    date_default_timezone_set('Asia/Dhaka');
    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));

    $user_row = mysqli_fetch_assoc(mysqli_query($conn, "SELECT * FROM `users` WHERE `username`='$username'"));
    if($user_row['partner']!=''){
        $get_partner_name = $user_row['partner'];
    	$make_partner_null = mysqli_query($conn,"UPDATE `users` SET `partner`='' WHERE `username`='$username'");
        $make_partner_null_2 = mysqli_query($conn,"UPDATE `users` SET `partner`='' WHERE `username`='$get_partner_name'");
        $make_partner_null_3 = mysqli_query($conn,"UPDATE `past_date` SET `available`='canceled' WHERE (`partner_finder`='$username' AND `partner_found`='$get_partner_name') OR (`partner_finder`='$get_partner_name' AND `partner_found`='$username')");
    }

    $my_image = $user_row['img'];
    $birthYear = (int) $user_row['birth_year'];
    $height = (float) $user_row['height'];

    $find = true;

    $country = $user_row['country'];
    $char_type = $user_row['char_type'];

    if($country ==''){
        $find = false;
        $response["value"] = "update_profile";
        $response["message"] = "Sorry, we are unable to find partner for you. You must update your profile in order to find partner for you. Add your country name in your profile.";
        echo json_encode($response);
    }
    if($user_row['gender']=="Male"){
    	$find_people_s_gender = "Female";
    }else if($user_row['gender']=='Female'){
    	$find_people_s_gender = "Male";
    }else if($user_row['gender']==''){
	    $response["value"] = "update_profile";
	    $response["message"] = "Sorry, we are unable to find partner for you. You must update your profile in order to find partner for you.";
        $find=false;
	    echo json_encode($response);
    }else{
        $find = false;
	    $response["value"] = "uncommon_gender";
	    $response["message"] = "Sorry, we are unable to find partner for you. You must update your profile in order to find partner for you. Please select your gender between male and female. Our team also thinks, you tried to trick our system. If so, you are likely to be banned.";
	    echo json_encode($response);
    }
    if ($birthYear=='') {
        $find = false;
	    $response["value"] = "update_profile";
	    $response["message"] = "Sorry, we are unable to find partner for you. You must update your profile in order to find partner for you.";
	    echo json_encode($response);
    }
    
    if($find==true){
        $birth_diff_1 = $birthYear-3;
        $birth_diff_2 = $birthYear+3;
        $height_diff_1 = $height-30.48;
        $height_diff_2 = $height+30.48;
        $finder_query = mysqli_query($conn,"SELECT * FROM `users` WHERE `gender`='$find_people_s_gender' AND `birth_year` BETWEEN '$birth_diff_1' AND '$birth_diff_2' AND `height` BETWEEN '$height_diff_1' AND '$height_diff_2' AND `user_type`='member' AND `country`='$country' AND `partner`='' AND `char_type`='$char_type' AND `img`!='' AND NOT EXISTS (SELECT * FROM `past_date` WHERE (`past_date`.`partner_finder`='$username' AND `past_date`.`partner_found`=`users`.`username`) OR (`past_date`.`partner_finder`=`users`.`username` AND `past_date`.`partner_found`='$username'))");
        $number_of_rows_found = mysqli_num_rows($finder_query);
        if($number_of_rows_found==0){
            $response["value"] = "unable_to_find";
            $response["message"] = "Sorry, we are unable to find partner for you. Currently, there's no person just like you. You are an unique person. If you are unsure about your all the data given in your profile then consider checking help page.";
            echo json_encode($response);
        }else{
            if($number_of_rows_found==1){
                $finder_row = mysqli_fetch_assoc($finder_query);
               $opposite_gender_people_s_name[0] = $finder_row['username'];
               $their_image[0] = $finder_row['img'];
               $their_full_name[0] = $finder_row['full_name'];
            }else{
                while($finder_row = mysqli_fetch_assoc($finder_query)) {
                   $opposite_gender_people_s_name[] = $finder_row['username'];
                   $their_image[] = $finder_row['img'];
                   $their_full_name[] = $finder_row['full_name'];
                }
            }
            $file = 'http://127.0.0.1:5000/chooser';
            $file_headers = @get_headers($file);
            if(!$file_headers || $file_headers[0] == 'HTTP/1.1 404 Not Found' || $file_headers[0] == 'HTTP/1.0 404 Not Found') {
                if($number_of_rows_found==1){
                    $found_partner = $opposite_gender_people_s_name[0];
                }else{
                    if(mysqli_query($conn,"UPDATE `users` SET `partner`='$opposite_gender_people_s_name[0]' WHERE `username`='$username'")){
                        $currentDateTime = new DateTime();
                        $format_datetime = $currentDateTime -> format('Y-m-d H:i:s');
                        $make_partner = mysqli_query($conn,"INSERT INTO `past_date`(`partner_finder`, `partner_found`,`date`, `available`) VALUES ('$username','$opposite_gender_people_s_name[0]','$format_datetime','yes')");
                        $update_partner = mysqli_query($conn,"UPDATE `users` SET `partner`='$username' WHERE `username`='$opposite_gender_people_s_name[0]'");
                        $found_partner = $their_full_name[0];
                        $response["value"] = "found";
                        $response["message"] = "Congrats. We have found best partner for you. Her name is ".$found_partner;
                        echo json_encode($response);
                    }else{
                        $response["value"] = "found_but_not_inserted";
                        $response["message"] = "Sorry, we found the best partner for you but for some reason the data wasn't saved in the database. Please, try again.";
                        echo json_encode($response);
                    }
                }
            }
            else {
                if($number_of_rows_found==1){
                    $found_partner = $opposite_gender_people_s_name[0];
                }else{
                    for ($x = 0; $x <= count($opposite_gender_people_s_name)-1; $x++) {
                        $opposite_names = '';
                        $their_images = '';
                        if($x!=count($opposite_gender_people_s_name)-1){
                            $opposite_names = $opposite_names.''.$opposite_gender_people_s_name[x].',';
                            $their_images = $their_images.''.$their_image[x].',';
                        }else{
                            $opposite_names = $opposite_names.''.$opposite_gender_people_s_name[x];
                            $their_images = $their_images.''.$their_image[x];
                        }
                    }
                    $json = file_get_contents('http://127.0.0.1:5000/chooser?name='.$username.'&my_image='.$my_image.'&opposite_name='.$opposite_names.'&pic='.$their_images);
                    $obj = json_decode($json,true);
                    $found_partner = $obj['selection'];
                }
                if(mysqli_query($conn,"UPDATE `users` SET `partner`='$found_partner' WHERE `username`='$username'")){
                    $currentDateTime = new DateTime();
                    $format_datetime = $currentDateTime -> format('Y-m-d H:i:s');
                    $make_partner = mysqli_query($conn,"INSERT INTO `past_date`(`partner_finder`, `partner_found`, `date`,`available`) VALUES ('$username','$found_partner','$format_datetime','yes')");
                    $update_partner = mysqli_query($conn,"UPDATE `users` SET `partner`='$username' WHERE `username`='$found_partner'");
                    $response["value"] = "found";
                    $response["message"] = "Congrats. We have found best partner for you. Her name is ".$found_partner;
                    echo json_encode($response);
                }else{
                    $response["value"] = "found_but_not_inserted";
                    $response["message"] = "Sorry, we found the best partner for you but for some reason the data wasn't saved in the database. Please, try again.";
                    echo json_encode($response);
                }
            }
        }
    }
}else{
	
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
 mysqli_close($conn);
?>
