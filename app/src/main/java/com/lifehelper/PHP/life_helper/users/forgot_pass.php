<?php 

require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));

    $check_user="SELECT * FROM `users` where `username`='$username' ";
    
    $result = mysqli_query($conn,$check_user);
    $num_rows = mysqli_num_rows($result);


    if($num_rows>0){
        $row = mysqli_fetch_assoc($result);
        $digits = 4;
        $random = rand(pow(10, $digits-1), pow(10, $digits)-1);
        $to = $row['mail'];
        $subject = "Verification code of Life Helper";
        
        $message = "<b>Hello, ".$username."</b>.<br/> Your verification code is : ".$random;
        
        $header = "From:Istiakshovon011@gmail.com \r\n";
        $header .= "MIME-Version: 1.0\r\n";
        $header .= "Content-type: text/html\r\n";
            
        if (mail ($to,$subject,$message,$header)) {
            $response["value"] = "sent";
            $response["message"] = $to.','.$random;
            echo json_encode($response);
        }else{
            $response["value"] = "failure";
            $response["message"] = "We couldn't send mail, please try again later.";
            echo json_encode($response);
        }
    }
    else{
        $response["value"] = "failure";
        $response["message"] ="Username is invalid.";
        echo json_encode($response);
    }
    

   

} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
 mysqli_close($conn);

?>