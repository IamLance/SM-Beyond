<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
require "MDMDB.php";
$sql = "delete from mobiledeviceaccounts where userToken='" . $_POST["token"] . "' ";
$conn->query($sql);

$file_handle = fopen("MonitoringConfig.txt", "r");
while (!feof($file_handle)) {
    $line = fgets($file_handle);
    $ip = substr($line, 3);
    echo $ip;
}
fclose($file_handle);

$fields_string = '';
$url = "http://{$ip}/phpprojects/MonitoringLogout.php";

$fields = array(
    'token' => urlencode($_POST["token"]),
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

if ($conn->query($sql) === TRUE) {
    echo "Logout Success";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
