<?php

/**
 * Created by PhpStorm.
 * User: Davey
 * Date: 1/22/17
 * Time: 9:39 AM
 */
class SchoolTest extends \Illuminate\Database\Eloquent\Model implements IEduObject{

    
    protected $table = 'school_test';

    public function school_year(){
        return $this->belongsTo("SchoolYear");
    }

    public function test(){
        return $this->belongsTo("Test");
    }


    public function setAttributes($data){

        if(isset($data["id"])){
            $this->setAttribute("id", $data["id"]);
        }

        if(isset($data["school_year_id"])){
            $this->setAttribute("school_year_id", $data["school_year_id"]);
        }

        if(isset($data["test"]["id"])){
            $this->setAttribute("test_id", $data["test"]["id"]);
        }
    }


}