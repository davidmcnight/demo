<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 3/22/18
 * Time: 12:49 PM
 */



ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once "fpdf.php";
require_once "../config/Config.php";

function printNicely($value){
    print nl2br(print_r($value, true));
}

include "../vendor/autoload.php";
include "../app/domain/models/IEduObject.php";
include "../app/domain/models/Year.php";
include "../app/domain/models/League.php";
include "../app/domain/models/School.php";
include "../app/domain/models/Grade.php";
include "../app/domain/models/BillingAddress.php";
include "../app/domain/models/ShippingAddress.php";
include "../app/domain/models/Competition.php";
include "../app/domain/models/SchoolCompetitionPDF.php";
include "../app/domain/models/SchoolTest.php";
include "../app/domain/models/SchoolYear.php";
include "../app/domain/models/Test.php";


//
$settings = array(
    'driver' => 'mysql',
    'host' => '127.0.0.1',
    'database' => 'educontest_test',
    'username' => 'root',
    'password' => 'root',
    'collation' => 'utf8_general_ci',
    'prefix' => ''
);

//$settings = array(
//    'driver' => 'mysql',
//    'host' => '127.0.0.1',
//    'database' => 'educonte_edu',
//    'username' => 'educonte_edu',
//    'password' => 'L]]ZTf#5%0Jd',
//    'collation' => 'utf8_general_ci',
//    'prefix' => ''
//);

require "makefont/makefont.php";


$container = new Illuminate\Container\Container;
$connFactory = new \Illuminate\Database\Connectors\ConnectionFactory($container);
$conn = $connFactory->make($settings);
$resolver = new \Illuminate\Database\ConnectionResolver();
$resolver->addConnection('default', $conn);
$resolver->setDefaultConnection('default');
\Illuminate\Database\Eloquent\Model::setConnectionResolver($resolver);

$current_id = 0;
$current_student  = "";

try{
    $db = new PDO(Config::DSN, Config::DB_USERNAME, Config::DB_PASSWORD);
    $query = "SELECT student.name, (student.test1_score + student.test2_score + student.test3_score + student.test4_score) as total_score, grade.id as grade_id, grade.name as grade, school.name as school_name, school.id as school_id
    FROM student
    INNER JOIN school_competition on student.school_competition_id = school_competition.id
    INNER JOIN competition on competition.id = school_competition.competition_id
    INNER JOIN grade on grade.id = competition.grade_id
    INNER JOIN school_year on school_competition.school_year_id = school_year.id
    INNER JOIN school on school_year.school_id = school.id
    INNER JOIN year on year.id = school_year.year_id
    WHERE year.id = 11
    AND school.id = 6825
    ORDER BY school.name, grade.id, total_score DESC;";






    $statement = $db->query($query);
    $results = $statement->fetchAll(PDO::FETCH_ASSOC);
//    printNicely($results);
    $db = null;

    $final_students = array();

    $all_students = array();


    $prev_score =0;
    $score_counter = 1;


    foreach ($results as $student){

        $current_id = $student["school_id"];
        $current_student = $student;
        $school_id = $student["school_id"];
        $grade_id = $student["grade_id"];
        $total_score = $student["total_score"];


        //check if school is part of the array if not add
        if(!isset($final_students[$school_id])){
            $final_students[$school_id] = array();
        }

        //check if school grade is in
        if(!array_key_exists($grade_id, $final_students[$school_id])){
            $final_students[$school_id][$grade_id] = array();
        }

        if(sizeof($final_students[$school_id][$grade_id]) == 0){
            $student["place"] = "First Place";
            //add student
            array_push($final_students[$school_id][$grade_id], $student);
            array_push($all_students, $student);
        }else

        if(sizeof($final_students[$school_id][$grade_id]) == 1){
            if($final_students[$school_id][$grade_id][0]["total_score"] == $total_score){
                $student["place"] = "First Place";
                //add student
                array_push($final_students[$school_id][$grade_id], $student);
                array_push($all_students, $student);
            }else{
                $student["place"] = "Second Place";
                //add student
                array_push($final_students[$school_id][$grade_id], $student);
                array_push($all_students, $student);
            }
        }else

        if(sizeof($final_students[$school_id][$grade_id]) == 2){
            if($final_students[$school_id][$grade_id][1]["total_score"] == $total_score){
                if($final_students[$school_id][$grade_id][0]["total_score"] == $total_score){
                    $student["place"] = "First Place";
                    //add student
                    array_push($final_students[$school_id][$grade_id], $student);
                    array_push($all_students, $student);
                }else{
                    $student["place"] = "Second Place";
                    //add student
                    array_push($final_students[$school_id][$grade_id], $student);
                    array_push($all_students, $student);
                }
            }else{
                $student["place"] = "Third Place";
                //add student
                array_push($final_students[$school_id][$grade_id], $student);
                array_push($all_students, $student);
            }
        }else


        if(sizeof($final_students[$school_id][$grade_id]) == 3){

            if($final_students[$school_id][$grade_id][2]["total_score"] == $total_score){
                if($final_students[$school_id][$grade_id][1]["total_score"] == $total_score){
                    if($final_students[$school_id][$grade_id][0]["total_score"] == $total_score){
                        $student["place"] = "First Place";
                        //add student
                        array_push($final_students[$school_id][$grade_id], $student);
                        array_push($all_students, $student);
                    }else{
                        $student["place"] = "Second Place";
                        //add student
                        array_push($final_students[$school_id][$grade_id], $student);
                        array_push($all_students, $student);
                    }
                }else{
                    $student["place"] = "Third Place";
                    //add student
                    array_push($final_students[$school_id][$grade_id], $student);
                    array_push($all_students, $student);
                }
            }

        }else

        if(sizeof($final_students[$school_id][$grade_id]) == 4){
            //if same as for person if not skip
            if($final_students[$school_id][$grade_id][3]["total_score"] == $total_score){

                if($final_students[$school_id][$grade_id][2]["total_score"] == $total_score){
                    if($final_students[$school_id][$grade_id][1]["total_score"] == $total_score){
                        if($final_students[$school_id][$grade_id][0]["total_score"] == $total_score){
                            $student["place"] = "First Place";
                            //add student
                            array_push($final_students[$school_id][$grade_id], $student);
                            array_push($all_students, $student);
                        }else{
                            $student["place"] = "Second Place";
                            //add student
                            array_push($final_students[$school_id][$grade_id], $student);
                            array_push($all_students, $student);
                        }
                    }else{
                        $student["place"] = "Third Place";
                        //add student
                        array_push($final_students[$school_id][$grade_id], $student);
                        array_push($all_students, $student);
                    }

                }
            }
        }
    }



    $pdf = new FPDF();

    $pdf->AddFont("Canterbury-Regular", "", "Canterbury.php");
//    printNicely($final_students);
//    exit;



    foreach ($all_students as $student){

        $total_score  = $student["total_score"];
        $pdf->AddPage("L");
        $pdf->SetAutoPageBreak(false);
        $pdf->SetLeftMargin(0);
        $pdf->SetTopMargin(0);
        $pdf->SetRightMargin(0);


        //TOP LINE
        $pdf->SetX(0);
        $pdf->SetY(0);
        $pdf->SetFont('Canterbury-Regular','',48);
        $pdf->Cell(0, 100, "Certificate of Merit", 0, 0, "C");

        //message
        $pdf->SetX(0);
        $pdf->SetY(0);
        $pdf->SetFont('Times','',16);
        $pdf->Cell(0,155, "THIS CERTIFICATE HAS BEEN AWARDED TO", 0, 0, "C");



        //student name//
        $pdf->SetX(0);
        $pdf->SetY(0);
        $pdf->SetFont('Times','BI',21);
        $pdf->Cell(0,180, $student["name"],0, 0, "C");



        //another message
        $pdf->SetX(0);
        $pdf->SetY(0);
        $pdf->SetFont('Times','',16);
        $pdf->Cell(0,205, "IN RECOGNITION OF OUTSTANDING MERIT FOR",0, 0, "C");

        //school name
        $pdf->SetX(0);
        $pdf->SetY(0);
        $pdf->SetFont('Times','B',21);
        $pdf->Cell(0, 230, $student["school_name"],0, 0, "C");


        //do the ranking logic

        //grade
        $pdf->SetY(0);
        $pdf->SetX(0);
        $pdf->SetFont('Times','B',21);
        $pdf->Cell(0, 250, $student["grade"] . " ". $student["place"] , 0 ,0,"C");

        //date
        $pdf->SetY(0);
        $pdf->SetX(0);
        $pdf->SetFont('Times','I',16);
        $pdf->Cell(0, 275,"Given This 14th Day Of May, 2018", 0 ,0,"C");


        $pdf->SetY(0);
        $pdf->SetX(230);
        $pdf->SetFont('Times','I',12);
        $pdf->Cell(20, 310,"Richard S. Pidcock");

        $pdf->SetY(0);
        $pdf->SetX(238);
        $pdf->SetFont('Times','I',12);
        $pdf->Cell(20, 317,"MathFax");

        $prev_score  = $student["total_score"];


    }

    $pdf->Output();






}catch (Exception $e){
    echo $e->getTraceAsString();
    echo $e->getMessage();
    printNicely($current_id);
    printNicely($current_student);
}


































