<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $username = $_POST['username'];
 $password = $_POST['password'];
 if($username=="Siddhartha " && $password=="9876543210"){
 echo 'success';
 }else{
 echo 'failure';
 }
 }
?>