<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/22/18
 * Time: 6:01 PM
 */
namespace PatriotIssue\Domain\Models;
use MedalsObject\IRestModel;


class Order implements IRestModel {

    //everything is abstracted to the ORM (there is not a table for this

    private $order_id;
    private $customer_id;
    private $gross_amount;
    private $net_amount;
    private $total_amount;
    private $shipping_amount;
    private $shipping_method;
    private $tax_amount;
    private $discount_amount;
    private $discount_code;
    private $payment_method;
    private $ip_address;
    private $customer_message;
    private $date_created;
    private $date_modified;

    public function setAttributes($parameters = array()){


        if(isset($parameters->id)){
            $this->order_id = $parameters->id;
        }

        if(isset($parameters->customer_id)){
            $this->customer_id = $parameters->customer_id;
        }

        if(isset($parameters->subtotal_ex_tax)){
            $this->gross_amount = $parameters->subtotal_ex_tax;
        }

        if(isset($parameters->base_shipping_cost)){
            $this->shipping_amount = $parameters->base_shipping_cost;
        }


        if(isset($parameters->total_ex_tax)){
            $this->net_amount = $parameters->total_ex_tax - $this->shipping_amount ;
        }


        $this->shipping_method = "standard";

        if(isset($parameters->total_tax)){
            $this->tax_amount = $parameters->total_tax;
        }

        $this->setTotalAmount();
        $this->setDiscountCode($parameters);

        if(isset($parameters->coupon_discount)){
            $this->discount_amount = $parameters->coupon_discount;
        }

        if(isset($parameters->payment_method)){
            $this->payment_method = $parameters->payment_method;
        }

        if(isset($parameters->ip_address)){
            $this->ip_address = $parameters->ip_address;
        }

        if(isset($parameters->status)){
            $this->status = $parameters->status;
        }

        if(isset($parameters->customer_message)){
            $this->customer_message = $parameters->customer_message;
        }

        if(isset($parameters->date_created)){
            $this->date_created = $parameters->date_created;
        }


        if(isset($parameters->date_modified)){
            $this->date_modified = $parameters->date_modified;
        }



    }

    /**
     * @param mixed $discount_code
     */
    public function setDiscountCode($parameters){
       if(isset($parameters->coupons[0])){
           $this->discount_code = $parameters->coupons[0]->code;
       }
    }

    /**
     * @param mixed $total_amount
     */
    public function setTotalAmount(){
        $this->total_amount =
            $this->net_amount
            + $this->tax_amount
            + $this->shipping_amount;
    }

    public function convertToDbObject(){
        // TODO: Implement convertToDbObject() method.
        $data = array(
            "site_id" => "pi",
            "web_order_id"=>$this->order_id,
            "gross_amount"=>$this->gross_amount,
            "net_amount"=>$this->net_amount,
            "total_amount"=>$this->total_amount,
            "payment_method"=>$this->payment_method,
            "shipping_amount"=>$this->shipping_amount,
            "shipping_method"=>$this->shipping_method,
            "discount_amount"=>$this->discount_amount,
            "discount_code" => $this->discount_code,
            "tax_amount" => $this->tax_amount,
            "status" => $this->status,
            "is_processed" => 0,
            "ip_address" => $this->ip_address,
            "customer_message"=>$this->customer_message,
            "date_created"=>$this->date_created,
            "date_modified"=>$this->date_modified
      );
        return $data;
    }


    public function toString(){
        foreach ($this as $key => $value) {
            echo nl2br("$key => $value\r\n");
        }
    }

    public function toJSONObject(){
        // TODO: Implement toJSONObject() method.
        $data = array();
        foreach ($this as $key => $value) {
            $data[$key] = $value;
        }
        echo json_encode($data);
    }




}