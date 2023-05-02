<?php 
require_once '../connection.php';

$username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
$limit = mysqli_real_escape_string($conn,stripcslashes($_POST['limit']));
$off_set = mysqli_real_escape_string($conn,stripcslashes($_POST['off_set']));
$type = mysqli_real_escape_string($conn,stripcslashes($_POST['type']));

$user_query = mysqli_query($conn,"SELECT * FROM `users` WHERE `username`='$username'");
$user_row = mysqli_fetch_assoc($user_query);
$gender = $user_row['gender'];
$birth_year = $user_row['birth_year'];
if($gender == 'Male'){
    $gender_search = 'Female';
}else if ($gender=='Female') {
    $gender_search = 'Male';
}


if ($gender=='Male' || $gender=='Female') {
    if ($type=='all') {
        $query = mysqli_query($conn, "SELECT * FROM `users` WHERE `gender`='$gender_search' ORDER BY `birth_year` DESC LIMIT $limit OFFSET $off_set");
    }else if ($type=='age') {
        $query = mysqli_query($conn, "SELECT * FROM `users` WHERE `gender`='$gender_search' AND `birth_year`='$birth_year' ORDER BY `birth_year` DESC LIMIT $limit OFFSET $off_set");
    }else if ($type=='all_but_not_age') {
        $query = mysqli_query($conn, "SELECT * FROM `users` WHERE `gender`='$gender_search' AND `birth_year`!='$birth_year' ORDER BY `birth_year` DESC LIMIT $limit OFFSET $off_set");
    }
}else{
    if ($type=='all') {
        $query = mysqli_query($conn, "SELECT * FROM `users` ORDER BY `birth_year` DESC LIMIT $limit OFFSET $off_set");
    }else if ($type=='age') {
        $query = mysqli_query($conn, "SELECT * FROM `users` WHERE `birth_year`='$birth_year' ORDER BY `birth_year` DESC LIMIT $limit OFFSET $off_set");
    }else if ($type=='all_but_not_age') {
        $query = mysqli_query($conn, "SELECT * FROM `users` WHERE `birth_year`!='$birth_year' ORDER BY `birth_year` DESC LIMIT $limit OFFSET $off_set");
    }
}

$array = array();
if (mysqli_num_rows($query)) {
    while ($row = mysqli_fetch_assoc($query)) {
        array_push($array, array(
            'username' => $row['username'], 
            'email' => $row['mail'], 
            'full_name' => $row['full_name'],
            'phone' => $row['phone'],
            'latitude' => $row['latitude'],
            'longitude' => $row['longitude'],
            'city' => $row['city'],
            'country' => $row['country'],
            'img' => $row['img'],
            'gender' => $row['gender'],
            'birth_year' => $row['birth_year'],
            'hobby' => $row['hobby'],
            'height' => $row['height'],
            'char_type' => $row['char_type'],
            'partner' => $row['partner'],
            'last_active' => $row['last_active'],
            'user_type' => $row['user_type'] ));
    }
}
echo json_encode($array);

?>
