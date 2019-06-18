<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 5/4/18
 * Time: 10:53 AM
 */

use Magento\Domain\Services\OrderService;
use Magento\Domain\Factory\Factory as MagentoFactory;
use MoaMaster\Domain\Factory\Factory as MoaFactory;
use Magento\Helpers\ProductUtility;



$app->get(
    '/api/v1/moa/interface',
    function () use ($app) {

        echo '<pre>';
        $order_service = MagentoFactory::createService("Order");
        $moa_order_service = MoaFactory::createService("Order");
        $order_log_service = MoaFactory::createService("OrderLog");

        $order_result = $order_service->get(1070395);

        //order header
        $order = MagentoFactory::createModel("Order", $order_result);
        $moa_order =  MoaFactory::createModel("Order", $order->convertToDbObject());

        //billing address
        $billing_address = MagentoFactory::createModel("BillingAddress", $order_result);
        $moa_billing_address = MoaFactory::createModel("BillingAddress", $billing_address->convertToDbObject());
        $moa_order->setBillingAddress($moa_billing_address);

        //shipping address
        $shipping_address = MagentoFactory::createModel("ShippingAddress", $order_result->extension_attributes->shipping_assignments[0]->shipping->address);
        $moa_shipping_address = MoaFactory::createModel("ShippingAddress", $shipping_address->convertToDbObject());
        $moa_order->setShippingAddress($moa_shipping_address);

        //order line items
        $custom_items_bunch = json_decode($order_result->custom_items_bunch);
        //get custom items bunch
        $filters = array();
        //array for line items to be created
        $line_items = array();

        // we will pass filters through the factory so it will know what type of line item to create
        foreach ($custom_items_bunch as $item){
            //check if builder -- uses Product Utility to determine the type
            if(ProductUtility::is_builder($item)){
                //there are multiple line_items associated with a builder we will first create the base item
                //create base for builder
                $filters["type"] = "builder_base";
                $filters["item"] = $item;
                $line_item = MagentoFactory::createModel("LineItem", $filters);
                $moa_line_item = MoaFactory::createModel("LineItem", $line_item->convertBuilderBase());
                printNicely($moa_line_item);
            }


//            if(ProductUtility::is_artifi())//

            //check if configurable



        }
        //printNicely($custom_items_bunch);

        //create order
//        $moa_order = $moa_order_service->post(array("site_id"=>"moa", "order"=>$moa_order));

        echo json_encode(array("success"=>true, "web_order_id" => $moa_order->web_order_id, "moa_master_id" => $moa_order->id));

    }
);
