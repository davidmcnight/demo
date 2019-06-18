<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/20/18
 * Time: 3:50 PM
 */

namespace MoaMaster\Domain\Services;


use MedalsObject\IRestService;
use MoaMaster\Domain\Factory\Factory;
class OrderLogService implements IRestService{
    public function get($id)
    {
        // TODO: Implement get() method.
    }

    public function getAll()
    {
        // TODO: Implement getAll() method.
    }

    public function getChildren($id, $childClass)
    {
        // TODO: Implement getChildren() method.
    }

    public function getByForeignKey($fk, $foreignClass)
    {
        // TODO: Implement getByForeignKey() method.
    }

    public function find($parameters = array())
    {
        // TODO: Implement find() method.
    }

    public function post($data){
        $order_log = Factory::createModel("OrderLog", $data);
        $order_log->save();
        return $order_log;
    }

    public function put($data)
    {
        // TODO: Implement put() method.
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }

    public function delete($id)
    {
        // TODO: Implement delete() method.
    }


}