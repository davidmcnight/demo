<?php
require_once "templates/layout/header.php";
?>


<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getSchoolMin(<?php echo $school_id ?>)">
    <div class="jumbotron" ng-init="getActiveSchoolYear(<?php echo $school_id ?>)">
        <h2>Admin: {{school.name}}'s Classes for {{activeYear.description}} </h2>
    </div>

    <div  class="col-xs-12 col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                Competitions registered for in {{activeYear.description}}
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Grade</th>
                    <th>Hide Schools Name</th>
                    <th># Number of Students</th>
                    <th></th>
                </tr>
                <tr ng-repeat="c in schoolYear.school_competitions">
                    <td>{{c.competition.grade.name}}</td>
                    <td>
                        <label class="radio-inline"><input ng-model="c.public" type="radio" name="public-{{c.id}}" value="0">No</label>
                        <label class="radio-inline mr1"><input ng-model="c.public" type="radio" name="public-{{c.id}}" value="1">Yes</label>
                        <button class="btn btn-primary" ng-click="updateSchoolCompetitionPublic(c)">Update</button>
                    </td>
                    <td>{{c.students.length}}</td>
                    <td><a class="btn btn-success" href="/admin/school/scores/{{school.id}}/{{c.competition.id}}/{{c.id}}">Enter Scores</a></td>
                </tr>
            </table>
        </div>
    </div>


</div>



