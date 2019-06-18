<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/30/16
 * Time: 10:46 AM
 */


$app->get(
    '/admin',
    function () use ($app, $log){
        if($log->isAdmin()){
            $app->render('admin/index.php');
        }else{
            echo '<script>window.location.href = "/";</script>';
        }

    }
);



$app->get(
    '/admin/school/current',
    function () use ($app, $log){
    	 if($log->isAdmin()){
         $app->render('admin/school/current.php');
			 }else{
            echo '<script>window.location.href = "/";</script>';
        }
       
    }
);


$app->get(
    '/admin/school',
    function () use ($app, $log){
    	 if($log->isAdmin()){
        $app->render('admin/school/master.php');
			 }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);

$app->get(
    '/admin/school/:school_id',
    function ($school_id) use ($app, $log){
    	if($log->isAdmin()){
         $app->render('admin/school/account.php', array('school_id' => $school_id));
			 }else{
            echo '<script>window.location.href = "/";</script>';
        }
       
    }
);

$app->get(
    '/admin/school/create/',
    function () use ($app, $log){
        if($log->isAdmin()){
            $app->render('admin/school/create.php');
        }else{
            echo '<script>window.location.href = "/";</script>';
        }

    }
);

$app->get(
    '/admin/school/created/',
    function () use ($app, $log){
        if($log->isAdmin()){
            $app->render('admin/school/created.php',  array('id' => $_GET["school_id"], "password" => $_GET["school_password"]));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }

    }
);




$app->get(
    '/admin/school/dashboard/:school_id',
    function ($school_id) use ($app, $log){
        if($log->isAdmin()) {
            $service = Factory::createService("Year");
            $params = array("isSchoolSignedUp" => 1, "school_id" => $school_id );
            $is_signed_up = $service->find($params);
            $app->render('admin/school/dashboard.php', array(
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
    '/admin/school/sign-up/:school_id',
    function ($school_id) use ($app, $log){
        if($log->isAdmin()) {
            $app->render('user/sign-up.php', array('school_id' => $school_id));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);


$app->get(
    '/admin/school/classlist/:school_id',
    function ($school_id) use ($app, $log){
        if($log->isAdmin()) {
            $app->render('admin/school/classlist.php', array('school_id' => $school_id));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);


$app->get(
    '/admin/school/scores/:schoolId/:competitionId/:schoolCompetitionId/',
    function ($schoolId, $competitionId, $schoolCompetitionId) use ($app, $log){
        if($log->isAdmin()) {
            $app->render('admin/school/enter-scores.php', array('school_id' => $schoolId,
                'schoolCompetitionId' => $schoolCompetitionId,
                'competitionId' => $competitionId));
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);



//CURRENT YEAR

$app->get(
    '/admin/invoices',
    function () use ($app, $log){
        if($log->isAdmin()) {
            $app->render('admin/invoices/index.php');
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);



$app->get(
    '/admin/divisions',
    function () use ($app, $log){
        if($log->isAdmin()) {
            $app->render('admin/divisions/index.php');
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);




$app->get(
    '/admin/settings',
    function () use ($app, $log){
        if($log->isAdmin()) {
            $app->render('admin/settings/settings.php');
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);

$app->get(
    '/admin/password',
    function () use ($app, $log){
        if($log->isAdmin()) {
            $app->render('admin/school/reset-passwords.php');
        }else{
            echo '<script>window.location.href = "/";</script>';
        }
    }
);
