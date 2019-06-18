<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/29/16
 * Time: 3:47 PM
 */


//registration views

$app->get(
    '/returning',
    function () use ($app, $log){
        if($log->isUserLoggedIn()) {
            $app->redirect('/');
        }else{
            $app->render('registration/returning.php');
        }
    }
);

$app->get(
    '/registration',
    function () use ($app, $log){
        if($log->isUserLoggedIn()) {
            echo '<script>window.location.href = "/";</script>';
        }else{
            $app->render('registration/registration.php');
        }

    }
);


$app->get(
    '/users/sign-in',
    function () use ($app, $log){
        if($log->isUserLoggedIn()) {
            echo '<script>window.location.href = "/";</script>';
        }else{
            $app->render('registration/sign-in.php');
        }
    }
);


$app->get(
    '/users/thank-you',
    function () use ($app, $log){
        if($log->isUserLoggedIn()) {
            $app->render('registration/thank-you.php');
        }else{
            $app->redirect('/');

        }
    }
);


$app->get(
    '/logout',
    function () use ($app, $log) {
        $log->logout($app);
        echo '<script>window.location.href = "/";</script>';
    }
);



/* AUTHENTICATION */
$app->post(
    '/verify',
    function () use ($app, $log) {
        if($log->isUserLoggedIn()){
            echo '<script>window.location.href = "/";</script>';
        }else{
            if (!empty($_POST['username']) && !empty($_POST['password'])) {
                $is_valid = $log->login($_POST['username'], $_POST['password']);
                if($is_valid){
                    echo '<script>window.location.href = "/";</script>';
                }else{
                    $params = array("invalid" => 1);
                    $app->render('registration/sign-in.php', $params);
                }
            }else{
                //echo '<script>window.location.href = "/";</script>';
//            $app->redirect("/");
            }
            
        }
    }
);


$app->get(
    '/edu-login',
    function () use ($app, $log){
        if($log->isUserLoggedIn()) {
            echo '<script>window.location.href = "/";</script>';
        }else{
            $app->render('admin/login.php');
        }
    }
);


$app->post(
    '/admin-verify',
    function () use ($app, $log) {
        if($log->isUserLoggedIn()){
            echo '<script>window.location.href = "/";</script>';
        }else{
            if (!empty($_POST['username']) && !empty($_POST['password'])) {
                $is_valid = $log->admin_login($_POST['username'], $_POST['password']);
                if($is_valid){
                    echo '<script>window.location.href = "/admin";</script>';
                }else{
                    $params = array("invalid" => 1);
                    $app->render('admin/login.php', $params);
                }
            }else{
                //echo '<script>window.location.href = "/";</script>';
//            $app->redirect("/");
            }

        }
    }
);