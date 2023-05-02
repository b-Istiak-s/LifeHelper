<?php 

require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
    $password = mysqli_real_escape_string($conn,stripcslashes($_POST['password']));

    $check_user="SELECT * FROM `users` where `username`='$username'";
    
    $result = mysqli_query($conn,$check_user);
    $num_rows = mysqli_num_rows($result);


    if($num_rows>0){
        $query = "UPDATE `users` SET `password`='$password' WHERE `username`='$username'";
        if (mysqli_query($conn,$query)) {
            $response["value"] = "succeed";
            $response["message"] = "Successfully changed password.";
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