<?php 

require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $email = mysqli_real_escape_string($conn,stripcslashes($_POST['email']));
    $password = mysqli_real_escape_string($conn,stripcslashes($_POST['password']));
    $fullName = mysqli_real_escape_string($conn,stripcslashes($_POST['full_name']));
    $phone = mysqli_real_escape_string($conn,stripcslashes($_POST['phone']));
    $userType = mysqli_real_escape_string($conn,stripcslashes($_POST['user_type']));
    $latitude = mysqli_real_escape_string($conn,stripcslashes($_POST['latitude']));
    $longitude = mysqli_real_escape_string($conn,stripcslashes($_POST['longitude']));


    $json = file_get_contents('https://api.tomtom.com/search/2/reverseGeocode/'.$latitude.','.$longitude.'.json?key=xPNKP4hzkpFc4VE2ssKJEG1xYL8SHlQw');
    $obj = json_decode($json,true);
    $countryName = $obj['addresses'][0]['address']['country'];
    $cityName = $obj['addresses'][0]['address']['localName'];

    $check_user="SELECT * FROM `users` where `username`='$username'";
    $query = "INSERT INTO `users`( `username`, `mail`, `password`, `full_name`, `phone`, `latitude`, `longitude`, `city`, `country`, `img`, `user_type`, `gender`, `hobby`, `char_type`, `partner`, `last_active`, `verification`,`birth_year`,`height`,`money`) VALUES ('$username','$email','$password','$fullName','$phone','$latitude','$longitude','$cityName','$countryName','','$userType','','','','','','','','',0)";
    
    $result =  mysqli_query($conn,$check_user);
    $num_rows =mysqli_num_rows($result);

    $digits = 4;
    $random = rand(pow(10, $digits-1), pow(10, $digits)-1);
    if($num_rows>0){

        $response["value"] = "exists";
        $response["message"] = "User Already Exists!";
        echo json_encode($response);
    }
    else{
        $to = $email;
        $subject = "Verification code of Life Helper";
        
        $message = "<b>Hello, ".$username."</b>. Your verification code is : ".$random;
        
        $header = "From:Istiakshovon011@gmail.com \r\n";
        $header .= "MIME-Version: 1.0\r\n";
        $header .= "Content-type: text/html\r\n";
        
        $retval = mail ($to,$subject,$message,$header);
        if ( mysqli_query($conn, $query) ){
            $response["value"] = "success";
            $response["message"] = $random;
            echo json_encode($response);
            
        } else {
            $response["value"] = "failure";
            $response["message"] ="Failed to add, \n Please try again!";
            echo json_encode($response);
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
