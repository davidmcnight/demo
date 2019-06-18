<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/12/16
 * Time: 4:35 PM
 */
require_once "templates/layout/header.php"

?>

<div class="container pt2 mt3" id="main"
ng-app="edu" ng-controller="registrationController">
    <div class="row p1 col-xs-12 ">

        <?php
            if(isset($invalid)){
              ?>
        <div class="alert alert-danger">
            <strong>Something went wrong!</strong> The credentials you have given did not work.
        </div><?php
            }?>
		<div  class="sign-in-form">
        <div class="mb1 row">
            <h1>Sign In</h1>
        </div>
        <div class="row">
            <form method="post" action="/verify" name="user_reg_form" class="p1">
                <div class="row mb1">
                    <!-- School Name -->
                    <div class="mb1 row">
                        <label class="control-label">School Code</label>
                        <input ng-model="sign_in.school_id" name="username"
                               type="text" class="form-control" placeholder="2328" >
                    </div>
                </div>
                <div class="row mb1">
                    <!-- School Name -->
                    <div class="mb1 row">
                        <label class="control-label">Password</label>
                        <input ng-model="sign_in.password" name="password"
                               type="password" class="form-control" placeholder="myschool@school.com" >
                    </div>
                </div>
                <div class="control-container row">
                    <input type="submit"  value=" Sign In" class="btn btn-primary" ng-disabled="!(
                                !!sign_in.password &&
                                !!sign_in.school_id
                            )">

            </form>
        </div>
        </div>
    </div>
</div>