<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/16/18
 * Time: 12:11 PM
 */

namespace MoaMaster\Domain\Models;


use Illuminate\Database\Eloquent\Model;
use MedalsObject\IRestModel;

class Payment extends Model implements IRestModel {


    protected $table = "moa_order_payment";
    protected $connection = "moa";

    public function order(){
        return $this->belongsTo('MoaMaster\\Domain\\Models\\Order', "id","order_id");
    }


    public function setAttributes($parameters = array()){

        if(isset($parameters->cc_type)){
            $this->cc_type = $parameters->cc_type;
        }
        if(isset($parameters->transaction_id)){
            $this->transaction_id = $parameters->transaction_id;
        }

        if(isset($parameters->payment_provider)){
            $this->payment_provider = $parameters->payment_provider;
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