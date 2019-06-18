<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/26/16
 * Time: 11:20 AM
 */

//Config file that has all the DB information on it.

class Config{

    //EMAIL

   // const EMAIL_USER = "scores@educontest.com";
    // const EMAIL_PASSWORD = "Mathfax1";
    
	const EMAIL_USER = "scores@educontest.com";
    const EMAIL_PASSWORD = "Mathfax1";
	
    const DSN = "mysql:host=localhost;dbname=educontest_test;charset=utf8";
    const DB_USERNAME = "root";
    const DB_PASSWORD = "root";

//    const DSN = "mysql:host=localhost;dbname=edudavid_edu;charset=utf8";
//    const DB_USERNAME = "edudavid_davey";
//    const DB_PASSWORD = "!Rishman1";

    public static function init(){
        define("ENCRYPTION_KEY", "%2K*vb^&d#02PMn6%^Dl1*%$#)_#$@dva");
        date_default_timezone_set('America/New_York');
    }

}