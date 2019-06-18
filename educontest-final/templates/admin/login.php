<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 4/3/17
 * Time: 9:34 AM
 */
require_once "templates/layout/header.php";
?>

<div class="container pt2 mt3" id="main">
    <div class="row p1 col-xs-12 ">
        <?php
        if(isset($invalid)){
            ?>
            <div class="alert alert-danger">
                <strong>Something went wrong!</strong> The credentials you have given did not work.
            </div><?php
        }?>
<div class="sign-in-form">
        <div class="mb1">
            <h1 class="">Admin Sign In</h1>
        </div>
        <div class="">
            <form method="post" action="/admin-verify" name="user_reg_form" class="p1">
                <div class="mb1">
                    <!-- School Name -->
                    <div class="mb1">
                        <label class="control-label">Username</label>
                        <input name="username"
                               type="text" class="form-control" placeholder="admin" >
                    </div>
                </div>
                <div class="mb1">
                    <!-- School Name -->
                    <div class="mb1">
                        <label class="control-label">Password</label>
                        <input ng-model="sign_in.password" name="password"
                               type="password" class="form-control" placeholder="" >
                    </div>
                </div>
                <div class="control-container">
                    <input type="submit"  value=" Sign In" class="btn btn-primary" ng-disabled="!(
                                !!sign_in.password &&
                                !!sign_in.school_id
                            )">

            </form>
        </div>
    </div>
    </div>
</div>
