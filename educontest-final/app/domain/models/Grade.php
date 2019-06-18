<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 12/20/16
 * Time: 10:54 AM
 */
class Grade extends \Illuminate\Database\Eloquent\Model implements IEduObject{

    protected $table = "grade";

    public function competition(){
       return $this->hasMany("Competition");
    }

    public function setAttributes($data){
        // TODO: Implement setAttributes() method.
    }


}