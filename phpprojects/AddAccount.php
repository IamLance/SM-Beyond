<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require "NACDB.php";

	

$sql = "INSERT INTO User_Accounts (fname,lname,username,password,usertype)
VALUES ('$_POST[fname]','$_POST[lname]','$_POST[uname]','$_POST[passwd]','$_POST[role]')";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
