<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/4/16
 * Time: 10:38 AM
 */



//BASIC ROUTES
//These routes call methods in the IRestService Interface


//GET ALL
$app->get('/api/:class/', function ($class) use ($app, $factory){
    echo Factory::createService($class)->getAll();
});

//GET IEduObject by ID
$app->get('/api/:class/:id', function ($class, $id) use ($app){
    echo Factory::createService($class)->get($id);
});


//FIND
$app->get('/api/find/:class/', function ($class) use ($app, $factory){
    $service = Factory::createService($class);
    echo  json_encode($service->find($app->request->params()));
    }
);
// POST route
$app->post(
    '/api/:class',
    function ($class) use ($app) {
        $data = json_decode($app->request->getBody(), true);
        $service = Factory::createService($class);
        $service->post($data);
    }
);

$app->put(
    '/api/:class',
    function ($class) use ($app) {
        $data = json_decode($app->request->getBody(), true);
        $service = Factory::createService($class);
        $service->put($data);
    }
);

$app->delete(
    '/api/:class/:id',
    function ($class, $id) use ($app) {
        $service = Factory::createService($class);
        $service->delete($id);
    }
);









