<?php
/**
 * Created by PhpStorm.
 * User: specbooks
 * Date: 10/23/18
 * Time: 10:45 AM
 */

namespace SpecbooksObject\Helpers;

class DataHelper
{
    public static function makeObject($data = array()){
        return json_decode(json_encode($data), FALSE);
    }

    public static function cleanData($data){
        return strtolower(trim($data));
    }

}