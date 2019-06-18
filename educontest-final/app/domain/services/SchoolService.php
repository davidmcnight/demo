<?php

/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/29/16
 * Time: 3:57 PM
 */
class SchoolService implements IRestService{


    public function get($id){
        $school = School::with("league")
            ->with("billing_address")
            ->with("shipping_address")
            ->with("school_year.year")
            ->with("school_year.school_competitions.competition")
            ->with("school_year.school_competitions.competition.grade")
            ->with("school_year.school_competitions.competition.year")
            ->where("id", $id)->get()->first();
        return $school;
    }

    public function getAll(){
        $schools = School
            ::join("league", "league.id", "=", "school.league_id")
            ->join("shipping_address", "shipping_address.school_id", "=", "school.id")
            ->select("school.id", "school.name", "school.contact",
                "shipping_address.address1", "shipping_address.city", "shipping_address.state")
            ->get();
        return $schools;
    }

    function generateRandomString($length = 6) {
        $characters = '23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < $length; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }

    public function create(IEduObject $object){
        $object = $object->save();
        return $object;
    }

    public function post($data){

        try {

//            printNicely($data);
//            exit;


            //Create School
            $school = Factory::createModel("School", $data["school"]);
            $school->password = $this->generateRandomString(6);
            $school->save();

            //set last id of billing and shipping address
            $data["billing_address"]["school_id"] = $school->id;
            $data["shipping_address"]["school_id"] = $school->id;

            //set state


            //Create BillingAddress and ShippingAddress
            $billing_address = Factory::createModel("BillingAddress", $data["billing_address"]);
            $billing_address->save();
            $shipping_address = Factory::createModel("ShippingAddress", $data["shipping_address"]);
            $shipping_address->save();


            echo $school->name . '<br>';

            $mail = new PHPMailer;
            $mail->SMTPDebug = 3;
            // $mail->isSMTP();                                      // Set mailer to use SMTP
            $mail->Host = 'smtp.gmail.com';  // Specify main and backup SMTP servers
            $mail->SMTPAuth = true;                               // Enable SMTP authentication
            $mail->Username = 'scores@educontest.com';                 // SMTP username
            $mail->Password = 'Mathfax1';                           // SMTP password
            $mail->SMTPSecure = 'tls';                            // Enable TLS encryption, `ssl` also accepted
            $mail->Port = 587;                                // TCP port to connect to
            $mail->isHTML(true);
            $mail->setFrom('scores@educontest.com', 'Mathfax');
            $mail->addAddress($school->school_email);
            if ($school->league_id == 1) {
                $mail->Subject = 'Thank you for Signing up for Catholic Math League';
                $mail->Body = '<html>
                        <body>
                            <div class="col-xs-12">
                                <h2>Thank You for signing up for Catholic Math League!</h2>
                                <h3>Here are your login credentials:</h3>
                                <p>School Code: ' . $school->id . '</p>
                                <p>Password: ' . $school->password . '</p>
                            </div>
                            </body>
                        </html>';
            } else {
                $mail->Subject = 'Thank you for Signing up for Educontest';
                $mail->Body = '<html>
                        <body>
                            <div class="col-xs-12">
                                <h2>Thank You for signing up for Educontest!</h2>
                                <h3>Here are your login credentials:</h3>
                                <p>School Code: ' . $school->id . '</p>
                                <p>Password: ' . $school->password . '</p>
                            </div>
                            </body>
                        </html>';
            }


            if(!$mail->send()) {
                echo 'Message could not be sent.';
                echo 'Mailer Error: ' . $mail->ErrorInfo;
            } else {
                echo 'Message has been sent';
            }

            //SEND EMAIL
            // $transport = Swift_SmtpTransport::newInstance('smtp.gmail.com', 465, "ssl")
            // ->setUsername(Config::EMAIL_USER)
            // ->setPassword(Config::EMAIL_PASSWORD);
            // $mailer = Swift_Mailer::newInstance($transport);
            // if ($school->league_id == 1) {
            // $message1 = '<html>
            // <body>
            // <div class="col-xs-12">
            // <h2>Thank You for signing up for Catholic Math League!</h2>
            // <h3>Here are your login credentials:</h3>
            // <p>School Code: ' . $school->id . '</p>
            // <p>Password: ' . $school->password . '</p>
            // </div>
            // </body>
            // </html>';
            // } else {
            // $message1 = '<html>
            // <body>
            // <div class="col-xs-12">
            // <h2>Thank You for signing up for Educontest!</h2>
            // <h3>Here are your login credentials:</h3>
            // <p>School Code: ' . $school->id . '</p>
            // <p>Password: ' . $school->password . '</p>
            // </div>
            // </body>
            // </html>';
            // }
// 
            // $message = Swift_Message::newInstance("Welcome " . $school->name)
            // ->setFrom(array('davidmcnight@gmail.com' => 'DavidMcNight'))
            // ->setTo(array('justin.pidcock@3foldx.com'))
            // ->setBody($message1, "text/html");
            // $result = $mailer->send($message);

            if(isset($data["admin"])){
                echo json_encode(array("school_id" => $school->id, "password" => $school->password));
            }else {
                $login = new Login();
                $login->login($school->id, $school->password);
            }
            //LOG CUSTOMER IN
        }catch (Exception $e){
            echo $e->getMessage();
        }




    }

    public function put($data){

        if(isset($data["update_password"])){
            $pwd = $this->generateRandomString();
            $school_data = array("id"=>$data["school_id"], "gen-pwd" => $pwd);
//            $school = Factory::createModel("School", $school_data);
            $db = Database::getInstance();
            $sql = "UPDATE school SET password = :pwd WHERE id = :id";
            $statement = $db->prepare($sql);
            $statement->bindValue("pwd", $pwd);
            $statement->bindValue("id", $data["school_id"]);
            $statement->execute();
            $db=null;


        }else{
            //update school
            $school = Factory::createModel("School", $data["school"]);
            $recordSchool = School::where("id", $school->id);
            $recordSchool->update($school->toArray());

            //update school billing address
            $billing_address = Factory::createModel("BillingAddress",$data["billing_address"]);
            $recordBa = BillingAddress::where("id", $billing_address->id);
            $recordBa->update($billing_address->toArray());

            //update school shipping address
            $shipping_address = Factory::createModel("ShippingAddress",$data["shipping_address"]);
            $recordSa = ShippingAddress::where("id", $shipping_address->id);
            $recordSa->update($shipping_address->toArray());

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

        if(isset($parameters["deleteSchool"])){
            $school = School::find($parameters["school_id"]);
            $school->delete();
            return $school;
        }

        if(isset($parameters["getAllSchoolsPassword"])){
            $db = Database::getInstance();
            $query = "SELECT id, name, password, contact FROM school";
            $statement = $db->query($query);
            $results = $statement->fetchAll(PDO::FETCH_ASSOC);
            return $results;
        }

        if(isset($parameters["min"])){
            $school = School::where("id",$parameters["id"])
                ->get()->first();
            return $school;
        }
        if(isset($parameters["getCurrentSchools"])){
            $params = array("getActiveYear" => 1);
            $activeYear = Factory::createService("Year")->find($params);
            $schools = School
                ::join("league", "league.id", "=", "school.league_id")
                ->join("shipping_address", "shipping_address.school_id", "=", "school.id")
                ->join("school_year", "school_year.school_id", "=", "school.id")
                ->where("school_year.year_id", $activeYear->id)
                ->select("school.id", "school.name", "school.contact",
                    "shipping_address.address1", "shipping_address.city", "shipping_address.state")
                ->get();
            return $schools;
        }
    }

    public function getByParent($parent_id)
    {
        // TODO: Implement getByParent() method.
    }


}