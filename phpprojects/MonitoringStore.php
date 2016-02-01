<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require "MONITORINGDB.php";
set_include_path("./Bean");
spl_autoload_register();
require 'User.php';

$User = new User();



$User->setUsername($_POST["uname"]);
$User->setUsertoken($_POST["userToken"]);
$User->setIpAddress($_POST["ipAddress"]);
$User->setMacAddress($_POST["macAddress"]);
$User->setUsertype($_POST["userType"]);
$User->setUserid($_POST["userid"]);

$sql = "INSERT INTO MonitoringDeviceAccounts (userId,username,usertype,usertoken,ipAddress,macAddress)
VALUES ('" . $User->getUserid() . "','" . $User->getUsername() . "','" . $User->getUsertype() . "','" . $User->getUsertoken() . "',
            '" . $User->getIpAddress() . "','" . $User->getMacAddress() . "')";

if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();