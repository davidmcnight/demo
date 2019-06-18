<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 1/17/17
 * Time: 8:44 AM
 */
class StudentService implements IRestService{


    public function get($id)
    {

    }

    public function getAll(){

    }

    public function post($data){
        $student = Factory::createModel("Student", $data["student"]);
        $student->save();
    }

    public function put($data){
        $student = Factory::createModel("Student", $data["student"]);
        $record = Student::where("id", $student->id);
        $record->update($student->toArray());
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }



    public function delete($id){
        printNicely($id);
        $record = Student::find($id);
        $record->delete();
    }

    public function find($parameters = array()){
        try {

            //get students by school competition
            if (isset($parameters['school_competition_id'])) {
                $students = Student::where("school_competition_id", $parameters['school_competition_id'])->get();
                return $students;
            }
            if (isset($parameters["studentResults"])) {

                $filters= json_decode($parameters["filters"]);

                $params = array('getPublic' => 1);
                $activeYear = Factory::createService("Year")->find($params);


                $db = Database::getInstance();



//                $query =  "SET @rank=0";
//                $statement = $db->prepare($query);
//                $statement->execute();
//                @rank:=@rank+1 AS rank,
                $query = "
                  
                    SELECT  student.id, student.name, school.name AS school_name, shipping_address.state, shipping_address.city,
                    student.public, grade.name AS grade,  student.test1_score, student.test2_score,  
                    student.test3_score , student.test4_score, school_competition.public as school_public, school.id as school_id,
                    test1_score + test2_score + test3_score + test4_score  AS total_score
                    FROM student 
                        INNER JOIN school_competition ON student.school_competition_id = school_competition.id
                        INNER JOIN competition ON competition.id = school_competition.competition_id
                        INNER JOIN school_year ON school_competition.school_year_id = school_year.id
                        INNER JOIN school ON school.id = school_year.school_id
                        INNER JOIN shipping_address ON school.id = shipping_address.school_id
                        INNER JOIN grade ON competition.grade_id = grade.id
                    WHERE competition.grade_id = " . $filters->grade_id . " AND school_year.year_id = 11";

                if($filters->state != "0" || $filters->state != 0 ) {
                    $div_query = "SELECT count(school_competition.division) AS div_count, shipping_address.state, school_competition.division 
                    FROM school_competition 
                    INNER JOIN competition ON competition.id = school_competition.competition_id
                    INNER JOIN school_year ON school_competition.school_year_id = school_year.id
                    INNER JOIN school ON school.id = school_year.school_id
                    INNER JOIN shipping_address ON school.id = shipping_address.school_id
                    INNER JOIN grade ON competition.grade_id = grade.id
                    WHERE  competition.grade_id = " . $filters->grade_id . "
                    AND school_year.year_id = 11
                    AND shipping_address.state = '" . $filters->state . "'";
                    if ($filters->league_id != 0) {

                        $div_query = $div_query . " AND school.league_id = " . $filters->league_id . "";
                        $div_query = $div_query . " GROUP BY school_competition.division ORDER BY div_count DESC";
                        $div_statement = $db->query($div_query);
                        $div_results = $div_statement->fetchAll(PDO::FETCH_ASSOC);
                        $division = $div_results[0]["division"];
                        $query = $query . " AND school_competition.division =" . $division;
                        $query = $query . " AND school.league_id = " . $filters->league_id . "";
                        $query = $query . " AND competition.year_id = " . 11 . "";
                        $query = $query . " ORDER BY total_score DESC";
                    } else {
                        $div_query = $div_query . " GROUP BY school_competition.division ORDER BY div_count DESC";
                        $div_statement = $db->query($div_query);
                        $div_results = $div_statement->fetchAll(PDO::FETCH_ASSOC);
                        $division1 = $div_results[0]["division"];
                        $division2 = $div_results[1]["division"];
                        $query = $query . " AND (school_competition.division =" . $division1 . " OR school_competition.division =" . $division2 . ")";
                        $query = $query . " AND competition.year_id = " . 11 . " ";
                        $query = $query . " ORDER BY total_score DESC";
                    }
                }else{
                    if ($filters->league_id != 0) {
                        $query = $query . " AND school.league_id = " . $filters->league_id . "";
                    }else{

                    }
                    $query = $query . " ORDER BY total_score DESC";
                }


                $statement = $db->query($query);
                $results = $statement->fetchAll(PDO::FETCH_ASSOC);
                $counter = 1;
                $rankedResults = array();
                $private_students_per_schools = array();
                foreach ($results as $result){
                    // print_r($result);
                    // exit;
                    if($result["public"] == 0){
                        if(isset($private_students_per_schools[$result["school_id"]])){
                            $private_students_per_schools[$result["school_id"]] =  $private_students_per_schools[$result["school_id"]] + 1;

                        }else{
                            $private_students_per_schools[$result["school_id"]] = 1;
                        }
                        $result["name"] = "Student #" . $private_students_per_schools[$result["school_id"]];
                    }

                    $result["rank"] = $counter;
                    $counter++;
                    array_push($rankedResults, $result);
                }

                return $rankedResults;

            }


            //school.name, school_competition.id

        }catch (Exception $e){
            printNicely($e->getMessage());
        }

    }

    public function getByParent($parent_id){

    }


}

//SELECT count(shipping_address.state) as count, shipping_address.state
//FROM school_competition
//INNER JOIN competition ON competition.id = school_competition.competition_id
//INNER JOIN school_year ON school_competition.school_year_id = school_year.id
//INNER JOIN school ON school.id = school_year.school_id
//INNER JOIN shipping_address ON school.id = shipping_address.school_id
//INNER JOIN grade ON competition.grade_id = grade.id
//WHERE school_competition.division = 1
//AND competition.grade_id = 3
//GROUP BY shipping_address.state
