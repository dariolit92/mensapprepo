	<?php
	include 'connection.php';
	$conn=db_connect();
$params= $_GET["params"];
$obj=json_decode($params, true);

	$query=	"SELECT * FROM Piatto WHERE Mensa='".$obj['mensa'] ."' AND DataPiatto='".$obj['datapiatto'] ."'
	AND Pasto=(SELECT Id FROM Pasto
   WHERE TipoPasto='".$obj['tipopasto']."' )";

	$result=mysqli_query($conn, $query);
	
	if($result  ){
	$rows=null;	
while($r = mysqli_fetch_assoc($result) ) {
    $rows[] =$r ;
}
if($rows==null){
sendError("Nessun pasto trovato");
}
print json_encode($rows);		

			
			} else sendError("Errore nella query");;
	
	
	mysqli_close($conn);
	
	function sendError($messageError){
	$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}
	?>