<?php 
require_once '../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){
$username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
$password = mysqli_real_escape_string($conn,stripcslashes($_POST['password']));
$query = mysqli_query($conn, "SELECT * FROM `users` WHERE `username`='$username'");

$array = array();
$row = mysqli_fetch_assoc($query);

$response["username"] = $row['username'];
$response["email"] = $row['mail'];
$response["full_name"] = $row['full_name'];
$response["phone"] =$row['phone'];
$response["latitude"] =$row['latitude'];
$response["longitude"] = $row['longitude'];
$response["city"] =$row['city'];
$response["country"] =$row['country'];
$response["img"] =$row['img'];
$response["gender"] =$row['gender'];
$response["birth_year"] =$row['birth_year'];
$response["hobby"] =$row['hobby'];
$response["height"] =$row['height'];
$response["char_type"] =$row['char_type'];
$response["partner"] =$row['partner'];
$response["last_active"] =$row['last_active'];
$response["user_type"] =$row['user_type'];

echo json_encode($response);

} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
 mysqli_close($conn);
?>