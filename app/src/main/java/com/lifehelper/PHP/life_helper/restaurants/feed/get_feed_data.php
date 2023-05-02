<?php 
require_once '../../connection.php';

$id = mysqli_real_escape_string($conn,stripcslashes($_POST['id']));
$limit = mysqli_real_escape_string($conn,stripcslashes($_POST['limit']));
$off_set = mysqli_real_escape_string($conn,stripcslashes($_POST['off_set']));

$query = mysqli_query($conn,"
        SELECT
            `users`.`img` AS `usrImg`,
            `restaurants`.`restaurant_name` AS `restaurant_name`,
            `restaurant_feed`.`username` AS `username`,
            `restaurant_feed`.`description` AS `description`,
            `restaurant_feed`.`image` AS `image`,
            `restaurant_feed`.`timestamp` AS `timestamp`
        FROM
            `restaurant_feed`
        INNER JOIN `restaurants` ON `restaurants`.`id` = '$id'
        INNER JOIN `users` ON `users`.`username` = `restaurant_feed`.`username`
        WHERE
            `restaurant_id` = '$id'
        ORDER BY
            `timestamp`
        DESC
        LIMIT $limit OFFSET $off_set
    ");

$array = array();
while ($row = mysqli_fetch_assoc($query)) {
    array_push($array, array(
        'img' => $row['usrImg'], 
        'restaurant_name' => $row['restaurant_name'], 
        'username' => $row['username'], 
        'desc' => $row['description'],
        'post_image' => $row['image'],
        'timestamp' => $row['timestamp']));
}
echo json_encode($array);

?>
