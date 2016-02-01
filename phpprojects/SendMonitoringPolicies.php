<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require 'MONITORINGDB.php';

$sql = "select m.*,p.policyname from policylist p,monitoringPolicy m,monitoringdeviceaccounts md where md.userToken='" .$_GET["userToken"]. "' and m.userType=md.userType and p.policyid=m.policyid";
$result = mysqli_query($conn, $sql);
$response = array();
$response["policylist"] = array();
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
    while ($row = mysqli_fetch_row($result)) {
        // temp user array
        $policy = array();
        $policy["Policy_ID"] = $row[0];
        $policy["Policy_Name"] = $row[3];
        $policy["Policy_Status"] = $row[2];
        // push single product into final response array
        array_push($response["policylist"], $policy);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No policylist found";

  
    echo json_encode($response);
}
