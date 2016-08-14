<?php 

	if($_SERVER['REQUEST_METHOD']=='GET'){
				
		require_once("config.php");
		
		$sql = "SELECT COUNT(*) AS total FROM labels";
		
		$r = mysqli_query($conn,$sql);
		
		$res = mysqli_fetch_array($r);

		$result = array();

		array_push($result,array("total"=>$res['total']));
				
		echo json_encode(array("result"=>$result));
		
		mysqli_close($conn);
		
	}
?>