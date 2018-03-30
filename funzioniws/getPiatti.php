<?php
	include 'connection.php';
	$conn=db_connect();
$params= $_GET["params"];
$obj=json_decode($params, true);

$array=explode ("-",$obj['idpiatti']);

$query="";
    $rows=[] ;
for( $i=0;$i<count($array); $i++ ){
  $id=   $array[$i];
    	$query="SELECT * FROM Piatto WHERE Id='" .$id."'AND Pasto='" .$obj['idpasto']."';" ;
	$result=mysqli_query($conn, $query);
	if($result){
$r = mysqli_fetch_assoc($result);
    $rows[] = $r;

}
		 else sendError("Errore nella query");;
}
print json_encode($rows);		



	
	
	mysqli_close($conn);
	
	

	function sendError($messageError){
	$jsonResponse = array(array('error' => $messageError));
die( json_encode($jsonResponse));
}
?>