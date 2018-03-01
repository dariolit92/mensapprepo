<?php
	include 'connection.php';
		$conn=db_connect();

	$json = file_get_contents('php://input');

$obj = json_decode($json);
		
	$queryGetId= "SELECT MAX(Id)
FROM Piatto; 
";	
$result=mysqli_query($conn,$queryGetId);

	$idPiatto=mysqli_fetch_array($result, MYSQLI_NUM)[0];

$queryGetIdPasto= "SELECT MAX(Id)
FROM Pasto; 
";	
$resultIdPasto=mysqli_query($conn,$queryGetIdPasto);

	$idPasto=mysqli_fetch_array($resultIdPasto, MYSQLI_NUM)[0];
$idPasto=1+$idPasto;

$today =  date('Y-m-d');
	$queryGetId= "SELECT MAX(Id)
FROM Piatto; 
";	
$result=mysqli_query($conn,$queryGetId);

	$idPiatto=mysqli_fetch_array($result, MYSQLI_NUM)[0];
$idPiatto=1+$idPiatto;

$i=0;
	$query ="";
	$idPiatti="";

while($i<count($obj)){
$query = $query."SET AUTOCOMMIT=0;
  SET FOREIGN_KEY_CHECKS=0;
  START TRANSACTION;
  INSERT INTO Piatto(  Id,  Nome,  TipoPiatto,  Mensa,  DataPiatto,  Pasto)			
  VALUES('" .$idPiatto ."', 		'" . $obj[$i] ->{'nome'}. "', '" .$obj[$i]->{'tipopiatto'} . "', 	'" .$obj[$i]->{'mensa'} . "',  '" .$today. "', 	'" .$idPasto . "' );";

if($i==count($obj)-1){
    $idPiatti.= $idPiatto;

  $query = $query." INSERT INTO   Pasto(Id, TipoPasto, IdPiatti)
			VALUES(	'" .$idPasto . "', 	'" .$obj[0]->{'pasto'}. "', '" .$idPiatti . "');
			SET FOREIGN_KEY_CHECKS=1;

			COMMIT;";
}else{
    $idPiatti.= $idPiatto.'-';

}
  $i++;
    $idPiatto++;

}





// invia messaggio di conferma o di errore
	if($insert_query = @mysqli_multi_query($conn, $query)  ){
			$close = mysqli_close($conn);
			if (!$close){ 	sendError("Errore di chiusura connessione");

			}
			else {
				$jsonResponse = "Piatti Aggiunti";
die( json_encode($jsonResponse));

			}
		}else{
			sendError( "Commit delle transazione fallito". mysqli_error($conn));

		}
					


	mysqli_close($conn);
	function sendError($messageError){
	$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}


?>