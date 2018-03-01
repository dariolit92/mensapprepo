<?php
	include 'connection.php';
	$conn=db_connect();

	$query="SELECT * FROM Mensa";
	$result=mysqli_query($conn, $query);
	if($result){

while($r = mysqli_fetch_assoc($result)) {
    $rows[] = $r;
}
print json_encode($rows);		
}
		 else sendError("Errore nella query");;
	
	
	mysqli_close($conn);
	
	function sendError($messageError){
	$jsonResponse = array('error' => $messageError);
die( json_encode($jsonResponse));
}
?>