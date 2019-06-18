<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/5/18
 * Time: 4:55 PM
 */

namespace PatriotIssue\Domain\Models;


use MedalsObject\IRestModel;

class ShippingAddress implements IRestModel
{

    private $order_id;
    private $first_name;
    private $last_name;
    private $email;
    private $phone;
    private $street_1;
    private $street_2;
    private $city;
    private $state;
    private $zip;


    public function setAttributes($parameters = array()){
        if(isset($parameters->id)){
            $this->order_id = $parameters->id;
        }

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

        if(isset($parameters->country_iso2)){
            $this->country = $parameters->country_iso2;
        }
    }

    public function convertToDbObject()
    {
        $data = array(
            "first_name"=>$this->first_name,
            "last_name"=>$this->last_name,
            "email"=>$this->email,
            "phone"=>$this->phone,
            "street_1"=>$this->street_1,
            "street_2"=>$this->street_2,
            "city"=>$this->city,
            "state" => $this->state,
            "zip" => $this->zip,
            "country" => $this->country
        );
        return $data;
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