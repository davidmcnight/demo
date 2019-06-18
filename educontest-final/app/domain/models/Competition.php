<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 12/20/16
 * Time: 10:44 AM
 */
class Competition extends \Illuminate\Database\Eloquent\Model implements IEduObject {

    protected $table = "competition";
    
    public function school_competition(){
        return $this->hasMany('SchoolCompetition');
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