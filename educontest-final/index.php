<?php
/**
 * Step 1: Require the Slim Framework
 *
 * If you are not using Composer, you need to require the
 * Slim Framework and register its PSR-0 autoloader.
 *
 * If you are using Composer, you can skip this step.
 */
session_start();

require 'Slim/Slim.php';

\Slim\Slim::registerAutoloader();

require_once "includes.php";

$factory = Factory::getInstance();
Config::init();
$log = new Login();



//Config::init();
//$log = new Login();

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

//DEBUGGING
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);


function printNicely($value){
    print nl2br(print_r($value, true));
}

function sortByOrder($a, $b) {
    return $a['total'] - $b['total'];
}



//SET CONDITIONS
\Slim\Route::setDefaultConditions(array(
    'class' => '[a-zA-Z]{3,}',
    'schoolId' =>'[0-9]{0,10000}',
    'competitionId' =>'[0-9]{0,10000}',
    'testId' =>'[0-9]{0,10000}',
    'id' =>'[0-9]{0,10000}',
    'schoolCompetitionId' =>'[0-9]{0,10000}'
));

$settings = array(
    'driver' => 'mysql',
    'host' => '127.0.0.1',
    'database' => 'educontest_test',
    'username' => 'root',
    'password' => 'root',
    'collation' => 'utf8_general_ci',
    'prefix' => ''
);

//$settings = array(
//    'driver' => 'mysql',
//    'host' => '127.0.0.1',
//    'database' => 'educonte_edu',
//    'username' => 'educonte_edu',
//    'password' => 'L]]ZTf#5%0Jd',
//    'collation' => 'utf8_general_ci',
//    'prefix' => ''
//);



$container = new Illuminate\Container\Container;
$connFactory = new \Illuminate\Database\Connectors\ConnectionFactory($container);
$conn = $connFactory->make($settings);
$resolver = new \Illuminate\Database\ConnectionResolver();
$resolver->addConnection('default', $conn);
$resolver->setDefaultConnection('default');
\Illuminate\Database\Eloquent\Model::setConnectionResolver($resolver);




//
//$container->singleton(
//    'Illuminate\Contracts\Debug\ExceptionHandler'
//    , 'My\Custom\DatabaseExceptionHandler'
//);




//SET CONDITIONS
\Slim\Route::setDefaultConditions(array(
    'class' => '[a-zA-Z]{3,}',
    'schoolId' =>'[0-9]{0,10000}',
    'competitionId' =>'[0-9]{0,10000}',
    'testId' =>'[0-9]{0,10000}',
    'id' =>'[0-9]{0,10000}',
    'schoolCompetitionId' =>'[0-9]{0,10000}'
));




//BOOTSTRAP VIEWS

//public pages
require_once "app/views/pages.php";
//registration and authentication views
require_once "app/views/registration-authentication.php";
//admin
require_once "app/views/admin.php";
//user
require_once "app/views/users.php";


//BOOTSTRAP API

//iRestService
require_once "app/api/iRest.php";


function generateRandomString($length = 6) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}


$app->get(
    '/lumen',
    function () use ($app){

     try{
     	
// 		
			// $to = "davidmcnight@gmail.com";
			// $subject = "My subject";
			// $txt = "Hello world!";
			// $headers = "From: davidmcnight@gmail.com" . "\r\n" .
			// "CC: ";

			// mail($to,$subject,$txt,$headers);	
				
		
// 		
            // $transport = Swift_SmtpTransport::newInstance('smtp.gmail.com', 587, "tls")
                // ->setUsername(Config::EMAIL_USER)
                // ->setPassword(Config::EMAIL_PASSWORD);
            // $mailer = Swift_Mailer::newInstance($transport);
// 
            // $message = Swift_Message::newInstance("Welcome " . "Davey")
                // ->setFrom(array(Config::EMAIL_USER => 'Educontest'))
                // ->setTo(array('davidmcnight@gmail.com'))
                // ->setBody('Hello Davey', "text/html");
            // $result = $mailer->send($message);
            
            $mail = new PHPMailer;

        $mail->SMTPDebug = 3;                               // Enable verbose debug output

		// $mail->isSMTP();                                      // Set mailer to use SMTP
		$mail->Host = 'smtp.gmail.com';  // Specify main and backup SMTP servers
		$mail->SMTPAuth = true;                               // Enable SMTP authentication
		$mail->Username = 'scores@educontest.com';                 // SMTP username
		$mail->Password = 'Mathfax1';                           // SMTP password
		$mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
		$mail->Port = 587;                                    // TCP port to connect to
		
		$mail->setFrom('scores@educontest.com', 'Mathfax');
		$mail->addAddress('davidmcnight@gmail.com', 'Joe User');     // Add a recipient
		              // Name is optional
		
		$mail->isHTML(true);
		$mail->Subject = 'Here is the subject';
		$mail->Body    = 'This is the HTML message body <b>in bold!</b>';
		$mail->AltBody = 'This is the body in plain text for non-HTML mail clients';

		if(!$mail->send()) {
		    echo 'Message could not be sent.';
		    echo 'Mailer Error: ' . $mail->ErrorInfo;
		} else {
		    echo 'Message has been sent';
		}


    }catch (Exception $e){
            echo $e->getMessage();
        }
    }

  
);

$app->get(
    '/email-mass',function () use ($app) {
    try{


        $schools = School::where("league_id", 1)->get();


        $counter = 0;
        foreach ($schools as $school){


            if(isset($school->school_email)) {

                echo $school->name . '<br>';

                $mail = new PHPMailer;
                $mail->SMTPDebug = 3;
                // $mail->isSMTP();                                      // Set mailer to use SMTP
                $mail->Host = 'smtp.gmail.com';  // Specify main and backup SMTP servers
                $mail->SMTPAuth = true;                               // Enable SMTP authentication
                $mail->Username = 'scores@educontest.com';                 // SMTP username
                $mail->Password = 'Mathfax1';                           // SMTP password
                $mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
                $mail->Port = 587;                                // TCP port to connect to
                $mail->isHTML(true);
                $mail->setFrom('scores@educontest.com', 'Mathfax');
                $mail->addAddress($school->school_email);
                if (isset($school->email_2)) {
                    $mail->addAddress($school->email_2);
                }

                if (isset($school->email_3)) {
                    $mail->addAddress($school->email_3);
                }

                if (isset($school->email_4)) {
                    $mail->addAddress($school->email_4);
                }


                $mail->Subject = 'Important Catholic Math League Information';
                $mail->Body =
                    '<p>In an effort to simplify registration and give your school a better online experience, we have upgraded our website!
			 All teachers at your school will now use the same School Code and Password to login.<p>
			 <p>Your School Code: <b>' . $school->id .
                    '</b></p> <p>Your Password: <b>' . $school->password .
                    '</b></p><p>If at any time you forget your school code or password, please call us at 864-286-0020 or email info@catholicmathleague.com. We are looking forward to the next great year of competition!</p>';

            }
//             if(!$mail->send()) {
//             echo 'Message could not be sent.';
//             echo 'Mailer Error: ' . $mail->ErrorInfo;
//             } else {
//             echo 'Message has been sent';
//             }



        }
    }catch(Exception $e){
//        $school
        echo $e->getMessage();
    }



});




/**
 * Step 4: Run the Slim application
 *
 * This method should be called last. This executes the Slim application
 * and returns the HTTP response to the HTTP client.
 */
$app->run();
