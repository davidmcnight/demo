<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 1/18/17
 * Time: 2:46 PM
 */

require_once "templates/layout/header.php";

?>


<div class="container pt2 mt3" id="main"
     ng-app="edu" ng-controller="registrationController">
    
    <div class="col-sm-6 mb1" style="padding: 0 50px; border-right: 1px solid #cccccc;">
        <h1 class="page-title">Previous Participant?</h1>
        <p style="text-align: center;">If you have previously participated in the contest, please enter you school code and password below and press "Sign-In"</p>
    	<form method="post" action="/verify" name="user_reg_form" class="p1 form_id_001">
            <label class="control-label">School Code</label>
            <input ng-model="sign_in.school_id" name="username"
                   type="text" class="form-control" placeholder="2328" >
            <label class="control-label">Password</label>
            <input ng-model="sign_in.password" name="password"
                   type="password" class="form-control" placeholder="myschool@school.com" >
            <input type="submit"  value=" Sign In" class="btn btn-primary mt1" ng-disabled="!(
                            !!sign_in.password &&
                            !!sign_in.school_id
                        )">

        </form>
    </div>    
    <div class="col-sm-6 mb1" style="padding: 0 50px;">
        <h1 class="page-title" style="margin-top: 7rem;">Never Participated Before?</h1>
        <p style="text-align: center;">Are you ready to get involved in an exciting academic challenge?</p>
        <p style="text-align: center;"><a style="margin-top: 15px;" class="btn btn-primary" href="/registration">Click here to get started.</a></p>
    </div>

    
</div>
