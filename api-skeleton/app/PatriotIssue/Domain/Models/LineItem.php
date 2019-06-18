<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/5/18
 * Time: 4:56 PM
 */

namespace PatriotIssue\Domain\Models;


use MedalsObject\IRestModel;

class LineItem implements IRestModel{

    private $order_id;
    private $sku;
    private $name;
    private $gross_amount;
    private $net_amount;
    private $total_amount;
    private $tax_amount;
    private $discount_amount;
    private $quantity;

    public function setAttributes($parameters = array()){

        if(isset($parameters->order_id)){
            $this->order_id = $parameters->order_id;
        }
        if(isset($parameters->sku)){
            $this->sku = $parameters->sku;
        }
        if(isset($parameters->name)){
            $this->name = $parameters->name;
        }
        if(isset($parameters->base_price)){
            $this->gross_amount = $parameters->base_price;
        }

        if(isset($parameters->quantity)){
            $this->quantity = $parameters->quantity;
        }

        if(isset($parameters->applied_discounts[0]->amount)){
            $this->net_amount =  ($this->gross_amount *  $this->quantity - $parameters->applied_discounts[0]->amount) / floatval($this->quantity);
            $this->discount_amount = $parameters->applied_discounts[0]->amount / floatval($this->quantity);
        }else{
            $this->net_amount =  $this->gross_amount;
        }

        if(isset($parameters->total_tax)){
            $this->tax_amount = $parameters->total_tax / floatval($this->quantity);
        }

        $this->total_amount = ($this->net_amount + $this->tax_amount);

    }

    public function convertToDbObject(){
        $data = array(
            "sku"=>$this->sku,
            "name"=>$this->name,
            "gross_amount"=>$this->gross_amount,
            "net_amount"=>$this->net_amount,
            "total_amount"=>$this->total_amount,
            "tax_amount"=>$this->tax_amount,
            "discount_amount"=>$this->discount_amount,
            "quantity" => $this->quantity
        );
        return $data;
    }

    public function toJSONObject(){
        foreach ($this as $key => $value) {
            echo nl2br("$key => $value\r\n");
        }
    }

    public function toString(){
        $data = array();
        foreach ($this as $key => $value) {
            $data[$key] = $value;
        }
        echo json_encode($data);
    }


}