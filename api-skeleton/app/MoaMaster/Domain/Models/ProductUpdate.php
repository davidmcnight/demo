<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/27/18
 * Time: 4:40 PM
 */

namespace MoaMaster\Domain\Models;


use Illuminate\Database\Eloquent\Model;
use MedalsObject\IRestModel;


class ProductUpdate extends Model implements IRestModel{

    protected $connection = 'moa_live';
    protected $table = 'moa_product_update';

    public function setAttributes($parameters = array()){

        if(isset($parameters->id)){
            $this->id = $parameters->id;
        }

        if(isset($parameters->sku)){
            $this->sku = $parameters->sku;
        }

        if(isset($parameters->base_sku)){
            $this->base_sku = $parameters->base_sku;
        }

        if(isset($parameters->available)){
            $this->available = $parameters->available;
        }

        if(isset($parameters->list_price)){
            $this->list_price = $parameters->list_price;
        }

        if(isset($parameters->price_code_1)){
            $this->price_code_1 = $parameters->price_code_1;
        }

        if(isset($parameters->closeout)){
            $this->closeout = $parameters->closeout;
        }
        if(isset($parameters->discontinued)){
            $this->discontinued = $parameters->discontinued;
        }

        if(isset($parameters->product_class)){
            $this->product_class = $parameters->product_class;
        }

        if(isset($parameters->on_demand)){
            $this->on_demand = $parameters->on_demand;
        }

        if(isset($parameters->is_processed)){
            $this->is_processed = $parameters->is_processed;
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