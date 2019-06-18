<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/2/18
 * Time: 3:16 PM
 */
namespace MoaMaster\Domain\Models;
use Illuminate\Database\Eloquent\Model;
use MedalsObject\IRestModel;

class Order extends Model implements IRestModel{


    protected $connection = 'moa';
    protected $table = 'moa_order';


    public function billing_address(){
        return $this->hasOne("MoaMaster\\Domain\\Models\\ShippingAddress", "order_id", "id");
    }

    public function shipping_address(){
        return $this->hasOne("MoaMaster\\Domain\\Models\\BillingAddress", "order_id", "id");
    }

    public function line_items(){
        return $this->hasMany("MoaMaster\\Domain\\Models\\LineItem", "order_id", "id");
    }

    public function payment(){
        return $this->hasOne("MoaMaster\\Domain\\Models\\Payment", "order_id", "id");
    }


    public function setAttributes($parameters = array()){

        if(isset($parameters->id)){
            $this->id = $parameters->id;
        }

        if(isset($parameters->web_order_id)){
            $this->web_order_id = $parameters->web_order_id;
        }
        //site ID -- then set increment
        if(isset($parameters->site_id)){
            $this->site_id = $parameters->site_id;
        }
        //order amounts
        if(isset($parameters->gross_amount)){
            $this->gross_amount = $parameters->gross_amount;
        }

        if(isset($parameters->net_amount)){
            $this->net_amount = $parameters->net_amount;
        }

        if(isset($parameters->total_amount)){
            $this->total_amount = $parameters->total_amount;
        }

        //payment method
        if(isset($parameters->payment_method)){
            $this->payment_method = $parameters->payment_method;
        }
        //shipping

        if(isset($parameters->shipping_amount)){
            $this->shipping_amount = $parameters->shipping_amount;
        }

        if(isset($parameters->shipping_method)){
            $this->shipping_method = $parameters->shipping_method;
        }

        //tax
        if(isset($parameters->tax_amount)){
            $this->tax_amount = $parameters->tax_amount;
        }
        //discount
        if(isset($parameters->discount_amount)){
            $this->discount_amount = $parameters->discount_amount;
        }

        if(isset($parameters->discount_code)){
            $this->discount_code = $parameters->discount_code;
        }

        //order ino
        if(isset($parameters->status)){
            $this->status = $parameters->status;
        }

        if(isset($parameters->is_processed)){
            $this->is_processed = $parameters->is_processed;
        }

        if(isset($parameters->is_address)){
            $this->is_address = $parameters->is_address;
        }

        if(isset($parameters->date_created)){
            $this->date_created = date('Y-m-d H:i:s', strtotime($parameters->date_created));
        }
        if(isset($parameters->date_modified)){
            $this->date_modified = date('Y-m-d H:i:s', strtotime($parameters->date_modified));
        }

        //customer
        if(isset($parameters->customer_message)){
            $this->customer_message = $parameters->customer_message;
        }



    }

    public function convertToDbObject(){

    }

    public function toString(){
        // TODO: Implement toString() method.
    }

    public function toJSONObject()
    {
        // TODO: Implement toJSONObject() method.
    }

    //setters
    public function setBillingAddress(BillingAddress $billingAddress){
        $this->setRelation('billing_address', $billingAddress);
    }

    public function setShippingAddress(ShippingAddress $shippingAddress){
        $this->setRelation('shipping_address', $shippingAddress);
    }

    public function setLineItems($line_items = array()){
        $this->setRelation('line_items', $line_items);
    }

    public function setPayment(Payment $payment){
        $this->setRelation('payment', $payment);
    }





}