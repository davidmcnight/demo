<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/5/18
 * Time: 4:53 PM
 */

namespace PatriotIssue\Domain\Models;


use MedalsObject\IRestModel;

class BillingAddress implements IRestModel {

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
    private $country;



    public function setAttributes($parameters = array()){

        if(isset($parameters->id)){
            $this->order_id = $parameters->id;
        }

        if(isset($parameters->billing_address->email)){
            $this->email = $parameters->billing_address->email;
        }

        if(isset($parameters->billing_address->phone)){
            $this->phone = $parameters->billing_address->phone;
        }

        if(isset($parameters->billing_address->first_name)){
            $this->first_name = $parameters->billing_address->first_name;
        }

        if(isset($parameters->billing_address->last_name)){
            $this->last_name = $parameters->billing_address->last_name;
        }

        if(isset($parameters->billing_address->street_1)){
            $this->street_1 = $parameters->billing_address->street_1;
        }

        if(isset($parameters->billing_address->street_2)){
            $this->street_2 = $parameters->billing_address->street_2;
        }

        if(isset($parameters->billing_address->city)){
            $this->city = $parameters->billing_address->city;
        }

        if(isset($parameters->billing_address->state)){
            $this->state = $parameters->billing_address->state;
        }

        if(isset($parameters->billing_address->zip)){
            $this->zip = $parameters->billing_address->zip;
        }

        if(isset($parameters->billing_address->country_iso2)){
            $this->country = $parameters->billing_address->country_iso2;
        }

    }

    public function convertToDbObject(){
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