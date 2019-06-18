<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 12/19/16
 * Time: 3:05 PM
 */


//user pages
$app->get(
    '/users/account/:school_id',
    function ($school_id) use ($app, $log){
        if($log->isUserLoggedIn() && $school_id == $_SESSION["school_id"] && !$log->isAdmin()) {
            $app->render('user/account.php', array('school_id' => $school_id));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);

//dashboard
$app->get(
    '/users/dashboard/:school_id',
    function ($school_id) use ($app, $log){
        if($log->isUserLoggedIn() && $school_id == $_SESSION["school_id"] && !$log->isAdmin()) {
            $service = Factory::createService("Year");
            $params = array("isSchoolSignedUp" => 1, "school_id" => $school_id );
            $is_signed_up = $service->find($params);
            $app->render('user/dashboard.php', array(
                'school_id' => $school_id,
                "is_school_signed_up" => $is_signed_up["is_school_signed_up"]
                )
            );
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);



$app->get(
    '/users/sign-up/:schoolId',
    function ($schoolId) use ($app, $log){
        if($log->isUserLoggedIn() && $schoolId == $_SESSION["school_id"] && !$log->isAdmin()) {
            $app->render('user/sign-up.php', array('school_id' => $schoolId));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);


$app->get(
    '/users/download/:schoolId',
    function ($schoolId) use ($app, $log){
        if($log->isUserLoggedIn() && $schoolId == $_SESSION["school_id"] && !$log->isAdmin()) {
            $app->render('user/download.php', array('school_id' => $schoolId));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);




$app->get(
    '/users/classlist/:schoolId',
    function ($schoolId) use ($app, $log){
        if($log->isUserLoggedIn() && $schoolId == $_SESSION["school_id"] && !$log->isAdmin()) {
            $app->render('user/classlist.php', array('school_id' => $schoolId));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);

$app->get(
    '/users/scores/:schoolId/:competitionId/:schoolCompetitionId/',
    function ($schoolId, $competitionId, $schoolCompetitionId) use ($app, $log){
        if($log->isUserLoggedIn() && $schoolId == $_SESSION["school_id"] && !$log->isAdmin()) {
            $app->render('user/enter-scores.php', array('school_id' => $schoolId,
                'schoolCompetitionId' => $schoolCompetitionId,
                'competitionId' => $competitionId));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);
