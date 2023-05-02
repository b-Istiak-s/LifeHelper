<?php

require_once '../../connection.php';

if ( $_SERVER['REQUEST_METHOD'] == 'POST' ){

    $username = mysqli_real_escape_string($conn,stripcslashes($_POST['username']));
	$limit = mysqli_real_escape_string($conn,stripcslashes($_POST['limit']));
	$off_set = mysqli_real_escape_string($conn,stripcslashes($_POST['off_set']));

    $query = mysqli_query($conn,"
    	SELECT
		    `message_between`.`id` AS `id`,
		    `message_between`.`user_1` AS `user_1`,
		    `message_between`.`user_2` AS `user_2`,
		    `message_between`.`last_message` AS `last_message`,
		    `message_between`.`last_message_from` AS `last_message_from`,
		    `message_between`.`last_message_timestamp` AS `last_message_timestamp`,
		    `message_between`.`read` AS `read`,
		    `users`.`img` AS `img`
		FROM
		    `message_between`
		INNER JOIN `users` ON `users`.`username` = `message_between`.`user_2`
		WHERE
		    `user_1` = '$username'
		ORDER BY
		    `last_message_timestamp`
		DESC
		LIMIT $limit OFFSET $off_set
    	");

    if (mysqli_num_rows($query)==0) {
    	$query = mysqli_query($conn,"
    	SELECT
		    `message_between`.`id` AS `id`,
		    `message_between`.`user_1` AS `user_1`,
		    `message_between`.`user_2` AS `user_2`,
		    `message_between`.`last_message` AS `last_message`,
		    `message_between`.`last_message_from` AS `last_message_from`,
		    `message_between`.`last_message_timestamp` AS `last_message_timestamp`,
		    `message_between`.`read` AS `read`,
		    `users`.`img` AS `img`
		FROM
		    `message_between`
		INNER JOIN `users` ON `users`.`username` = `message_between`.`user_1`
		WHERE
		    `user_2` = '$username'
		ORDER BY
		    `last_message_timestamp`
		DESC
		LIMIT $limit OFFSET $off_set
    	");
    }
    
	$array = array();
    while ($row = mysqli_fetch_assoc($query)) {
        array_push($array, array(
            'id' => $row['id'], 
            'user_1' => $row['user_1'], 
            'user_2' => $row['user_2'],
            'message' => $row['last_message'],
            'last_message_from' => $row['last_message_from'],
            'read' => $row['read'],
            'img' => $row['img'],
            'timestamp' => $row['last_message_timestamp']));
	}
	echo json_encode($array);


} else {
    $response["value"] = 0;
    $response["message"] = "Oops! Try again!";
    echo json_encode($response);
}

//close db connection
mysqli_close($conn);

?>