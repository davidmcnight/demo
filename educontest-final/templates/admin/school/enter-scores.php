<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/16/16
 * Time: 8:44 AM
 */
require_once "templates/layout/header.php";
?>



<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init=" getSchoolCompetition(<?php echo $schoolCompetitionId; ?>);">
    <div class="jumbotron">
        <h2 ng-init=""> Admin:
            {{schoolCompetition.school_year.school.name}}'s {{schoolCompetition.competition.grade.name}} for
            {{schoolCompetition.school_year.year.description}}
        </h2>
    </div>


    <div class="col-xs-12" ng-hide="schoolCompetition.school_year.year.open == 0" ng-init="student.school_competition_id = schoolCompetition.id">
        <div class="col-xs-12">
            <h3>Add Student</h3>
        </div>
        <form name="createStudentForm">
            <div class="col-xs-2">
                <label class="control-label">Student Name</label>
                <input ng-model="student.name" name="student_name"
                       type="text" class="form-control" placeholder="Jane Doe" required >
            </div>
            <div class="col-xs-2"  ng-init="student.public=0">
                <label class="control-label">Name Public?</label><br>
                <label class="radio-inline"><input ng-model="student.public" type="radio" name="public" value="0">No</label>
                <label class="radio-inline"><input ng-model="student.public" type="radio" name="public" value="1">Yes</label>

            </div>
            <div class="col-xs-2">
                <label class="control-label">Test Score 1</label>
                <input  min="0" max="25" name = "test1_score" ng-disabled = "schoolCompetition.school_year.year.openTest < 1" ng-model="student.test1_score"
                        type="number"  class="form-control" placeholder="20" >

            </div>
            <div class="col-xs-2">
                <label class="control-label">Test Score 2</label>
                <input min="0" max="25" name = "test2_score" ng-disabled="schoolCompetition.school_year.year.openTest < 2" ng-model="student.test2_score"
                       type="number"  class="form-control" placeholder="20" >
            </div>
            <div class="col-xs-2">
                <label class="control-label">Test Score 3</label>
                <input min="0" max="25" name = "test3_score" ng-disabled="schoolCompetition.school_year.year.openTest < 3"  ng-model="student.test3_score"
                       type="number" class="form-control" placeholder="20" >
            </div>
            <div class="col-xs-2">
                <label class="control-label ">Test Score 4</label>
                <input min="0" max="25" name = "test4_score" ng-disabled="schoolCompetition.school_year.year.openTest < 4" ng-model="student.test4_score"
                       type="number" class="form-control" placeholder="20" >
            </div>
            <div ng-show="!createStudentForm.test1_score.$valid || !createStudentForm.test2_score.$valid || !createStudentForm.test3_score.$valid || !createStudentForm.test4_score.$valid"
                 class="col-xs-12 alert-danger mt1" >
                <h4>  <strong>Score Invalid.</strong> The students score cannot exceed 25.</h4>
            </div>
            <div class=" col-xs-12 control-container mt1">
                <button
                    ng-disabled="createStudentForm.$invalid"
                    class="btn btn-primary"
                    ng-click="createStudent(student, <?php echo $schoolCompetitionId;?>)">
                    Add Student
                </button>
            </div>
        </form>
    </div>


    <div class="col-xs-12 mt1">
        <div ng-show="studentDeletedAlert" class="alert alert-success">
            <strong>Success!</strong> You have deleted a student score.
        </div>
    </div>

    <div class="col-xs-12 mt1">
        <div ng-show="studentCreatedAlert" class="alert alert-success">
            <strong>Success!</strong> You have added a student score.
        </div>
    </div>
    <!--    !!student.public &&-->

    <div class="col-xs-12 mt2" ng-hide="schoolCompetition.school_year.year.open == 0">
        <table class="table table-striped">
            <tr>
                <th>Student Name</th>
                <th>Name Public?</th>
                <th>Test 1</th>
                <th>Test 2</th>
                <th>Test 3</th>
                <th>Test 4</th>
                <th>Total</th>
                <th>Actions</th>
            </tr>

            <tr ng-repeat="s in schoolCompetition.students">
                <form name="updateForm-{{s.id}}">
                    <td class="col-xs-3"><input ng-model="s.name" type="text" class="form-control"></td>
                    <!--    Is public            -->

                    <td ng-init="s.public" class="col-xs-2">
                        <label class="radio-inline"><input ng-model="s.public" type="radio" name="public{{s.id}}" value="0">No</label>
                        <label class="radio-inline"><input ng-model="s.public" type="radio" name="public{{s.id}}" value="1">Yes</label>
                    </td>
                    <td class="col-xs-1"><input ng-disabled="schoolCompetition.school_year.year.openTest < 1" ng-model="s.test1_score" type="number" class="form-control"></td>
                    <td class="col-xs-1"><input ng-disabled="schoolCompetition.school_year.year.openTest < 2" ng-model="s.test2_score" type="number" class="form-control"></td>
                    <td class="col-xs-1"><input ng-disabled="schoolCompetition.school_year.year.openTest < 3" ng-model="s.test3_score" type="number" class="form-control"></td>
                    <td class="col-xs-1"><input ng-disabled="schoolCompetition.school_year.year.openTest < 4" ng-model="s.test4_score" type="number" class="form-control"></td>
                    <td class="col-xs-1">{{s.test1_score + s.test2_score + s.test3_score + s.test4_score}}</td>
                    <td class="col-xs-2">
                        <button class="btn btn-success" ng-click="updateStudent(s)"
                                ng-disabled="s.test1_score > 25 || s.test2_score > 25 || s.test3_score > 25 || s.test4_score > 25">
                            Save
                        </button>
                        <button class="btn btn-danger" ng-click="deleteStudent(s)">Delete</button>
                    </td>
                </form>
            </tr>

        </table>
    </div>
</div>