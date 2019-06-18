<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/23/16
 * Time: 1:12 PM
 */
class League extends \Illuminate\Database\Eloquent\Model implements IEduObject {
    
    protected $table = "league";
    
    public function school(){
        return $this->hasMany('School');
    }

    public function setAttributes($data){
        // TODO: Implement setAttributes() method.
    }


}