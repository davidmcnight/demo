<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/27/18
 * Time: 5:19 PM
 */



use MoaMaster\Domain\Services\ProductUpdateService;
use MoaMaster\Domain\Factory\Factory as MoaFactory;
use Magento\Domain\Factory\Factory as MagentoFactory;



$app->get(
    '/api/v1/integrations/pricing_update/:sku/:price',
    function ($sku, $price) use ($app) {
        try{

        $token = "218adyips5c4ls290f2lq5yhdsov9qek";
        $url =  "https://www.medalsofamerica.com/rest/default/V1/products/" . $sku;
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
        curl_setopt($ch, CURLOPT_HEADER, false);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array("Content-Type: application/json", "Authorization: Bearer " . $token));
        $data = array("product" => array("price" => $price));
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        $result = curl_exec($ch);
        var_dump($result);
        }catch (Exception $e){
            $e->getMessage();
        }
});


$app->get(
    '/api/v1/integrations/product_update',
    function () use ($app) {

        // MoA

        //init product update service
        $product_update_service = MoaFactory::createService("ProductUpdate");

        //magento product service
        $magento_product_service = MagentoFactory::createService("Product");

        //get product updates
        $params = array("getLast10" =>1);
        $product_updates = $product_update_service->find($params);

        foreach ($product_updates as $product_update){
            $params = array("doesProductExistDb"=>1, "sku" =>$product_update->sku);
            if($magento_product_service->find($params)){
                echo $product_update->sku . " exists in Magento.";
                //start the process here

                //check if discontinued -- if so just turn it off and move
                if($product_update->discontinued == 1){
                    /* PRODUCT IS DISCONTINUED */

                }else{
                    //check closeout
                    if($product_update->close_out == 1){
                        /* PRODUCT IS ON CLOSEOUT */


                        //check the list_price and price_code_1
                        if($product_update->list_pice == $product_update->price_code_1){
                            //check if on-demand

                        }else{
                            //move product to the sale section


                        }

                    }else{
                        //check if on-demand

                    }
                }
            }else{
                echo $product_update->sku . " does not exist in Magento. Skipping.";
            }
            echo '<br>';
            echo '---------------------';
            echo '<br>';
            echo '<br>';
            echo '<br>';
        }



        //TODO: PI product update
    });
