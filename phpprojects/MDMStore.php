<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
require "MDMDB.php";

set_include_path("./Bean");
spl_autoload_register();
require 'User.php';
$file_handle = fopen("MonitoringConfig.txt", "r");
while (!feof($file_handle)) {
    $line = fgets($file_handle);
    $ip = substr($line, 3);
    echo $ip;
}
fclose($file_handle);
$User = new User();

$fields_string = '';

$User->setUsername($_POST["uname"]);
$User->setUsertoken($_POST["userToken"]);
$User->setIpAddress($_POST["ipAddress"]);
$User->setMacAddress($_POST["macAddress"]);
$User->setUsertype($_POST["userType"]);
$User->setUserid($_POST["userid"]);


$sql = "INSERT INTO MobileDeviceAccounts (userId,username,usertype,usertoken,ipAddress,macAddress,lastActivityTime)
VALUES ('" . $User->getUserid() . "','" . $User->getUsername() . "','" . $User->getUsertype() . "','" . $User->getUsertoken() . "',
            '" . $User->getIpAddress() . "','" . $User->getMacAddress() . "','NOW()')";


if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$url = "http://{$ip}/phpprojects/MonitoringStore.php";

$fields = array(
    'uname' => urlencode($User->getUsername()),
    'userid' => urlencode($User->getUserid()),
    'userToken' => urlencode($User->getUsertoken()),
    'userType' => urlencode($User->getUsertype()),
    'macAddress' => urlencode($User->getMacAddress()),
    'ipAddress' => urlencode($User->getIpAddress())
);
//url-ify the data for the POST
foreach ($fields as $key => $value) {
    $fields_string .= $key . '=' . $value . '&';
}
rtrim($fields_string, '&');

//open connection
$ch = curl_init();

//set the url, number of POST vars, POST data
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_POST, count($fields));
curl_setopt($ch, CURLOPT_POSTFIELDS, $fields_string);

//execute post
$result = curl_exec($ch);

//close connection
curl_close($ch);



$conn->close();


