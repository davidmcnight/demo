<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/29/16
 * Time: 3:56 PM
 */
class Factory{

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
            static::$instance = new Factory();
        }
        return static::$instance;
    }

    //BLAME THIS ON PHP NOT ALLOWING Multiple Constructors Sigh
    public static function createModel($className, $data){
        $object = new $className;
        $object->setAttributes($data);
        return $object;
    }
    

    //Creates service from model name -- thank PHP for how easy this is.
    public static function createService($className){
        if($className){
            $serviceClass = $className . "Service";
            return new $serviceClass;
        }
        return null;
    }
    
    //initiates a School Object from the DB record and the below 3 objects
    public static function createSchool($data){
        $school = new School();
        $school->setAttributes($data);
        return $school;
    }

}