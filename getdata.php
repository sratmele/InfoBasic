<?php 

	if($_SERVER['REQUEST_METHOD']=='GET'){
		
		$val  = $_GET['val'];
		
		require_once("config.php");
		
		$sql = "SELECT * FROM labels WHERE (id < $val) 
			ORDER BY id DESC 
			LIMIT 10";
		
		$r = mysqli_query($conn,$sql);

		$result = array();
		
		while($res = mysqli_fetch_array($r)) {
		
			array_push($result,array(
				"notice"=>$res['notice'],
				"postedby"=>$res['postedby'],
				"dateandtime"=>$res['dateandtime']));
			}

		echo json_encode(array("result"=>$result));
		
		mysqli_close($conn);
		
	}
?>