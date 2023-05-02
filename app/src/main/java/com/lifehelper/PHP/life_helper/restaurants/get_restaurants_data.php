<?php 
require_once '../connection.php';

$username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
$limit = mysqli_real_escape_string($conn,stripcslashes($_POST['limit']));
$off_set = mysqli_real_escape_string($conn,stripcslashes($_POST['off_set']));

$user_row = mysqli_fetch_assoc(mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'"));
$city = $user_row['city'];


$query = mysqli_query($conn, "SELECT * FROM `restaurants` WHERE `city`='$city' LIMIT $limit OFFSET $off_set");

$array = array();
if (mysqli_num_rows($query)) {
    while ($row = mysqli_fetch_assoc($query)) {
        array_push($array, array(
            'id' => $row['id'],
            'restaurant_name' => $row['restaurant_name'], 
            'creator_username' => $row['creator_username'], 
            'address' => $row['address'],
            'city' => $row['city'],
            'country' => $row['country'],
            'opening_hours' => $row['opening_hours'],
            'closing_hours' => $row['closing_hours'],
            'map_link_address' => $row['map_link_address'],
            'phone_number' => $row['phone_number'],
            'restaurant_logo' => $row['logo'],
            'current_status' => $row['current_status']));
    }
}
echo json_encode($array);

?>
