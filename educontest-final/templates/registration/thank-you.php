<?php
/**
 * Created by PhpStorm.
 * User: Davey
 * Date: 1/21/17
 * Time: 3:31 PM
 */
require_once "templates/layout/header.php";
?>


<div style="margin: 0 auto;" class="container row  pt2 mt3" id="main"
     ng-app="edu" ng-controller="registrationController">
    <div class="mb1 jumbotron">
        <h1 style="text-align: center;">Thank You for Registering!</h1>
    </div>
    <div style="text-align: center;">
        <h3>You will need to use your school code and password to login.</h3>
        <p>Your school code is: <span class="bold"> <?php echo $_SESSION['school_id'];?></span></p>
        <p>Your school password is: <span class="bold"> <?php echo $_SESSION['password'];?></span></p>
    </div>
    <div style="margin: 20px 0; text-align: center;">
        <a class="btn btn-success " href="/users/dashboard/<?php echo $_SESSION['school_id'];?>">Click Here to Sign-Up for Classes</a>
    </div>
</div>
