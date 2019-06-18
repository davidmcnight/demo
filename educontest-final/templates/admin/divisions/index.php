<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 1/28/17
 * Time: 11:40 AM
 */


require_once "templates/layout/header.php";
?>

<div class="container mt3" id="main" ng-app="edu" ng-controller="SchoolController" ng-init="getSchoolsForDivision(1)">
    <div class="col-xs-12 row mt2 mb2">
        <div class="jumbotron" ng-init="getActiveYear()">
            <h3>Divisions for {{activeYear.description}}</h3>
        </div>


        <div class="row mb1 mt1">
            <div class="col-md-2">
                <label for="search">Search:</label>
                <input ng-model="filter" id="search" class="form-control" placeholder="Filter text">
            </div>
            <div class="col-md-2">
                <label for="search">items per page:</label>
                <input type="number" min="1" max="99" class="form-control width-33" ng-model="pageSize">
            </div>
            <div class="col-md-3 mt1" style="margin-top: 25px">
                <button class="btn btn-danger" ng-click="getSchoolsForDivision(1)">
                    Get Catholic Math League Schools
                </button>
            </div>
            <div class="col-md-3 mt1" style="margin-top: 25px">
                <button class="btn btn-success" ng-click="getSchoolsForDivision(2)">
                    Get Educontest Schools
                </button>
            </div>
        </div>

    </div>
    <table class="table table-striped">
        <tr >
            <th>Grade</th>
            <th>State</th>
            <th>School Name</th>
            <th>School Id</th>
            <th>Division</th>
            <th>League</th>
            <th>Actions</th>
        </tr>
        <tr dir-paginate="sc in schoolCompetitionsDivisions | filter:filter | itemsPerPage: pageSize" current-page="currentPage"" >
<!--            <td>{{sc.id}}</td>-->
            <td>{{sc.grade}}</td>
            <td>
                {{sc.state}}
            </td>
            <td>{{sc.school}}</td>
            <td>{{sc.school_id}}</td>
            <td>
                <input type="text" ng-model="sc.division" class="form-control" style="width: 5em">
            </td>
            <td>{{sc.league}}</td>
            <td><button class="btn btn-primary" ng-click="updateDivision(sc)">Save</button></td>
        </tr>
    </table>
    <div ng-controller="OtherController" class="other-controller">
        <div class="text-center">
            <dir-pagination-controls boundary-links="true" on-page-change="pageChangeHandler(newPageNumber)" template-url="../../dirPagination.tpl.html"></dir-pagination-controls>
        </div>
    </div>
</div>