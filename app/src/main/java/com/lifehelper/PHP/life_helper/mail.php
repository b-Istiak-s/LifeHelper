<?php
	error_reporting(-1);
	ini_set('display_errors', 'On');
	set_error_handler("var_dump");
         $to = "billyistiak001@gmail.com";
         $subject = "This is subject";
         
         $message = "<b>This is HTML message.</b>";
         $message .= "<h1>This is headline.</h1>";
         
         $header = "From:Istiakshovon011@gmail.com \r\n";
         $header .= "MIME-Version: 1.0\r\n";
         $header .= "Content-type: text/html\r\n";
         
         var_dump(mail ($to,$subject,$message,$header));
         
?>
