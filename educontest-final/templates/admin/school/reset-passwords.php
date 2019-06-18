<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/30/16
 * Time: 3:54 PM
 */

require_once "templates/layout/header.php"

?>

<div class="container" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getAllSchoolsPw()">
    <div class="col-xs-12 row mt2 mb2">
        <h1>Schools</h1>
    </div>
    <div class="col-md-12 row mb2">
        <div class="pull-left">
            <label for="search">Search:</label>
            <input ng-model="filter" id="search" class="form-control" placeholder="Filter text">
        </div>

        <div class="col-md-2">
            <label for="search">items per page:</label>
            <input type="number" min="1" max="99" class="form-control width-33" ng-model="pageSize">
        </div>
        <div class="col-md-2">
            <h4>Page: {{ currentPage }}</h4>
        </div>
        <div class="pull-right">
            <a class="btn btn-primary pull-right mt1" href="/admin/school/create">Add School</a>
        </div>

    </div>
    <div class="col-xs-12 row">
        <div class="panel panel-default">
            <div class="panel-heading">
                Schools
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Contact</th>
                    <th>Password</th>
                    <th>Actions</th>
                </tr>
                <tr dir-paginate="school in schools | filter:filter : false| itemsPerPage: pageSize" current-page="currentPage">
                    <td class="bold">{{school.id}}</td>
                    <td><a href="/admin/school/{{school.id}}">{{school.name}}</td>
                    <td><a href="/admin/school/{{school.id}}">{{school.contact}}</td>
                    <td class="bold">{{school.password}}</td>

                    <td>
                        <button class="btn btn-info" ng-click="updatePassword(school.id)">
                            Generate New Password
                            <span class="glyphicon glyphicon-menu-cog" aria-hidden="true"></span>
                    </td>
                </tr>
            </table>
            <div ng-controller="OtherController" class="other-controller">
                <div class="text-center">
                    <dir-pagination-controls boundary-links="true" on-page-change="pageChangeHandler(newPageNumber)"
                                             template-url="../dirPagination.tpl.html"></dir-pagination-controls>
                </div>
            </div>
        </div>
    </div>
</div>
