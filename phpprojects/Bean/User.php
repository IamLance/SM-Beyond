<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of User
 *
 * @author Lance
 */
class User {
  private $username;
  private $password;
  private $ipAddress;
  private $macAddress;
  private $usertype;
  private $usertoken;
  private $userid;
  private $firstname;
  private $lastname;
  
  
  
  function getUsername() {
      return $this->username;
  }

  function getPassword() {
      return $this->password;
  }

  function getIpAddress() {
      return $this->ipAddress;
  }

  function getMacAddress() {
      return $this->macAddress;
  }

  function getUsertype() {
      return $this->usertype;
  }

  function getUsertoken() {
      return $this->usertoken;
  }

  function getUserid() {
      return $this->userid;
  }

  function getFirstname() {
      return $this->firstname;
  }

  function getLastname() {
      return $this->lastname;
  }

  function setUsername($username) {
      $this->username = $username;
  }

  function setPassword($password) {
      $this->password = $password;
  }

  function setIpAddress($ipAddress) {
      $this->ipAddress = $ipAddress;
  }

  function setMacAddress($macAddress) {
      $this->macAddress = $macAddress;
  }

  function setUsertype($usertype) {
      $this->usertype = $usertype;
  }

  function setUsertoken($usertoken) {
      $this->usertoken = $usertoken;
  }

  function setUserid($userid) {
      $this->userid = $userid;
  }

  function setFirstname($firstname) {
      $this->firstname = $firstname;
  }

  function setLastname($lastname) {
      $this->lastname = $lastname;
  }

    

    //put your code here
}
