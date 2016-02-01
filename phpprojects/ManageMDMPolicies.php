<?php
require 'MDMDB.php';

$sql = "select * from policylist";
$result = $conn->query($sql);
?>



<html>
    <head>
        <meta charset="UTF-8">
        <title>SM BEYOND</title>
    </head>
    <body><form action = "editPolicies" method = "post">
            <?php
            while ($row = $result->fetch_row()) {
                ?>

                <table border="1" name="policytable <?php echo row[0] ?>">
                    <tr>
                        <?php
                        echo $row[1];
                        $sql = "select * from mobilepolicy where policyId = '" . $row[0] . "'";
                        $policylist = $conn->query($sql);
                        $status = "Enabled";
                        while ($rowPolicylist = $policylist->fetch_row()) {
                            if ($rowPolicylist[2] == 1) {
                                $status = "Enabled";
                            } else {
                                $status = "Disabled";
                            }
                            ?>

                            <td con> User Type: <?php echo $rowPolicylist[1] ?></div></td>
                            <td > Status: <div contenteditable="true"><?php echo $status ?></div></td>
                            <?php
                            if ($rowPolicylist[3] === NULL) {
                                $rowPolicylist[3] = "Not Applicable";
                                ?>
                                <td>Value: <?php echo $rowPolicylist[3] ?></div></td>
                            <?php } else { ?>
                                <td > Value: <div contenteditable="true"> <?php echo $rowPolicylist[3] ?></div></td>
                            <?php } ?>
                        </tr>
                    <?php } ?>
                </table>

                <?php
            }
            ?>

            <input type="submit" name="edit" value="Edit Policies"/>
        </form>

    </body>
</html>