<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require "MONITORINGDB.php";
$sql = "delete from monitoringdeviceaccounts where userToken='" . $_POST["token"] . "' ";
$conn->query($sql);

