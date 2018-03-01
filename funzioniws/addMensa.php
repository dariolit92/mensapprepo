<?php
	include 'connection.php';
		$conn=db_connect();

	$json = file_get_contents('php://input');

$obj = json_decode($json);
	if(!isset($obj->{'indirizzo'})OR !isset($obj->{'citta'}) OR
		!isset($obj->{'numeroposti'} )) {

sendError("Uno o piu parametri mancanti");
		}		
		
		$query= "
			INSERT INTO Mensa
			(Indirizzo, Citta, NumeroPosti, OrarioAperturaPranzo, OrarioChiusuraPranzo, OrarioAperturaCena, OrarioChiusuraCena)
			VALUES(
			'" .$obj->{'indirizzo'} . "', 
			'" . $obj->{'citta'} . "', 			
			'" .$obj->{'numeroposti'} . "', 

			'" .$obj->{'orarioaperturapranzo'} . "',  
			'" .$obj->{'orariochiusurapranzo'}. "',
			 '" .$obj->{'orarioaperturacena'} . "',  
			 '" .$obj->{'orariochiusuracena'} . "'
			 );
					
   
";

// invia messaggio di conferma o di errore
	if($insert_query = @mysqli_query($conn, $query)){

		
			$close = mysqli_close($conn);
			if (!$close){ 	$jsonResponse = array('error' => "Errore di chiusura connessione");

die (json_encode($jsonResponse));
			}
			else {
				$jsonResponse = array('indirizzo' => $obj->{'indirizzo'} , 'citta' => $obj->{'citta'});

die (json_encode($jsonResponse));
			}
		}else{
			$jsonResponse = array('error' => "Errore nella query di inserimento");

die( json_encode($jsonResponse));
		}
					


	mysqli_close($conn);
	function sendError($messageError){
	$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}


?>