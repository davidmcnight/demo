<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/6/18
 * Time: 4:17 PM
 */

namespace MoaMaster\Domain\Models;


use Illuminate\Database\Eloquent\Model;
use MedalsObject\IRestModel;

class ShippingAddress extends Model implements IRestModel{


    protected $table = "moa_address_shipping";
    protected $connection = "moa";

    public function order(){
        return $this->belongsTo('MoaMaster\\Domain\\Models\\Order', "id", "order_id");
    }

    public function setAttributes($parameters = array()){
        if(isset($parameters->email)){
            $this->email = $parameters->email;
        }

        if(isset($parameters->phone)){
            $this->phone = $parameters->phone;
        }

        if(isset($parameters->first_name)){
            $this->first_name = $parameters->first_name;
        }

        if(isset($parameters->last_name)){
            $this->last_name = $parameters->last_name;
        }

        if(isset($parameters->street_1)){
            $this->street_1 = $parameters->street_1;
        }

        if(isset($parameters->street_2)){
            $this->street_2 = $parameters->street_2;
        }

        if(isset($parameters->city)){
            $this->city = $parameters->city;
        }

        if(isset($parameters->state)){
            $this->state = $parameters->state;
        }

        if(isset($parameters->zip)){
            $this->zip = $parameters->zip;
        }

        if(isset($parameters->country)){
            $this->country = $parameters->country;
        }
    }

    public function convertToDbObject()
    {
        // TODO: Implement convertToDbObject() method.
    }

    public function toJSONObject()
    {
        // TODO: Implement toJSONObject() method.
    }

    public function toString()
    {
        // TODO: Implement toString() method.
    }


}