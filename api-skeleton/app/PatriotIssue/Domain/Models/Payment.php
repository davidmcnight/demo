<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/5/18
 * Time: 4:56 PM
 */

namespace PatriotIssue\Domain\Models;


use MedalsObject\IRestModel;
use PatriotIssue\Config\Config;

class Payment implements IRestModel {

    private $order_id;
    private $transaction_id;
    private $method;
    private $status;
    private $cc_type;



    public function setAttributes($parameters = array()){
        // TODO: Implement setAttributes() method.

        if(isset($parameters->payment_method)){
            $this->method = $parameters->payment_method;
        }

        if(isset($parameters->payment_provider_id)){
            $this->transaction_id = $parameters->payment_provider_id;
        }

        if(isset($parameters->status)){
            $this->status = $parameters->status;
        }

        if(isset($parameters->credit_card_type)){
           $this->setCcType($parameters->credit_card_type);
        }

    }

    public function convertToDbObject(){
        // TODO: Implement convertToDbObject() method.

        if(trim($this->method) == "Credit Card"){
            $method = "cc";
        }else{
            $method = $this->method;
        }

        $data = array(
            "payment_method" => $method,
            "transaction_id" =>$this->transaction_id,
            "payment_provider" => Config::$payment_provider,
            "cc_type" =>$this->cc_type,
            "status" =>$this->status
        );

        return $data;
    }

    /**
     * @param mixed $cc_type
     */
    public function setCcType($cc_type){
        if($cc_type == "AMERICAN_EXPRESS"){
            $this->cc_type = "AMX";
        }elseif($cc_type == "VISA"){
            $this->cc_type = "VI";
        }elseif($cc_type == "MASTERCARD"){
            $this->cc_type = "MC";
        }elseif($cc_type == "DISCOVER"){
            $this->cc_type = "DI";
        }else{
            $this->cc_type =  substr($this->cc_type , 0, 2);
        }


    }



    public function toJSONObject(){
        // TODO: Implement toJSONObject() method.
    }

    public function toString(){
        // TODO: Implement toString() method.
    }

}