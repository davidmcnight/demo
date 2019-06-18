<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/29/16
 * Time: 1:11 PM
 */


interface IRestService{
    
    //REST
    public function get($id);
    public function getAll();
    public function post($data);
    public function put($data);
    public function patch($data);
    public function delete($id);
    
    //OTHER
    public function find($parameters = array());
    public function getByParent($parent_id);
    
    
    

}