<?php
	include 'connection.php';
		$db = db_connect(); 
	if(!$db) echo mysqli_error();
		$json = file_get_contents('php://input');


	$obj = json_decode($json);
	
	$query = "UPDATE Utente
		SET Sessionid=NULL
		WHERE CodiceFiscale='" 	.$obj->{'codicefiscale'}.  "'";
	$result = mysqli_query($db, $query);
	if (!$result) sendError("Problema con la richiesta: " . $query . "<br/>" . pg_last_error());
	else{
		$close = mysqli_close($db );
		if (!$close) sendError("Errore di chiusura connessione");
		else {
	$jsonResponse = array('response' => "logout");
	die( json_encode($jsonResponse));
	}
	}
	
	function sendError($messageError){
		$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}
?>