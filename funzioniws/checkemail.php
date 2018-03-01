<?php
	include 'connection.php';
	$conn=db_connect();
	$query="SELECT CodiceFiscale FROM Utente WHERE Email='" . ($_GET['email']) . "'";
	$result=mysqli_query($conn, $query);
	if($result){
		if(mysqli_fetch_array($result)[0]) $output["output"]="notavailable";
		else $output["output"]="available";
	} else $output["output"]="connectionerror";
	$array[0]=$output;
	print(json_encode($array));
	
	mysqli_close($conn);
?>