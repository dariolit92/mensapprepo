<?php
	function db_connect(){
		  $connessione = mysqli_connect("localhost", "id2182937_dario92", "calvin31", "id2182937_mensappdb");

if (!$connessione) {
    echo "Error: Unable to connect to MySQL." ;
}


		return $connessione;
	}
?>