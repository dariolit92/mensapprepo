<?php
	include 'connection.php';
	$conn=db_connect();
	$idutente;
	$json = file_get_contents('php://input');

$obj = json_decode($json);
	if(!isset($obj->{'email'})OR !isset($obj->{'password'}) OR
		!isset($obj->{'codicefiscale'} )OR !isset($obj->{'fasciaisee'} )OR !isset($obj->{'universita'} )OR !isset($obj->{'facolta'}) 
		OR !isset($obj->{'dipartimento'})OR !isset($obj->{'cittaresidenza'})OR !isset($obj->{'pastiaddebitati'})){
$jsonResponse = array('error' => "Uno o piu parametri mancanti");

die( json_encode($jsonResponse));		}
		$cf=strtoupper(($obj->{'codicefiscale'}));

	

			$sessionid=dechex(rand(100000000, 999999999));

		$query= "
			INSERT INTO Utente
			(CodiceFiscale, Email, Password, CittaResidenza, FasciaIsee, Universita, Facolta, Dipartimento, SessionId, PastiAddebitati)
			VALUES('" . $cf . "', 
			'" .$obj->{'email'} . "', 
			'" . sha1($obj->{'password'}) . "', 			
			'" .$obj->{'cittaresidenza'} . "', 

			'" .$obj->{'fasciaisee'} . "',  
			'" .$obj->{'universita'}. "',
			 '" .$obj->{'facolta'} . "',  
			 '" .$obj->{'dipartimento'} . "',
			 '" . sha1($sessionid) . "',
			 '" . $obj->{'pastiaddebitati'} . "');
					
   
";

// invia messaggio di conferma o di errore
	if($insert_query = @mysqli_query($conn, $query)){

		
			$close = mysqli_close($conn);
			if (!$close){ 	$jsonResponse = array('error' => "Errore di chiusura connessione");

die( json_encode($jsonResponse));
			}
			else {
				$jsonResponse = array('response' => "register",'codicefiscale' => $cf , 'sessionid' => $sessionid);

die( json_encode($jsonResponse));
			}
		}else{
			$jsonResponse = array('error' => "Errore nella query di inserimento");

die(json_encode($jsonResponse));
		}
					


	mysqli_close($conn);
	

?>