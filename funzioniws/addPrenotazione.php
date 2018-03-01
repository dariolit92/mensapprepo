	<?php
		include 'connection.php';
			$conn=db_connect();

		$json = file_get_contents('php://input');


	$obj = json_decode($json);
	if(!isset($obj->{'codicefiscale'})OR 
			  	!isset($obj->{'pasti'}) OR !isset($obj->{'sessionid'})  ) {

	sendError("Uno o piu parametri mancanti");
			}	

		$querySession =" SELECT SessionId FROM Utente WHERE CodiceFiscale='" .$obj->{'codicefiscale'}."' AND SessionID='" .$obj->{'sessionid'}."'";
	$sessionIdQuery=	@mysqli_query($conn, $querySession);
	if($sessionIdQuery ){
	   		if(mysqli_fetch_array($sessionIdQuery)==null)
	   		sendError("Sessione scaduta");
}else
				sendError( "Commit delle transazione fallito". mysqli_error($conn));

	$queryAddebito =" SELECT PastiAddebitati FROM Utente WHERE CodiceFiscale='" .$obj->{'codicefiscale'}."'";
	$getAddebito=	@mysqli_query($conn, $queryAddebito);
	$pastiAddebitati;
	if($getAddebito ){
	   	$pastiAddebitati=mysqli_fetch_array($getAddebito)[0];
}else
				sendError( "Commit delle transazione fallito". mysqli_error($conn));
				++$pastiAddebitati;
$query =" 


	  UPDATE  Utente SET PastiAddebitati='" . $pastiAddebitati .  "', Pasti='" . $obj->{'pasti'} .  "' WHERE CodiceFiscale='". $obj->{'codicefiscale'} ."';	";



	// invia messaggio di conferma o di errore
		if($insert_query = @mysqli_query($conn, $query) ){
			
				$close = mysqli_close($conn);
				if (!$close){ 	sendError("Errore di chiusura connessione");

				}
				else {
					$jsonResponse = "Prenotazione Aggiunta";
	die( json_encode($jsonResponse));

				}
			}else{
				sendError( "Commit delle transazione fallito". mysqli_error($conn));

			}
						


		mysqli_close($conn);
		
		function  sendError( $messageError ){
		$jsonResponse = array('error' => $messageError);
	die( json_encode($jsonResponse));
	}
	


	?>