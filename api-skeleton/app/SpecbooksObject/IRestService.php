<?php
/**
 * Created by PhpStorm.
 * User: specbooks
 * Date: 10/23/18
 * Time: 10:46 AM
 */

namespace SpecbooksObject;


interface IRestService{

    public function get($id);
    public function getAll();
    public function getChildren($id, $childClass);
    public function getByForeignKey($fk, $foreignClass);
    public function find($parameters = array());
    public function post($data);
    public function put($data);
    public function patch($data);
    public function delete($id);

}