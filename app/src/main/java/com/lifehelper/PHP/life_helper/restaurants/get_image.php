<?php
	require_once '../connection.php';

	$id = mysqli_real_escape_string($conn,stripcslashes($_POST['id']));


	$row = mysqli_fetch_assoc(mysqli_query($conn, "SELECT * FROM `restaurants` WHERE `id`='$id'"));

	$response["image"] = $row[];
	echo json_encode($array);
?>