<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/3/16
 * Time: 1:04 PM
 */

require_once "templates/layout/header.php";

?>

<div class="container mt3 pr2 pl2" id="main" ng-app="edu" ng-controller="SchoolController"
     xmlns="http://www.w3.org/1999/html">
    <div class="row p1">
        <h2 style="margin-top: 20px;" class="page-title">
           Filter Your Test Results
        </h2>
    </div>
    <div class="row ">
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="col-md-2 mb1">
                    <label class="control-label">School League</label>
                    <select class="form-control" ng-model="result_filter.league_id" ng-init="result_filter.league_id = '0'">
                        <option value="0" >All</option>
                        <option value="1" >Catholic Math League</option>
                        <option value="2" >Educontest</option>
                    </select>
                </div>
                <div class="col-md-2  mb1">
                    <label class="control-label">Grade</label>
                    <select class="form-control" ng-model="result_filter.grade_id" ng-init="result_filter.grade_id = '1'">
                        <option value="1" >Math 3</option>
                        <option value="2" >Math 4</option>
                        <option value="3" >Math 5</option>
                        <option value="4" >Math 6</option>
                        <option value="5">Math 7</option>
                        <option value="6" >Pre-Algebra</option>
                        <option value="7" >Algebra 1</option>
                        <option value="8" >Algebra 2</option>
                        <option value="9">Geometry</option>
                        <option value="10" >Advanced Math</option>
                    </select>
                </div>
                <div class="col-md-2 mb1">
                <label class="control-label">State</label>
                <select  class="form-control" name="state" ng-model="result_filter.state" ng-init="result_filter.state = '0'">
                    <option value="0">ALL</option>
                    <option value="AL">AL</option>
                    <option value="AK">AK</option>
                    <option value="AZ">AZ</option>
                    <option value="AR">AR</option>
                    <option value="CA">CA</option>
                    <option value="CO">CO</option>
                    <option value="CT">CT</option>
                    <option value="DE">DE</option>
                    <option value="DC">DC</option>
                    <option value="FL">FL</option>
                    <option value="GA">GA</option>
                    <option value="HI">HI</option>
                    <option value="ID">ID</option>
                    <option value="IL">IL</option>
                    <option value="IN">IN</option>
                    <option value="IA">IA</option>
                    <option value="KS">KS</option>
                    <option value="KY">KY</option>
                    <option value="LA">LA</option>
                    <option value="ME">ME</option>
                    <option value="MD">MD</option>
                    <option value="MA">MA</option>
                    <option value="MI">MI</option>
                    <option value="MN">MN</option>
                    <option value="MS">MS</option>
                    <option value="MO">MO</option>
                    <option value="MT">MT</option>
                    <option value="NE">NE</option>
                    <option value="NV">NV</option>
                    <option value="NH">NH</option>
                    <option value="NJ">NJ</option>
                    <option value="NM">NM</option>
                    <option value="NY">NY</option>
                    <option value="NC">NC</option>
                    <option value="ND">ND</option>
                    <option value="OH">OH</option>
                    <option value="OK">OK</option>
                    <option value="OR">OR</option>
                    <option value="PA">PA</option>
                    <option value="RI">RI</option>
                    <option value="SC">SC</option>
                    <option value="SD">SD</option>
                    <option value="TN">TN</option>
                    <option value="TX">TX</option>
                    <option value="UT">UT</option>
                    <option value="VT">VT</option>
                    <option value="VA">VA</option>
                    <option value="WA">WA</option>
                    <option value="WV">WV</option>
                    <option value="WI">WI</option>
                    <option value="WY">WY</option>
                </select>
                </div>
                <div class="col-md-3 mb1" ng-init="result_filter.results_type = 'school'">
                    <label class="control-label">Show Results</label>
                    <label style="font-size: 16px;" class="radio-inline">
                        <input ng-model="result_filter.results_type" type="radio" name="results_type-" value="school">
                        Show School Scores
                    </label>
                    <label style="font-size: 16px; margin-left: 0px;" class="radio-inline" style="margin-left: 0">
                        <input ng-model="result_filter.results_type" type="radio" name="results_type" value="student">
                        Show Student Scores
                    </label>
                </div>

                <div class="col-md-3 mt1" ng-show="result_filter.results_type=='school'">
                    <button class="btn btn-primary" ng-click="getSchoolTestResults(result_filter)">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                        Search School Results
                    </button>
                </div>
                <div class="col-md-3 mt1" ng-show="result_filter.results_type=='student'">
                    <button class="btn btn-primary" ng-click="getStudentTestResults(result_filter)">
                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                        Search Student Results
                    </button>
                </div>
            </div>

        </div>
    </div>
    <div class="row mt1" ng-show="showSchoolResults">
        <div class="col-xs-12">
            <table class="table table-striped">
                <tr>
                    <th>Rank</th>
                    <th>School</th>
                    <th>City, State</th>
                    <th>Test 1</th>
                    <th>Test 2</th>
                    <th>Test 3</th>
                    <th>Test 4</th>
                    <th>Total</th>
                </tr>
                <!--                <tr ng-repeat="r in studentTestResults">-->
                <tr dir-paginate="r in schoolTestResults | itemsPerPage: school_results_page_size" current-page="school_results_current_page">

                    <td>{{r.score_rank}}</td>
                    <td>{{r.school_name}}</td>
                    <td>{{r.city_state}}</td>
                    <td>{{r.score_1}}</td>
                    <td>{{r.score_2}}</td>
                    <td>{{r.score_3}}</td>
                    <td>{{r.score_4}}</td>
                    <td>{{r.total}}</td>
                </tr>
            </table>
            <div ng-controller="OtherController" class="other-controller">
                <div class="text-center">
                    <dir-pagination-controls boundary-links="true" on-page-change="pageChangeHandler(newPageNumber)" template-url="../../../dirPagination.tpl.html"></dir-pagination-controls>
                </div>
            </div>
        </div>
    </div>







    <div class="row mt1" ng-show="showStudentResults">
        <div class="col-xs-12">
            <table class="table table-striped">
                <tr>
                    <th>Rank</th>
                    <th>Name</th>
                    <th>School</th>
                    <th>City, State</th>
                    <th>Test 1</th>
                    <th>Test 2</th>
                    <th>Test 3</th>
                    <th>Test 4</th>
                    <th>Total</th>
                </tr>
<!--                <tr ng-repeat="r in studentTestResults">-->
            <tr dir-paginate="r in studentTestResults | itemsPerPage: student_results_page_size" current-page="student_results_current_page">
                <td>{{r.rank}}</td>
<!--                <td ng-show="r.public == 1">{{r.name}}</td>-->
<!--                <td ng-show="r.public == 0">Student #{{r.id}}</td>-->
                <td>{{r.name}}</td>
                <td ng-show="r.school_public == 1">{{r.school_name}}</td>
                <td ng-show="r.school_public == 0">School #{{r.school}}</td>
                <td>{{r.city}}, {{r.state}}</td>
                <td>{{r.test1_score}}</td>
                <td>{{r.test2_score}}</td>
                <td>{{r.test3_score}}</td>
                <td>{{r.test4_score}}</td>
                <td>{{r.total_score}}</td>
            </tr>
            </table>
            <div ng-controller="OtherController" class="other-controller">
                <div class="text-center">
                    <dir-pagination-controls boundary-links="true" on-page-change="pageChangeHandler(newPageNumber)" template-url="../../../dirPagination.tpl.html"></dir-pagination-controls>
                </div>
            </div>
        </div>
    </div>

</div>
