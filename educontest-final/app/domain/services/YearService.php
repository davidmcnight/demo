<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 12/19/16
 * Time: 3:52 PM
 */
class YearService implements IRestService
{
    public function get($id)
    {
        $year = Year::find($id);
        return $year;
    }

    public function getAll(){
        $years = Year::all();
        return $years;
    }

    public function post($data){
       if(isset($data["createSchoolYear"])){



           $schoolYear = Factory::createModel("SchoolYear", $data);

           $db = Database::getInstance();
           $stmt = $db->query("SELECT MAX(invoice_number) as i_n FROM school_year");
           $result = $stmt->fetch(PDO::FETCH_ASSOC);
           $filled_int = sprintf("%04d", $result["i_n"] + 1);
           $schoolYear->invoice_number = $filled_int;
           $schoolYear->save();


           $transport = Swift_SmtpTransport::newInstance('smtp.gmail.com', 465, "ssl")
               ->setUsername(Config::EMAIL_USER)
               ->setPassword(Config::EMAIL_PASSWORD);
           $mailer = Swift_Mailer::newInstance($transport);

           $school = School::where("id", $data['school']['id'])->get()->first();
           
           if ($school->league_id == 1) {
               $message1 = '<html>
                        <body>
                            <div class="col-xs-12">
                                <h2>Thank You for signing up for the 2016-2017 Catholic Math League! </h2>
                            </div>
                            </body>
                        </html>';
           } else {
               $message1 = '<html>
                        <body>
                            <div class="col-xs-12">
                                <h2>Thank You for signing up for the 2016-2017 Educontest! </h2>
                            </div>
                            </body>
                        </html>';
           }

           $message = Swift_Message::newInstance("Welcome " . $school->name)
               ->setFrom(array('davidmcnight@gmail.com' => 'DavidMcNight'))
               ->setTo(array('justin.pidcock@3foldx.com'))
               ->setBody($message1, "text/html");
           $result = $mailer->send($message);



           //send email

       }
    }

    public function put($data){
        if(isset($data["makeAPayment"])) {
            $schoolYear = Factory::createModel("SchoolYear", $data["school_year"]);
            $record = SchoolYear::where("id", $schoolYear->id)->first();
            $record->amount_paid = $record->amount_paid + $data["amount"];
            $record->update($schoolYear->toArray());
        }
        if(isset($data["updateActiveYear"])) {
            //set prev active year to zero
            Year::where("open", 1)->update(['open' => 0]);
            Year::where("id", $data["activeYearId"])->update(['open' => 1]);
        }

        if(isset($data["updateOpenTest"])) {
            $params = array("getActiveYear" => 1);
            $activeYear = Factory::createService("Year")->find($params);
            Year::where("id", $activeYear->id)->update(['openTest' => $data["openTest"]]);
        }

        if(isset($data["updateTestYears"])) {
            $params = array("getActiveYear" => 1);
            $activeYear = Factory::createService("Year")->find($params);
            Year::where("id", $activeYear->id)->update([
                "date_test_1" => $data["test1"],
                "date_test_2" => $data["test2"],
                "date_test_3" => $data["test3"],
                "date_test_4" => $data["test4"]
            ]);
        }
    }

    public function patch($data)
    {
        // TODO: Implement patch() method.
    }

    public function delete($id)
    {
        // TODO: Implement delete() method.
    }

    public function find($parameters = array()){

        if(isset($parameters["schoolsInvoice"])){

            $year= Year::where("open",1)->get()->first();
            $sy = SchoolYear::with("school")->with("school_competitions")->with("school_tests")->with("school.league")
                ->with("school.billing_address")
                ->where("year_id", $year->id)->get();
            return $sy;
        }

        if(isset($parameters["getActiveSchoolYear"])){
            $params = array("getActiveYear" => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $schoolYear = SchoolYear::with("school_competitions")
                ->with("school_tests")
                ->with("school_tests.test")
                ->with("school_tests.test.grade")
                ->with("school_competitions.competition")
                ->with("school_competitions.students")
                ->with("school_competitions.competition.grade")
                ->with("year")
                ->where("year_id", $activeYear->id)
                ->where("school_id", $parameters["school_id"])
                ->get()->first();
            return array("school_year" => $schoolYear, "active_year" => $activeYear);
            
        }

        if(isset($parameters["getActiveYear"])){
            return Year::where("open",1)->get()->first();
        }

        if(isset($parameters["getPublic"])){
            return Year::where("resultsArePublic",1)->get()->first();
        }
        
        if(isset($parameters["isSchoolSignedUp"]) && isset($parameters["school_id"])){
            $params = array("getActiveYear" => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $schoolYear = SchoolYear::where("school_id", $parameters["school_id"])
                ->where("year_id", $activeYear["id"])
                ->get()
                ->first();
            if($schoolYear == null){
                return array("is_school_signed_up" => false);
            }else{
                return array("is_school_signed_up" => true, "activeSchoolYear" => $schoolYear->id);
            }
        }

    }

    public function getByParent($parent_id)
    {
        // TODO: Implement getByParent() method.
    }


}