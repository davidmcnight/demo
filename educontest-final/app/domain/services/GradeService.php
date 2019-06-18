<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/28/17
 * Time: 9:57 AM
 */
class GradeService implements IRestService
{
    public function get($id)
    {
        // TODO: Implement get() method.
    }

    public function getAll(){
        return Grade::all();
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

    public function find($parameters = array())
    {
        // TODO: Implement find() method.
    }

    public function getByParent($parent_id)
    {
        // TODO: Implement getByParent() method.
    }

}