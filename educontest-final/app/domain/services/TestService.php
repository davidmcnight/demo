<?php

/**
 * Created by PhpStorm.
 * User: Davey
 * Date: 1/22/17
 * Time: 9:27 AM
 */
class TestService implements IRestService{
    public function get($id)
    {
        // TODO: Implement get() method.
    }

    public function getAll()
    {
        // TODO: Implement getAll() method.
    }

    public function post($data)
    {
        if(isset($data["createSchoolTest"])){
            $schoolTest = Factory::createModel("SchoolTest", $data);
//            printNicely($schoolTest);
            $schoolTest->save();
        }
    }

    public function put($data)
    {
        // TODO: Implement put() method.
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }

    public function delete($id){

    }

    public function find($parameters = array()){

        if(isset($parameters["deleteSchoolTest"])){
            $data = json_decode($parameters["school_test"]);
            $st = SchoolTest::find($data->id);
            $st->delete();
            return $st;
        }

        if(isset($parameters["getSchoolTestLists"])){
            $params = array('getActiveYear' => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $allTests = Test::with("grade")
                ->where("year_id", $activeYear->id)
                ->get();
            $signedUpTests = SchoolTest::with("test")->with("test.grade")
                ->with("school_year")

//                ->where("test.year_id", $activeYear->id)
                ->where("school_test.school_year_id", $parameters["school_year_id"])
                ->get();

            $signedUpKeys = array();

            //GET KEYS
            foreach ($signedUpTests as $su){
                array_push($signedUpKeys, $su->test->id);
            }


            //Compare and add keys not in array
            $notSignedUp = array();


            foreach ($allTests as $ac){

                if(in_array($ac->id, $signedUpKeys)){
                    continue;
                }else{
                    array_push($notSignedUp, $ac);
                }
            }

//
            //package arrays and return
            $testsLists = array(
                "signedUpTest" => $signedUpTests,
                "notSignedUpTest" =>$notSignedUp);
            return $testsLists;
        }
    }

    public function getByParent($parent_id)
    {
        // TODO: Implement getByParent() method.
    }


}