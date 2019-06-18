<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/30/16
 * Time: 11:22 AM
 */
class Year extends \Illuminate\Database\Eloquent\Model implements IEduObject{



    protected $table = "year";

    public function school_year(){
        return $this->hasMany('SchoolYear');
    }

    public function competition(){
        return $this->hasMany("Competition");
    }

    public function setAttributes($data){

    }


}