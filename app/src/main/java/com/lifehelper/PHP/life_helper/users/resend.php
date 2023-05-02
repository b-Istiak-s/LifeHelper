<?php 

require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $email = mysqli_real_escape_string($conn,stripcslashes($_POST['email']));
    $type = mysqli_real_escape_string($conn,stripcslashes($_POST['type']));

    $query = "UPDATE `users` SET `verification`='true' WHERE `username`='$username'";
    

    $digits = 4;
    $random = rand(pow(10, $digits-1), pow(10, $digits)-1);

    if($email == "email"){
        $row = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'"));
        $email = $row['mail'];
    }

    if ( $type=="activate"){
        if(mysqli_query($conn,$query)){
            $make_dir = mkdir($username);
            $response["value"] = "activated";
            $response["message"] ="Successfully activated your account.";
        }else{
            $response["value"] = "not_updated";
            $response["message"] ="Couldn't active for server error.";
        }
    } else if($type=="send") {
        $to = $email;
        $subject = "Verification code of Life Helper";
        
        $message = "<b>Hello, ".$username."</b>. Your verification code is : ".$random;
        
        $header = "From:Istiakshovon011@gmail.com \r\n";
        $header .= "MIME-Version: 1.0\r\n";
        $header .= "Content-type: text/html\r\n";
        
        $retval = mail ($to,$subject,$message,$header);
        $response["value"] = "sent";
        $response["message"] = $random;
    }else{
        $response["value"] = "mistake";
        $response["message"] ="Something went wrong, \n Please try again!";
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
