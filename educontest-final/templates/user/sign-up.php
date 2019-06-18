<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/6/17
 * Time: 10:17 AM
 */
require_once "templates/layout/header.php";
?>

<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getSchool(<?php echo $school_id; ?>); getActiveYear(); isSchoolSignedUp(<?php echo $school_id; ?>); ">
    <div class="col-xs-12 col-md-12 row">
        <h1 style="text-align: center;">Classes for {{school.name}}</h1>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div ng-show="classRegSuccess" class="alert alert-success " role="alert">
                <p>Class successfully registered. </p>
            </div>
        </div>
        <div class="col-xs-12">
            <div ng-show="classTestSuccess" class="alert alert-success" role="alert">
                <p>Practice test successfully registered. </p>
            </div>
        </div>
    </div>
    <div class="col-xs-6">
        <h3>Sign Up for Classes for {{activeYear.description}}</h3>
    </div>
    <div class="col-xs-6">
        <h3>Sign Up for Practice Tests for {{activeYear.description}}</h3>
    </div>
    <div class="row"  ng="">
        <div  class="col-xs-12 col-md-3" ">
        <div class="panel panel-default">
            <div class="panel-heading">
                Not Registered
            </div>
            <table class="table table-striped">
                <tr ng-repeat="c in notSignedUpCompetition">
                    <td>{{c.grade.name}}</td>
                    <td><button class="btn btn-primary"
                                ng-click="createSchoolCompetition(c)">Add
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div  class="col-xs-12 col-md-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                Registered
            </div>
            <table class="table table-striped">
                <tr ng-repeat="c2 in signedUpCompetition">
                    <td>{{c2.competition.grade.name}}</td>
                    <td><button class="btn btn-danger"
                                ng-click="deleteSchoolCompetition(c2)">Delete
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div class="row">
        <div  class="col-xs-12 col-md-3" ">
        <div class="panel panel-default">
            <div class="panel-heading">
                Not Registered
            </div>
            <table class="table table-striped">
                <tr ng-repeat="t in notSignedUpTest">
                    <td>{{t.grade.name}}</td>
                    <td><button class="btn btn-primary"
                                ng-click="createSchoolTest(t)">Add
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div  class="col-xs-12 col-md-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                Registered
            </div>
            <table class="table table-striped">
                <tr ng-repeat="t2 in signedUpTest">
                    <td>{{t2.test.grade.name}}</td>
                    <td><button class="btn btn-danger"
                                ng-click="deleteSchoolTest(t2)">Delete
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
    
    
