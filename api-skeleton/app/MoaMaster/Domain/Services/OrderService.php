<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/19/18
 * Time: 2:01 PM
 */
namespace MoaMaster\Domain\Services;
use MedalsObject\IRestService;
use MoaMaster\Domain\Factory\Factory;
use MoaMaster\Domain\Models\BillingAddress;
use MoaMaster\Domain\Models\Order;

class OrderService implements IRestService{

    public function get($id){
        $order = Order::with("billing_address")
            ->with("shipping_address")
            ->with("line_items")
            ->with("payment")
            ->where("id", $id)->get()->first();
        $order = Factory::createModel("Order", $order);
        return $order;
    }

    public function getAll()
    {
        // TODO: Implement getAll() method.
    }

    public function getChildren($id, $childClass)
    {
        // TODO: Implement getChildren() method.
    }

    public function getByForeignKey($fk, $foreignClass){
        // TODO: Implement getByForeignKey() method.
    }

    public function find($parameters = array()){
        //get by
        if(isset($parameters["getPiOrder"])){
            $order = Order::with("billing_address")
                ->with("shipping_address")
                ->with("line_items")
                ->with("payment")
                ->where("web_order_id", $parameters["order_id"])
                ->where("site_id", "pi")
                ->get()
                ->first();
            return $order;
        }

    }

    public function post($data){

        if(isset($data["site_id"])){
            $order = $data["order"];
            if($data["site_id"] == "moa"){
                $order = $this->createMoaOrder($order);
                return $order;
            }elseif($data["site_id"] == "pi"){
                $order = $this->createPatriotIssueOrder($order);
                return $order;
            }
        }else{
            return array("success"=> false, "message"=> "Please specify site_id");
        }
        return array("success"=> false, "message"=> "something happened trying to post an order to the database");
    }

    public function put($data){

        // TODO: Implement put() method.
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }

    public function delete($id){
        //get the order
        $order = $this->get($id);
        if($order->site_id == "pi"){
            $this->deletePatriotIssueOrder($order);
        }

    }

    private function createMoaOrder(Order $order){
        $order->save();
        $order->billing_address()->save($order->billing_address);
        $order->shipping_address()->save($order->shipping_address);
        return $order;
    }





    private function createPatriotIssueOrder(Order $order){
        $order->save();
//        $order->billing_address()->save($order->billing_address);
//        $order->shipping_address()->save($order->shipping_address);
//        $order->line_items()->saveMany($order->line_items);
//        $order->payment()->save($order->payment);
        return $order;
    }

    private function deletePatriotIssueOrder(Order $order){
        echo "HERE at delete";
        $order = Order::with("billing_address")
            ->with("shipping_address")
            ->with("line_items")
            ->with("payment")
            ->where("id", $order->id)->get()->first();
        $order->destroy($order->id);

//        printNicely($order);
        $order->delete();

//        $order->delete();
        //printNicely($moa_order);
//        printNicely($moa_order);
//        $order->delete();

//        $order->billing_address()->delete();
//        $order->shipping_address()->delete();
//        $order->line_items()->saveMany($order->line_items);
//        $order->payment()->save($order->payment);
    }


}