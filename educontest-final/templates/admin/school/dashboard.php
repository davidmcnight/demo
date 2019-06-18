<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/17/16
 * Time: 9:48 AM
 */

require_once "templates/layout/header.php";
?>


<div class="container pt2 mt3 pb3" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getSchool(<?php echo $school_id; ?>);isSchoolSignedUp(<?php echo $school_id; ?>); getActiveSchoolYear(<?php echo $school_id; ?>); ">
    <div class="jumbotron">
        <h2>ADMIN: {{school.name}}'s Dashboard</h2>
    </div>

    <!-- NG-IF if they are not sign up   -->
    <?php if(!$is_school_signed_up){ ?>
        <div>
            <div ng-init="" class="col-xs-12 pb2 row">
                <h3>Your are currently not registered for this year.</h3>
                <button class="btn btn-primary " ng-click="createSchoolYearAdmin(<?php echo $school_id; ?>);"
                        ng-disabled="syButtonDisabled">
                    Register for: {{activeYear.description}}
                </button>
            </div>
        </div>
        <!--MESSAGE BOXES-->

    <?php }else{
    ?>
    <div id="classRegistration">

        <div class="col-xs-12">
            <div class="col-xs-12">
                <h2>School Year {{activeYear.description}}</h2>
            </div>
            <div class="col-xs-12 mt1">
                <a class="btn btn-primary mr1"
                   href="/admin/school/sign-up/{{school.id}}">
                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                    Edit Registration

                </a>
                <a class="btn btn-danger"
                   href="/admin/school/classlist/{{school.id}}">
                    <span class="glyphicon glyphicon-apple" aria-hidden="true"></span>
                    Enter Scores
                </a>
            </div>
        </div>
        <?php
        }
        ?>
    </div>


