<?php
/**
 * Step 1: Require the Slim Framework
 *
 * If you are not using Composer, you need to require the
 * Slim Framework and register its PSR-0 autoloader.
 *
 * If you are using Composer, you can skip this step.
 */


ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

function printNicely($value){
    print nl2br(print_r($value, true));
}

use Sgpatil\Orientdb\OrientdbServiceProvider as OrientModel;

//phpinfo();

header('Access-Control-Allow-Origin: *');


//ini_set('memory_limit','1000M');

//foreach (glob("app/MoaMag/api/*.php") as $filename) {require_once $filename;}

require 'Slim/Slim.php';
//require_once "Config.php";
require_once "vendor/autoload.php";

\Slim\Slim::registerAutoloader();

//Config::init();



/**
 * Step 2: Instantiate a Slim application
 *
 * This example instantiates a Slim application using
 * its default settings. However, you will usually configure
 * your Slim application now by passing an associative array
 * of setting names and values into the application constructor.
 */
$app = new \Slim\Slim();


/**
 * Step 3: Define the Slim application routes
 *
 * Here we define several Slim application routes that respond
 * to appropriate HTTP request methods. In this example, the second
 * argument for `Slim::get`, `Slim::post`, `Slim::put`, `Slim::patch`, and `Slim::delete`
 * is an anonymous function.
 */


//SET CONDITIONS
\Slim\Route::setDefaultConditions(array(
    'class' => '[a-zA-Z]{3,}',
    'parent' => '[a-zA-Z]{3,}',
    'child' => '[a-zA-Z]{3,}',
    'foreignClass' => '[a-zA-Z]{3,}',
    'fk' => '[0-9]{0,10000}',
    'id' =>'[0-9]{0,10000}'
));


//Database connections
//$settings = array(
//
//    'default' => 'sqlsrv',
//    'connections' => array(
//        'resp' => array(
//            'driver' => 'sqlsrv',
//            'host' => 'MOASQL01',
//            'database' => 'R4W_primary;ConnectionPooling=0;',
//            'username' => 'sa',
//            'password' => 'm2a2oh58',
//            'collation' => 'utf8_general_ci',
//            'prefix' => ''
//        ),
//        'orientdb' => [
//            'driver' => 'orientdb',
//            'host'   => 'testing.specbooks.com',
//            'port'   => '2480',
//            'database' => 'Specbook',
//            'username' => 'root',
//            'password' => 'root'
//        ]
//    )
//);
//
//
//
////bootstrapping everything
//$container = new Illuminate\Container\Container;
//$connFactory = new \Illuminate\Database\Connectors\ConnectionFactory($container);
//$resp = $connFactory->make($settings["connections"]["orientdb"]);
//$resolver = new \Illuminate\Database\ConnectionResolver();
//$resolver->addConnection('orientdb', $resp);
//$resolver->setDefaultConnection('orientdb');
//\Illuminate\Database\Eloquent\Model::setConnectionResolver($resolver);

//required files (API) Namespaces in composer JSON

//test urls
$app->get(
    '/',
    function () use ($app) {
        try{

            $db = new OrientDB('testing.specbooks.com', 2480);
            $config = $db->DBOpen('Specbooks', 'root', 'root');
            printNicely($config);
//            printNicely($db->connect("root", "root"));

        }catch (Exception $e){
            echo $e->getMessage();
        }
    }
);




// run app
$app->run();
