<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/18/16
 * Time: 9:25 AM
 */
require_once "templates/layout/header.php";
?>


<div class="container pt2 mt3 pb3" id="main" ng-app="edu" ng-controller="SchoolController">
    <div class="jumbotron" ng-init="getActiveYear()">
        <h2>Admin Settings</h2>
    </div>
    <div class="col-md-2 mb2">
        <div class="col-xs-12" ng-init="getAllYears()">
            <label class="control-label">Set Active Year</label>
            <select class="form-control" ng-model="activeYear.id"
                    ng-options="year.id as year.description for year in years">
            </select>
        </div><br>
        <div class="col-xs-12">
            <button class="btn btn-success mt2" ng-click="updateActiveYear(activeYear.id)">Update Year</button>
        </div>
    </div>
    <div class="col-md-2 mb2">
        <div class="col-xs-12">
            <label class="control-label">Set Open Test</label>
            <select class="form-control" ng-model="activeYear.openTest" convert-to-number>
                <option value="0">0</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
            </select>
        </div>
        <div class="col-xs-12">
            <button class="btn btn-success mt2" ng-click="updateOpenTest(activeYear.openTest)">Update Open Test</button>
        </div>
    </div>

    <div class="col-md-6 mb2">
        <div class="col-xs-3">
            <label class="control-label">Set Date 1</label>
            <input ng-model="activeYear.date_test_1" type="text" class="form-control ">
        </div>
        <div class="col-xs-3">
            <label class="control-label">Set Date 2</label>
            <input ng-model="activeYear.date_test_2" type="text" class="form-control ">
        </div>
        <div class="col-xs-3">
            <label class="control-label">Set Date 3</label>
            <input ng-model="activeYear.date_test_3" type="text" class="form-control ">
        </div>
        <div class="col-xs-3">
            <label class="control-label">Set Date 4</label>
            <input ng-model="activeYear.date_test_4" type="text" class="form-control ">
        </div>

    </div>
    <div class="col-xs-2">
        <button class="btn btn-success mt1" style="margin-top: 22px" ng-click="updateYearDates(activeYear.date_test_1,
         activeYear.date_test_2,
         activeYear.date_test_3,
         activeYear.date_test_4)">Update Test Years</button>
    </div>


</div>
