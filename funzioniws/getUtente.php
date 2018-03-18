<?php
	include 'connection.php';
	$conn=db_connect();
$params= $_GET["params"];
$obj=json_decode($params, true);
	$query="SELECT CodiceFiscale, Email, CittaResidenza, FasciaIsee, Universita, Facolta, Dipartimento, PastiAddebitati
	FROM Utente WHERE CodiceFiscale='".$obj['codicefiscale'] ."' AND SessionId='".$obj['sessionid'] ."'";
	$result=mysqli_query($conn, $query);
	if($result){
$r=mysqli_fetch_assoc($result);
print json_encode($r);		

}
		 else sendError("Errore nella query");;
	
	
	mysqli_close($conn);
	
	function sendError($messageError){
	$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}
?>