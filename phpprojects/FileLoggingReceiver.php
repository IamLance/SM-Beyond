<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require "MONITORINGDB.php";


$json = file_get_contents('php://input');
$obj = json_decode($json);
print_r($obj);


for ($idx = 0; $idx < count($obj); $idx++) {
    $attribute = (Array) $obj[$idx];
    $date = date("Y-m-d H:i:s", strtotime($attribute["timestamp"]));
    echo $attribute["path"];
    $sql = "select username,userid from monitoringdeviceaccounts where userToken = '" . $attribute["userToken"] . "'";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
        $row = $result->fetch_row();
        $userid = $row[1];
        $username = $row[0];
        $sql = "INSERT INTO logs (logType,userid,username,macAddress,timestamp)
                            VALUES ('" . $attribute["type"] . "','$userid','$username','" . $attribute["mac"] . "','$date')";
        $result = mysqli_query($conn, $sql);
        $id = mysqli_insert_id($conn);
        $sql = "INSERT INTO FileLogs (idLogs,path,event)
                            VALUES ('$id','" . $attribute["path"] . "','" . $attribute["event"] . "')";
        if ($conn->query($sql) === TRUE) {
            echo "New record created successfully";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    }
}