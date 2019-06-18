<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/29/16
 * Time: 4:26 PM
 */
class LeagueService implements IRestService  {
    public function get($id){
        // TODO: Implement get() method.
    }

    public function getAll(){
       return League::all();
    }

    public function post($data){
    }

    public function put($data){
    }

    public function patch($data){
    }

    public function delete($id){

    }
    
    public function find($parameters = array()){

    }

    public function getByParent($parent_id){

    }


}