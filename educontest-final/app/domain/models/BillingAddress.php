<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/23/16
 * Time: 11:07 AM
 */
class BillingAddress extends \Illuminate\Database\Eloquent\Model implements IEduObject{
    protected $table ="billing_address";
    
    
    public function school(){
        return $this->belongsTo('School');
    }
    
    public function setAttributes($data)
    {
        if(isset($data["id"])){
            $this->setAttribute("id", $data["id"]);
        }

        if(isset($data["address1"])){
            $this->setAttribute("address1", $data["address1"]);
        }

        if(isset($data["address2"])){
            $this->setAttribute("address2", $data["address2"]);
        }


        if(isset($data["city"])){
            $this->setAttribute("city", $data["city"]);
        }

        if(isset($data["state"])){
            $this->setAttribute("state", $data["state"]);
        }

        if(isset($data["zip"])){
            $this->setAttribute("zip", $data["zip"]);
        }

        if(isset($data["school_id"])){
            $this->setAttribute("school_id", $data["school_id"]);
        }



    }


}