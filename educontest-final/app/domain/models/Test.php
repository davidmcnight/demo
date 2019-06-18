<?php

/**
 * Created by PhpStorm.
 * User: Davey
 * Date: 1/22/17
 * Time: 9:28 AM
 */
class Test extends \Illuminate\Database\Eloquent\Model implements IEduObject{


    protected $table = "test";


    public function school_test(){
        return $this->hasMany('SchoolTest');
    }

    public function year(){
        return $this->belongsTo("Year");
    }
    public function grade(){
        return $this->belongsTo("Grade");
    }


    public function setAttributes($data){

    }


}