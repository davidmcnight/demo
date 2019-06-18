<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/3/16
 * Time: 10:54 AM
 */
require_once "templates/layout/header.php"

?>

<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="SchoolController">
    <div class="row p1">
        <h2 class="page-title">Test Samples</h2>
        <p style="text-align: center;">Click the links below to view a sample of each test:</p>
        <div ng-repeat="(key, value) in sample_pdf" class="col-xs-3">
            <a href="/public/assets/pdf/samples/{{value}}" target="_blank">{{key}}</a>
        </div>
    </div>
</div>