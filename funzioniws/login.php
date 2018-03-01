<?php
	include 'connection.php';

	$json = file_get_contents("php://input");

$obj = json_decode($json);
	if(!isset($obj->{'email'})){
		sendError("Email mancante");
	} 
	if(!isset($obj->{'password'}))	sendError("Password mancante");



	$db = db_connect(); 
	if(!$db) echo mysqli_error();
	
	// verifica se e-mail e password matchano; password criptata tramite l'algoritmo hash sha1()
	$query = "SELECT CodiceFiscale FROM Utente
		WHERE Email='" . ($obj->{'email'}) . "'
		AND (Password='" . sha1($obj->{'password'}) . "'
			
		)";
	$result = mysqli_query($db, $query);
	if (!$result) sendError("Problema con la richiesta: " . $query . "; " . mysqli_error());

	else{
		$close = mysqli_close($db );
		if (!$close) sendError("Errore di chiusura connessione");
		else {
			$cf=mysqli_fetch_array($result)[0];
			if($cf==null) sendError("Password e indirizzo e-mail\nnon corrispondono");
			else {
				$db = db_connect();
				$sessionid=sha1(dechex(rand(100000000, 999999999)));
				$query = "UPDATE Utente
					SET SessionId='" . $sessionid . "'
					WHERE CodiceFiscale='" . $cf . "';";
				$result = mysqli_query($db, $query);
				if (!$result) sendError("Problema con la richiesta: " . $query . "; " . mysqli_error());
				else{
					if (!$close) sendError( "Errore di chiusura connessione");
					else {
					    	$query2 = "SELECT PastiAddebitati FROM Utente
		WHERE CodiceFiscale='" . $cf . "'";
			$resultPasti = mysqli_query($db, $query2);
				if (!$resultPasti) 
				sendError("Problema con la richiesta: " . $query . "; " . mysqli_error());	

			$pastiaddebitati=mysqli_fetch_array($resultPasti)[0];


							$jsonResponse = array('response' => "login",'codicefiscale' => $cf ,'sessionid' => $sessionid, 'pastiaddebitati'=>$pastiaddebitati);
							

die( json_encode($jsonResponse));
}
					}
						
					$close =mysqli_close($db );
				}
			}
		}
	
function sendError($messageError){
		$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}
	
?> 