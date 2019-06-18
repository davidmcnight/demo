<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 4/3/17
 * Time: 3:51 PM
 */
require_once "templates/layout/header.php";
?>
<div class="container col-xs-12 row  pt2 mt3 pb2" id="main"
     ng-app="edu" ng-controller="registrationController">
    <div class="col-xs-12">
        <h1>The School Code is: <?php echo $_GET['school_id'];?> </h1>
<h1>The school password is: <span class="bold"> <?php echo $_GET['school_password'];?></span></h1>
</div>
<div class="col-xs-12">
    <a class="btn btn-success " href="/admin/school/dashboard/<?php echo $_GET['school_id'];?>">Click Here to Sign-Up for Classes</a>
</div>
</div>
