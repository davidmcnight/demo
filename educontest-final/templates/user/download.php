<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/6/17
 * Time: 11:36 AM
 */

require_once "templates/layout/header.php";
?>

<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getSchoolMin(<?php echo $_SESSION['school_id'] ?>)">
    <div class="jumbotron" ng-init="getActiveSchoolYear(<?php echo $_SESSION['school_id'] ?>)">
        <h2>{{school.name}}'s Classes for {{activeYear.description}} </h2>
    </div>
    <div  class="col-xs-12 col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                Competitions registered for in {{activeYear.description}}
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Grade</th>
                    <th>Test 1</th>
                    <th>Test 2</th>
                    <th>Test 3</th>
                    <th>Test 4 </th>
                </tr>
                <tr ng-repeat="c in schoolYear.school_competitions">
                    <td>{{c.competition.grade.name}}</td>
                    <td>
                        <button  class="btn btn-primary" target="_blank" ng-click="downloadTest(c.competition.test_1_url)"
                            ng-disabled="schoolYear.year.openTest < 1">
                            <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                            Test 1
                        </button>
                    </td>
                    <td>
                        <button  class="btn btn-primary" target="_blank" ng-click="downloadTest(c.competition.test_2_url)"
                                 ng-disabled="schoolYear.year.openTest < 2">
                            <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                            Test 2
                        </button>
                    </td>
                    <td>
                        <button  class="btn btn-primary" target="_blank" ng-click="downloadTest(c.competition.test_3_url)"
                                 ng-disabled="schoolYear.year.openTest < 3">
                            <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                            Test 3
                        </button>
                    </td>
                    <td>
                        <button  class="btn btn-primary" target="_blank" ng-click="downloadTest(c.competition.test_4_url)"
                                 ng-disabled="schoolYear.year.openTest < 4">
                            <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                            Test 4
                        </button>
                    </td>
                </tr>
            </table>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                Practice Tests registered for in {{activeYear.description}}
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Grade</th>
                    <th>Practice Tests</th>
                </tr>
                <tr ng-repeat="t in schoolYear.school_tests">
                    <td>{{t.test.grade.name}}</td>
                    <td>
                        <button  class="btn btn-primary" target="_blank" ng-click="downloadTest(t.test.url)"">
                            <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                            Test 1
                        </button>
                    </td>
                </tr>
            </table>
        </div>


    </div>


</div>

