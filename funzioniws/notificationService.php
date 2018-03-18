<?php

	include 'connection.php';
	$conn=db_connect();

	$query="SELECT * FROM Mensa";
	$result=mysqli_query($conn, $query);
	$arrayJson=null;
	if($result){

while($r = mysqli_fetch_assoc($result)) {
    $rows[] = $r;
}
$arrayJson= json_encode($rows);		
}
		 else sendError("Errore nella query");
	
	
	

// Message to send
$message;

$registrationId = '164563496568';
$apiKey = "AIzaSyDcgvCSTjeGYgeTYpps5-xLs20RC6qkaA8";
$dec = json_decode($arrayJson);
$today =  date("H:i:s");
$oraNotificaPranzo=null;
$oraNotificaCena=null;
for($idx = 0; $idx < count($dec); $idx++){
    $obj = (Array)$dec[$idx];
  $pranzo = date("H:i:s", $obj["OrarioAperturaPranzo"]);
     $cena =    date("H:i:s", $obj["OrarioAperturaCena"]);

strtotime('+3 hours', $today);
echo (strtotime($differenzaCena));
if($differenzaPranzo==strtotime('02:15:00')){
    
    $oraNotificaPranzo=date(strtotime($pranzo)-strtotime('02:15:00'));
$message+="Tra 15 minuti scade il tempo per prenotarti a pranzo alla mensa di "+ $obj["Indirizzo"]+" ;";
}
if($differenzaCena==strtotime('02:15:00')){
    echo( $differenzaCena);

    $oraNotificaCena=date(strtotime($pranzo)-strtotime('02:15:00'));

$message+="Tra 15 minuti scade il tempo per prenotarti a cena alla mensa di "+ $obj["Indirizzo"]+" ;";
}
}
$response=null;
if($oraNotificaPranzo!=null){
$response = sendNotification( 
                $apiKey, 
                array($registrationId), 
                array('message' => $message,'time'=> $oraNotificaPranzo) );
}else if($oraNotificaCena!=null){
    $response = sendNotification( 
                $apiKey, 
                array($registrationId), 
                array('message' => $message,'time'=> $oraNotificaCena) );
}
 	mysqli_close($conn);

echo $response;


/**
 * The following function will send a GCM notification using curl.
 * 
 * @param $apiKey		[string] The Browser API key string for your GCM account
 * @param $registrationIdsArray [array]  An array of registration ids to send this notification to
 * @param $messageData		[array]	 An named array of data to send as the notification payload
 */
function sendNotification( $apiKey, $registrationIdsArray, $messageData )
{   
    $headers = array("Content-Type:" . "application/json", "Authorization:" . "key=" . $apiKey);
    $data = array(
        'data' => $messageData,

        'registration_ids' => $registrationIdsArray
    );
 
    $ch = curl_init();
 
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers ); 
    curl_setopt( $ch, CURLOPT_URL, "https://android.googleapis.com/gcm/send" );
    curl_setopt( $ch, CURLOPT_SSL_VERIFYHOST, 0 );
    curl_setopt( $ch, CURLOPT_SSL_VERIFYPEER, 0 );
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode($data) );
 
    $response = curl_exec($ch);
    curl_close($ch);
 
    return $response;
}


	function sendError($messageError){
	$jsonResponse = array(array('error' => $messageError));
die( json_encode($jsonResponse));
}
?>