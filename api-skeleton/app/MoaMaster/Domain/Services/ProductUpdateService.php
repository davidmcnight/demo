<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/27/18
 * Time: 5:11 PM
 */

namespace MoaMaster\Domain\Services;


use MedalsObject\IRestService;
use MoaMaster\Domain\Factory\Factory;
use MoaMaster\Domain\Models\ProductUpdate;

class ProductUpdateService implements IRestService{

    public function get($id){
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

    public function find($parameters = array()){
        if(isset($parameters["getLast10"])){
            $results = array();
            $product_updates = ProductUpdate::where("is_processed", "=",0)->limit(10)->get();
            foreach ($product_updates as $product_update){
                $result =Factory::createModel("ProductUpdate", $product_update);
                array_push($results, $result);
            }
            return $results;
        }else{
            return null;
        }
    }

    public function post($data)
    {
        // TODO: Implement post() method.
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