<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/29/16
 * Time: 3:46 PM
 */


//PAGES
$app->get(
    '/',
    function () use ($app){
        $app->render('pages/home.php');
    }
);

$app->get(
    '/about',
    function () use ($app){
        $app->render('pages/about.php');
    }
);

$app->get(
    '/samples',
    function () use ($app){
        $app->render('pages/samples.php');
    }
);

$app->get(
    '/contact',
    function () use ($app){
        $app->render('pages/contact.php');
    }
);

$app->get(
    '/results',
    function () use ($app){
        $app->render('pages/results.php');
    }
);
