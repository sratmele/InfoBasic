<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $notice = $_POST['notice'];
 $postedby = $_POST['postedby'];
 $dateandtime = $_POST['dateandtime'];
 
 require_once('config.php');
 
 $sql = "INSERT INTO labels (notice,postedby,dateandtime)
		VALUES ('$notice', '$postedby', '$dateandtime')";
 
 $result = mysqli_query($conn,$sql);
  
 if($result){
 echo 'success';
 }else{
 echo 'failure';
 }
 }
?>