<?php
	include 'connection.php';
	$conn=db_connect();
$params= $_GET["params"];
$obj=json_decode($params, true);
		$querySession =" SELECT SessionId FROM Utente WHERE CodiceFiscale='" .$obj['codicefiscale']."' AND SessionId='" .$obj['sessionid']."'";
	$sessionIdQuery=	@mysqli_query($conn, $querySession);
	if($sessionIdQuery ){
	   		if(mysqli_fetch_array($sessionIdQuery)==null)
	   		sendError("Sessione scaduta");
}else
				sendError( "Commit delle transazione fallito". mysqli_error($conn));

	$query="SELECT Pasti FROM Utente WHERE CodiceFiscale='" .$obj['codicefiscale']."'" ;
	$result=mysqli_query($conn, $query);
	if($result){

print (mysqli_fetch_array($result)[0]);		
}
		 else sendError("Errore nella query");;
	
	
	mysqli_close($conn);
	
	function sendError($messageError){
	$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}
?>