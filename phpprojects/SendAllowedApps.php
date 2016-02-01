<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


require "MDMDB.php";
$sql = "select userType from mobiledeviceaccounts where userToken='" . $_GET["userToken"] . "'";
//$sql = "select userType from mobiledeviceaccounts where userToken='567ffeb1adbf3'";

$result = mysqli_query($conn, $sql);
if (mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_row($result);
    $filename = "AllowedApps.txt";
    $concat = "{$row[0]}{$filename}";
    $file_handle = fopen($concat, "r");
    $response = array();
    $response["AppList"] = array();
    while (!feof($file_handle)) {
        $line = rtrim(fgets($file_handle));
        array_push($response["AppList"], $line);
    }
    $response["success"] = 1;

    echo json_encode($response);
    fclose($file_handle);
}





