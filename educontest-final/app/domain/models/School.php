<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/23/16
 * Time: 10:51 AM
 */


class School extends \Illuminate\Database\Eloquent\Model implements IEduObject {

    //db table
    protected $table = "school";

//    protected $hidden = array('password');

    //relationships
    public function billing_address(){
        return $this->hasOne('BillingAddress');
    }

    public function shipping_address(){
        return $this->hasOne('ShippingAddress');
    }

    public function league(){
        return $this->belongsTo('League');
    }

    public function school_year(){
        return $this->hasMany('SchoolYear');
    }
    
    

    // this acts as a constructor
    public function setAttributes($data){

        if(isset($data["id"])){
            $this->setAttribute("id", $data["id"]);
        }

        if(isset($data["gen-password"])){
            $this->setAttribute("password", $data["gen-password"]);
        }

        if(isset($data["name"])){
            $this->setAttribute("name", $data["name"]);
        }

        if(isset($data["contact"])){
            $this->setAttribute("contact", $data["contact"]);
        }

        if(isset($data["billing_name"])){
            $this->setAttribute("billing_name", $data["billing_name"]);
        }

        if(isset($data["purchase_order"])){
            $this->setAttribute("purchase_order", $data["purchase_order"]);
        }

        if(isset($data["school_email"])){
            $this->setAttribute("school_email", $data["school_email"]);
        }

        if(isset($data["phone"])){
            $this->setAttribute("phone", $data["phone"]);
        }
        if(isset($data["fax"])){
            $this->setAttribute("fax", $data["fax"]);
        }
        if(isset($data["league_id"])){
            $this->setAttribute("league_id", $data["league_id"]);
        }

        if(isset($data["email_2"])){
            $this->setAttribute("email_2", $data["email_2"]);
        }

        if(isset($data["email_3"])){
            $this->setAttribute("email_3", $data["email_3"]);
        }

        if(isset($data["email_4"])){
            $this->setAttribute("email_4", $data["email_4"]);
        }


        
    }


}


?>