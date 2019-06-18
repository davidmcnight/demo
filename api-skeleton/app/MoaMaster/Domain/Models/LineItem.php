<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/9/18
 * Time: 3:52 PM
 */

namespace MoaMaster\Domain\Models;


use Illuminate\Database\Eloquent\Model;
use MedalsObject\IRestModel;

class LineItem extends Model implements IRestModel {

    protected $table = "moa_order_line_item";
    protected $connection = "moa";

    public function order(){
        return $this->belongsTo('MoaMaster\\Domain\\Models\\Order', "id","order_id");
    }

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
        if(isset($parameters->gross_amount)){
            $this->gross_amount = $parameters->gross_amount;
        }

        if(isset($parameters->net_amount)){
            $this->net_amount = $parameters->net_amount;
        }

        if(isset($parameters->total_amount)){
            $this->total_amount = $parameters->total_amount;
        }

        if(isset($parameters->tax_amount)){
            $this->tax_amount = $parameters->tax_amount;
        }

        if(isset($parameters->discount_amount)){
            $this->discount_amount = $parameters->discount_amount;
        }

        if(isset($parameters->quantity)){
            $this->quantity = $parameters->quantity;
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