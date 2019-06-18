<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/22/18
 * Time: 4:12 PM
 */




use PatriotIssue\Domain\Factory\Factory as PIFactory;
use MoaMaster\Domain\Factory\Factory as MoaFactory;
use  MedalsObject\Helpers\DataHelper;
$app->get(
    '/api/v1/patriot_issue/interface',
    function () use ($app) {

        $current_id  = 0;

        try{

            /*
            The process here is simple. We are bringing orders down from BigCommerce and creating them in our
            Patriot Issue name space. We are then using the IRestModel interface which in turn will always interface
            with moa master. The interface has a convert to DB object which returns an array (manipulated by DataHelper
            to return and object) which interfaces with the MoaMaster namespace. This namespace interacts directly with
            the database. It uses the Eloquent orm to do so. All objects apart of this namespace will have a moa_prefix.
            Steps:
            1. Get orders through api that are Awaiting Fufillment.
            2. Loop through orders.
            3. Create PI.Domain objects
            4. Interface with MoaMaster
            5. Insert into DB
            6. Log
            7. Update Status
            8. Echo message
            */




            //create order service
            $pi_order_service = PIFactory::createService("Order");
            $moa_order_service = MoaFactory::createService("Order");
            $order_log_service = MoaFactory::createService("OrderLog");

            $pi_order_service->put(array("updateOrderStatus"=> 1, "order_id"=> 167, "status_id" => 11));

            //get order from pi
            $results = $pi_order_service->find(array("getPendingOrders"=>1));

            if(isset($results)){
                foreach ($results as $result){

                    $current_id = $result->id;

                    //order header
                    $order = PIFactory::createModel("Order", $result);
                    $moa_order = MoaFactory::createModel("Order",
                        DataHelper::makeObject($order->convertToDbObject()
                        ));

                    //billing
                    $billing_address = PIFactory::createModel("BillingAddress", $result);
                    $moa_billing_address = MoaFactory::createModel("BillingAddress",
                        DataHelper::makeObject($billing_address->convertToDbObject()
                        ));
                    $moa_order->setBillingAddress($moa_billing_address);

                    // shipping
                    $shipping_address = PIFactory::createModel("ShippingAddress", $result->shipping_addresses[0]);
                    $moa_shipping_address = MoaFactory::createModel("ShippingAddress",
                        DataHelper::makeObject($shipping_address->convertToDbObject()
                        ));
                    $moa_order->setShippingAddress($moa_shipping_address);

                    //line items
                    $moa_line_items = array();

                    //loop through and insert in an array
                    foreach ($result->products as $product){
                        $line_item = PIFactory::createModel("LineItem", $product);
                        $moa_line_item = MoaFactory::createModel("LineItem", DataHelper::makeObject($line_item->convertToDbObject()));
                        array_push($moa_line_items, $moa_line_item);
                    }
                    $moa_order->setLineItems($moa_line_items);

                    //payment
                    $payment = PIFactory::createModel("Payment", $result);
                    $moa_payment = MoaFactory::createModel("Payment",
                        DataHelper::makeObject($payment->convertToDbObject())
                    );
                    $moa_order->setPayment($moa_payment);

                    //insert into the db
                    $moa_order = $moa_order_service->post(array("site_id"=>"pi", "order"=>$moa_order));

                    // change status of order
                    $pi_order_service->put(array("updateOrderStatus"=> 1, "order_id"=> $result->id, "status_id" => 10));

                    //create log
                    $order_log = $order_log_service->post(DataHelper::makeObject(array(
                        "site_id" => "pi",
                        "web_order_id" => $result->id,
                        "success" => true,
                        "message" => "Order successfully created.",
                        "is_processed" => 1,
                        "issue_resolved" => 1,
                    )));

                    //echo message
                    echo json_encode(array("success" => true, "site_id" => "pi", "message" => "Order successfully created", "order_id" => $moa_order->id));

                }
            }else{
                echo "No orders in the system.";
            }

        }catch (Exception $e) {

            $moa_order_service = MoaFactory::createService("Order");
            $order_log_service = MoaFactory::createService("OrderLog");
            $pi_order_service = PIFactory::createService("Order");



            $order_log = $order_log_service->post(DataHelper::makeObject(array(
                "site_id" => "pi",
                "web_order_id" => $current_id,
                "success" => false,
                "message" => $e->getMessage(),
                "is_processed" => 0,
                "issue_resolved" => 0,
                "line" => $e->getLine(),
                "trace" => $e->getTraceAsString(),
            )));

            $pi_order_service->put(array("updateOrderStatus"=> 1, "order_id"=> $current_id, "status_id" => 12));

            $moa_order  = $moa_order_service->find(array("getPiOrder"=>1, "order_id"=>$current_id));
            $moa_order_service->delete($moa_order->id);

        }
    }
);
