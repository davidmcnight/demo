<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/29/16
 * Time: 4:17 PM
 */
//require_once "../config/Config.php";

class Database{
    

    private static $instance;

    /**
     * Protected constructor to prevent creating a new instance of the
     * *Singleton* via the `new` operator from outside of this class.
     */
    protected function __construct(){

    }

    /**
     * Returns the *Singleton* instance of this class.
     *
     * @return Singleton The *Singleton* instance.
     */
    public static function getInstance(){
        if (null === static::$instance) {
            static::$instance = new PDO(Config::DSN, Config::DB_USERNAME, Config::DB_PASSWORD);
        }
        return static::$instance;
    }
    
}