<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <meta charset="UTF-8">
        <title>SM BEYOND</title>
    </head>
    <body>
        <form method="POST" action="AddAccount.php">
            First name:<br>
            <input type="text" name="fname">
            <br>
            Last name:<br>
            <input type="text" name="lname">
            <br>
            Username:<br>
            <input type="text" name="uname">
            <br>
            Password:<br>
            <input type="password" name="passwd">
            <br>
            Re Enter Password:<br>
            <input type="password" name="password">
            <br>
            Role:<br>
            <input type="text" name="role">
            <br>   
            <input type="submit" value="Submit">
            
        </form>
        <a href="ManageMDMPolicies.php">Manage Mobile Policies</a>
        
        
    </body>
</html>
