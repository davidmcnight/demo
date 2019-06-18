<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/19/18
 * Time: 10:45 AM
 */

namespace MoaMaster\Domain\Models;


use Illuminate\Database\Eloquent\Model;
use MedalsObject\IRestModel;

class OrderLog extends Model implements IRestModel {

    protected $table = "moa_order_log";
    protected $connection = "moa";

    public function setAttributes($parameters = array()){

        if(isset($parameters->site_id)){
            $this->site_id = $parameters->site_id;
        }

        if(isset($parameters->web_order_id)){
            $this->web_order_id = $parameters->web_order_id;
        }
        if(isset($parameters->success)){
            $this->success = $parameters->success;
        }

        if(isset($parameters->message)){
            $this->message = $parameters->message;
        }

        if(isset($parameters->trace)){
            $this->trace = $parameters->trace;
        }

        if(isset($parameters->line)){
            $this->line = $parameters->line;
        }

        if(isset($parameters->is_processed)){
            $this->is_processed = $parameters->is_processed;
        }

        if(isset($parameters->issue_resolved)){
            $this->issue_resolved = $parameters->issue_resolved;
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