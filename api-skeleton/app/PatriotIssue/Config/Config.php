<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/22/18
 * Time: 4:27 PM
 */
namespace PatriotIssue\Config;

class Config{

    public static $store_url = "https://store-z6entsr29p.mybigcommerce.com";
    public static $username = "davey";
    public static $api_key = "780643d7f56978250da36c000f1ff8e45788f3fc";

    public static $payment_provider = "square";

    public static function getConfig() {
        return  array(
            'store_url' => Config::$store_url,
            'username'  => Config::$username,
            'api_key'   => Config::$api_key
        );
    }

}