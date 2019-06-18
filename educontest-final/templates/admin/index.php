<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/22/16
 * Time: 11:20 AM
 */
require_once "templates/layout/header.php";
?>


<div class="container mt3 admin-page"  id="main" ng-app="edu" ng-controller="SchoolController">
    <div class="mt2 jumbotron">
        <h1>Educontest Admin Panel</h1>
    </div>

    <div class="row">
        <div class="col-xs-3">
            <div class="panel panel-default">
                <div class="panel-heading">Schools</div>
                <div class="panel-body">
                    <div id="panel-items-container">
                        <a href="/admin/school/create/" class="btn btn-primary "><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Create School</a>
                        <br>
                        <a href="/admin/school/current" class="btn btn-success mt1"><span class="glyphicon glyphicon-education" aria-hidden="true"></span> Signed-Up</a>
                        <br>
                        <a href="/admin/school" class="btn btn-danger mt1"><span class="glyphicon glyphicon-bookmark" aria-hidden="true"></span> All Schools</a>
                        <br>
                        <a href="/admin/password" class="btn btn-warning mt1"><span class="glyphicon glyphicon-scissors" aria-hidden="true"></span>Reset Passwords</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-3">
            <div class="panel panel-default">
                <div class="panel-heading">School Year Settings</div>
                <div class="panel-body">
                    <div id="panel-items-container">
                        <a href="/admin/invoices" class="btn btn-primary "><span class="glyphicon glyphicon-usd" aria-hidden="true"></span> Invoices</a>
                        <br>
                        <a href="/admin/divisions" class="btn btn-success mt1"><span class="glyphicon glyphicon-th" aria-hidden="true"></span> Divisions</a>
                        <br>
                        <a href="/admin/settings" class="btn btn-danger mt1"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span> Settings</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-3">
            <div class="panel panel-default">
                <div class="panel-heading">Email</div>
                <div class="panel-body">
                    <div id="panel-items-container">
                        <a href="#" class="btn btn-primary "><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span> Send a Message</a>
                        <br>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xs-3">
            <div class="panel panel-default">
                <div class="panel-heading">Documents</div>
                <div class="panel-body">
                    <div id="panel-items-container">
                        <a href="#" class="btn btn-primary "><span class="glyphicon glyphicon-apple" aria-hidden="true"></span> Tests</a>
                        <br>
                        <a  href="/files/general/educontest_info.pdf" target="_blank" class="btn btn-success mt1"><span class="glyphicon glyphicon-paperclip" aria-hidden="true"></span> ICI Sheet</a>
                        <br>
                        <a href="/files/general/score_release_form.pdf" target="_blank" class="btn btn-danger mt1"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span> Release Form</a>
                        <br>
                        <a  href="/files/general/answer_key.pdf" target="_blank" class="btn btn-warning mt1"><span class="glyphicon glyphicon-erase" aria-hidden="true"></span> Answer Key</a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>