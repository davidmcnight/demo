<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/5/18
 * Time: 10:46 AM
 */

namespace MoaMaster\Domain\Factory;

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
        try {
            //get class name and create get the path for that model class
            $className = ucfirst($className);
            $className = "MoaMaster\\Domain\\Models\\" . $className;
            //create new object based on string
            $object = new $className;
            //set attributes and return object
            $object->setAttributes($data);
            return $object;
        }catch (\Exception $e){
            echo $e->getMessage();
        }
        return null;
    }


    //Creates service from model name -- thank PHP for how easy this is.
    public static function createService($className){
        if($className){
            $className = ucfirst($className);
            $className = "MoaMaster\\Domain\\Services\\". $className . "Service";
            $serviceClass = $className;
            return new $serviceClass;
        }
        return null;
    }

}