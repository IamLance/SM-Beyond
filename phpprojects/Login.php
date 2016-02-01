<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
require "NACDB.php";


set_include_path('./Bean');
spl_autoload_register();
require 'User.php';


$file_handle = fopen("MDMConfig.txt", "r");
while (!feof($file_handle)) {
    $line = fgets($file_handle);
    $ip = substr($line, 3);
    echo $ip;
}
fclose($file_handle);

$User = new User();

$User->setUsername($_POST["uname"]);
$User->setPassword($_POST["pass"]);
$User->setUsertoken(uniqid());
$User->setIpAddress($_POST["ipAddress"]);
$User->setMacAddress($_POST["macAddress"]);
$sql = "select * from User_Accounts where username= '" . $User->getUsername() . "' and password = '" . $User->getPassword() . "'";




$result = $conn->query($sql);
if ($result->num_rows > 0) {
    echo "Access-Granted ";
    echo "userToken=";
    echo $User->getUsertoken();
    echo "!endOfToken ";
//insert to userlogs
    $row = $result->fetch_row();
    $User->setUserid($row[0]);
    $User->setUsertype($row[5]);
    $sql = "INSERT INTO UserLogs (userid,ipAddress,macAddress,userToken,lastLogin) VALUES('" . $User->getUserid() . "','" . $User->getIpAddress() . "','" . $User->getMacAddress() . "','" . $User->getUsertoken() . "',NOW())";
    if ($conn->query($sql) === TRUE) {
        echo "New record created successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
//send to mdm
    $fields_string = '';
    $url = "http://{$ip}/phpprojects/MDMStore.php";

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
} else {
    echo "Access-Denied";
}

$conn->close();



