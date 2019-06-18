<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 12/20/16
 * Time: 10:52 AM
 */

use Illuminate\Support\Collection as BaseCollection;
use Illuminate\Support\Facades\DB;
class CompetitionService implements IRestService

{
    public function get($id){
        $competitions = Competition::with("grade")
            ->with("year")->where("id", $id)
            ->get()->first();
        return $competitions;
    }

    public function getAll(){
        $competitions = Competition::with("grade")
            ->with("year")->get();
        return $competitions;
    }

    public function post($data){
        // TODO: Implement post() method.
        if(isset($data["createSchoolCompetition"])){
            $schoolYear = Factory::createModel("SchoolCompetition", $data);
            $schoolYear->save();
        }
    }

    public function put($data){

        if(isset($data["updateSchoolCompetitionPublic"])){
            try{
                $sc1 = Factory::createModel("SchoolCompetition", $data["school_competition"]);
                $record = SchoolCompetition::where("id", $sc1->id)->first();
                printNicely($record);
                $params = array("public" => $sc1->public);
                $record->update($params);
            }catch (Exception $e){
                echo  $e->getMessage();
            }
        }


        if(isset($data["updateDivision"])){
            try{
                $sc1 = Factory::createModel("SchoolCompetition", $data["school_competition"]);
                $record = SchoolCompetition::where("id", $sc1->id)->first();
                $record->update($sc1->toArray());
            }catch (Exception $e){
                echo  $e->getMessage();
            }
        }
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }

    public function delete($id)
    {
        if(isset($data["createSchoolCompetition"])) {
            
        }
    }

    public function diffCollection(Collection $collection)
    {
        $diff = new static;
        foreach ($this->items as $item)
        {
            if ( ! $collection->contains($item->getKey()))
            {
                $diff->add($item);
            }
        }
        return $diff;
    }


    public function getDivision($state, $grade, $league_id){
        $db = new PDO(Config::DSN, Config::DB_USERNAME, Config::DB_PASSWORD);
        $params["getActiveYear"] = 1;
        $activeYear = Factory::createService("Year")->find($params);
        $div_query = "SELECT count(school_competition.division) as div_count, shipping_address.state, school_competition.division
                    FROM school_competition
                    INNER JOIN competition ON competition.id = school_competition.competition_id
                    INNER JOIN school_year ON school_competition.school_year_id = school_year.id
                    INNER JOIN school ON school.id = school_year.school_id
                    INNER JOIN shipping_address ON school.id = shipping_address.school_id
                    INNER JOIN grade ON competition.grade_id = grade.id
                    WHERE  competition.grade_id = " . $grade . "
                     AND school_year.year_id = " . $activeYear->id . "
                    AND shipping_address.state = '" . $state . "'" . "AND school_competition.division IS NOT NULL";


        if(!$league_id == 0){
            $div_query = $div_query . " AND school.league_id = = '" . $state . "'";
        }
        $div_query = $div_query = " ORDER BY div_count DESC";


        $div_statement = $db->query($div_query);
        $div_results = $div_statement->fetchAll(PDO::FETCH_ASSOC);

        $db = null;

        $myDivisions = array();
        if($div_results){
            foreach ($div_results as $div_result){
                array_push($myDivisions, $div_result["division"]);
            }

            return $myDivisions;
        }else{
            return null;
        }



    }


    public function find($parameters = array()){
        //get school competition
        if (isset($parameters["schoolResults"])) {

            $filters = json_decode($parameters["filters"]);

            //get data
            $league_id = $filters->league_id;
            $grade_id = $filters->grade_id;
            $params = array('getPublic' => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $schoolCompetitions = SchoolCompetition
                ::with("school_year")
                ->with("school_year.school")
                ->with("school_year.school.shipping_address")
            ->whereHas("competition",
                function ($query) use ($activeYear){
                    $query->where("competition.year_id", "=", $activeYear->id);
            })->whereHas("competition.grade",
                function ($query) use($grade_id){
                $query->where("grade.id", "=", $grade_id);
            })->whereHas("school_year.school",
                    function ($query) use($league_id){
                        $query->where("school.league_id", "=", $league_id);
            });;


            //check if state given -- if not -- get data
            if($filters->state != "0") {


                if($league_id == 1 || $league_id == 2){
                    $divisions = $this->getDivision($filters->state, $filters->grade_id, $league_id);
                    $counter = 0;
                }else{
                    $divisions = $this->getDivision($filters->state, $filters->grade_id, 0);
                    $counter = 0;
                }


                if(sizeof($divisions) == 1){
                    $schoolCompetitions = $schoolCompetitions->where(
                        function ($query) use($divisions){
                            $query->where("division", "=", $divisions[0]);
                        });;
                }else{
                    $schoolCompetitions = $schoolCompetitions->where(
                        function ($query) use($divisions){
                            $query->where("division", "=", $divisions[0])
                            ->orWhere("division", "=", $divisions[1]);

                        });;
                }

            }else{

                $schoolCompetitions = $schoolCompetitions->get();;
            }



            //get student scores
            $results = array();
            foreach ($schoolCompetitions as $sc){
                $test_1 = array();
                $test_2 = array();
                $test_3 = array();
                $test_4 = array();
                foreach ($sc->students as $s){
                    array_push($test_1, $s->test1_score);
                    array_push($test_2, $s->test2_score);
                    array_push($test_3, $s->test3_score);
                    array_push($test_4, $s->test4_score);
                }
                //sort top to bottom
                rsort($test_1);
                rsort($test_2);
                rsort($test_3);
                rsort($test_4);
                // sum up top 3
                $result = array(
                    "score_1" =>array_sum(array_slice($test_1, 0, 3)),
                    "score_2" =>array_sum(array_slice($test_2, 0, 3)),
                    "score_3" =>array_sum(array_slice($test_3, 0, 3)),
                    "score_4" =>array_sum(array_slice($test_4, 0, 3)),
                );

                $result["total"] = $result["score_1"] + $result["score_2"] + $result["score_3"] + $result["score_4"];

                if($sc->public == 1){
                   $result["school_name"] = $sc->school_year->school->name;
                }else{
                    $result["school_name"] = "School #" .$sc->school_year->school->id;
                };

                $result["city_state"] = $sc->school_year->school->shipping_address->city . ", " .  $sc->school_year->school->shipping_address->state;
                array_push($results, $result);
            }
            usort($results, 'sortByOrder');
            $results = array_reverse($results);

            $rank = 1;
            $final = array();
            foreach ($results as $r){
                $r["score_rank"] = $rank;
                $rank ++;
                array_push($final, $r);
            }

            return $final;

        }

        if(isset($parameters["deleteSchoolCompetition"])){
            $data = json_decode($parameters["school_competition"]);
            $st = SchoolCompetition::find($data->id);
            $st->delete();
            return $st;
        }

        if(isset($parameters["getSchoolsForDivision"])){
            $params = array('getActiveYear' => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $schoolCompetitions = SchoolCompetition
                ::join("competition", "school_competition.competition_id", "=", "competition.id")
                ->join("grade", "grade.id", "=", "competition.grade_id")
                ->join("school_year", "school_competition.school_year_id", "=", "school_year.id")
                ->join("school", "school_year.school_id", "=", "school.id")
                ->join("shipping_address", "school.id", "=", "shipping_address.school_id")
                ->join("league", "league.id", "=", "school.league_id")
                ->select(
                    "school_competition.id","school.id as school_id", "school.name as school", "shipping_address.state","grade.name as grade",
                    "school_competition.division", "league.description as league"
                )
                ->where([
                    ["competition.year_id", $activeYear->id], ["league.id", $parameters["league_id"] ]
                ])
                ->orderBy("school_competition.competition_id", "ASC")
                ->orderBy("shipping_address.state", "ASC")
                ->get();

            return $schoolCompetitions;
        }


        if(isset($parameters["getSchoolCompetition"])){
            $schoolCompetition = SchoolCompetition::where("id", $parameters["school_competition_id"])
                ->with("school_year")->with("competition")->with("school_year.school")->with("school_year.year")
                ->with("competition.grade")
                ->with("students")
            ->get()->first();
           return $schoolCompetition;
        }
        //get registered classes -- pass school and 
        if(isset($parameters["getRegisteredClasses"])) {
            $params = array('getActiveYear' => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $schoolYear = SchoolYear::where("school_id", $parameters["school_id"])
                ->where("year_id". $activeYear);
            return  SchoolCompetition::with("grade")
                ->where("school_year", $schoolYear->id)
                ->get();
        }
        
        //get both lists -- signed up and not signed up
        if(isset($parameters["getSchoolCompetitionLists"])){
            $params = array('getActiveYear' => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $allCompetitions = Competition::with("grade")
                ->where("year_id", $activeYear->id)
                ->get();
            $signedUpCompetitions= SchoolCompetition::with("competition")->with("competition.grade")
                ->with("school_year")

//                ->where("test.year_id", $activeYear->id)
                ->where("school_competition.school_year_id", $parameters["school_year_id"])
                ->get();

            $signedUpKeys = array();

            //GET KEYS
            foreach ($signedUpCompetitions as $su){
                array_push($signedUpKeys, $su->competition->id);
            }


            //Compare and add keys not in array
            $notSignedUp = array();


            foreach ($allCompetitions as $ac){

                if(in_array($ac->id, $signedUpKeys)){
                    continue;
                }else{
                    array_push($notSignedUp, $ac);
                }
            }

//
            //package arrays and return
            $competitionLists = array(
                "signedUpCompetition" => $signedUpCompetitions,
                "notSignedUpCompetition" =>$notSignedUp);
            return $competitionLists;
        }

    }

    public function getByParent($parent_id)
    {
        // TODO: Implement getByParent() method.
    }


}