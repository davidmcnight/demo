<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/23/18
 * Time: 11:30 AM
 */

namespace PatriotIssue\Domain\Services;

use \MedalsObject\IRestService;
use PatriotIssue\Config\Config as C;
use Bigcommerce\Api\Client as Bigcommerce;
use PatriotIssue\Domain\Factory\Factory;
use PatriotIssue\Domain\Models\Order;
use PatriotIssue\Domain\Factory\Factory as PIFactory;


class OrderService implements IRestService{

    public function get($id){
        //get configure
        Bigcommerce::configure(C::getConfig());
        //strip ssl
        Bigcommerce::verifyPeer(false);
        //get data
        $data = Bigcommerce::getOrder($id);
        //instantiate object and set attributes
        return $data;

    }

    public function getAll(){
        //get configure
        Bigcommerce::configure(C::getConfig());
        //strip ssl
        Bigcommerce::verifyPeer(false);
        //get data
        $data = Bigcommerce::getOrders(array("limit" =>"250"));
        $orders = array();
        foreach ($data as $datum){
            $order = Factory::createModel("Order", $datum);
            array_push($orders, $order);
        }
       return $orders;
    }


    public function find($parameters = array()){
        if(isset($parameters["getPendingOrders"])){
            $params = array("status_id" => 11);
            Bigcommerce::configure(C::getConfig());
            Bigcommerce::verifyPeer(false);
            $orders = Bigcommerce::getOrders($params);
            return $orders;
        }
        if(isset($parameters["getLastOrder"])){
            Bigcommerce::configure(C::getConfig());
            Bigcommerce::verifyPeer(false);
            $order = Bigcommerce::getOrders(array("sort"=>"id:desc", "limit"=>1) );
            return $order;
        }else{
            return array("success"=>false, "message"=>"No routines match the parameters given. Try again.");
        }

    }

    public function post($data)
    {
        // TODO: Hold off for now
    }

    public function put($data){
        if(isset($data["updateOrderStatus"])){
            //update order status
            Bigcommerce::configure(C::getConfig());
            Bigcommerce::verifyPeer(false);
            $fields = array("status_id" => $data["status_id"]);
            Bigcommerce::updateOrder($data["order_id"], $fields);
        }
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }

    public function delete($id)
    {
        // TODO: Implement delete() method.
    }


    public function getChildren($id, $childClass){
        // TODO: LOOK at Order Products maybe?
    }

    public function getByForeignKey($fk, $foreignClass){
        // TODO: Implement getByForeignKey() method.
    }
}