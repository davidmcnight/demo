<?php
/**
 * Created by PhpStorm.
 * User: specbooks
 * Date: 10/23/18
 * Time: 10:44 AM
 */

namespace SpecbooksObject;


interface IRestModel{

    public function setAttributes($parameters = array());
    public function convertToDbObject();
    public function toJSONObject();
    public function toString();


}