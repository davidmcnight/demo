<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 4/7/17
 * Time: 10:08 AM
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
// 
 // $settings = array(
     // 'driver' => 'mysql',
     // 'host' => '127.0.0.1',
     // 'database' => 'educontest_test',
     // 'username' => 'root',
     // 'password' => 'root',
     // 'collation' => 'utf8_general_ci',
     // 'prefix' => ''
 // );

$settings = array(
  'driver' => 'mysql',
  'host' => '127.0.0.1',
  'database' => 'edudavid_edu',
  'username' => 'edudavid_davey',
  'password' => '!Rishman1',
  'collation' => 'utf8_general_ci',
  'prefix' => ''
);





$container = new Illuminate\Container\Container;
$connFactory = new \Illuminate\Database\Connectors\ConnectionFactory($container);
$conn = $connFactory->make($settings);
$resolver = new \Illuminate\Database\ConnectionResolver();
$resolver->addConnection('default', $conn);
$resolver->setDefaultConnection('default');
\Illuminate\Database\Eloquent\Model::setConnectionResolver($resolver);


try {


    $year = Year::where("open", 1)->get()->first();
    $sy =
        SchoolYear::with("school")
            ->with("school.shipping_address")
            ->where("year_id", $year->id)->get();

    $counter = 1;

    $pdf = new FPDF();
    $pdf->AddPage();
    $pdf->SetFont('Arial','B',12);

    $col = 1;
    $row = 1;
    foreach ($sy as $s){


            $x = 0;

            if($col == 2){
                $x = 75;
            }

            if($col == 3){
                $x = 150;
            }

            $y = 16;

            if($row == 2){$y = 44;}
            if($row == 3){$y = 73;}
            if($row == 4){$y = 100;}
            if($row == 5){$y = 127;}
            if($row == 6){$y = 155;}
            if($row == 7){$y = 182;}
            if($row == 8){$y = 209;}
            if($row == 9){$y = 236;}
            if($row == 10){$y = 261;$pdf->SetFont('Arial','B',8); }


            $len = 20;
            if(
                strlen($s->school->contact) > $len
                || strlen($s->school->name) > $len
                || strlen($s->school->shipping_address->address1) > $len
                || strlen($s->school->shipping_address->city .
                    $s->school->shipping_address->state .
                    $s->school->shipping_address->zip) + 3 > $len){

                $pdf->SetFont('Arial','B',10);
				
            }


            $len = 30;
            if(
                strlen($s->school->contact) > $len
                || strlen($s->school->name) > $len
                || strlen($s->school->shipping_address->address1) > $len
                || strlen($s->school->shipping_address->city .
                        $s->school->shipping_address->state .
                        $s->school->shipping_address->zip) + 3 > $len){
                    $pdf->SetFont('Arial','B',8);
                }

        $len = 35;
        if(
            strlen($s->school->contact) > $len
            || strlen($s->school->name) > $len
            || strlen($s->school->shipping_address->address1) > $len
            || strlen($s->school->shipping_address->city .
                $s->school->shipping_address->state .
                $s->school->shipping_address->zip) + 3 > $len){
            $pdf->SetFont('Arial','B',7);
        }


		
		if($row == 10){$y = 264;$pdf->SetFont('Arial','B',9); }

        //school contact
            $pdf->SetY($y);
            $pdf->SetX($x);
			$pdf->Cell(0,0, $s->school->contact);
			// if($row == 10){
// 				
			// }
            

            //school name
            $pdf->SetY($y + 5);
			if($row == 10){
				 $pdf->SetY($y + 4);
			}
			
            $pdf->SetX($x);
            $pdf->Cell(0,0, $s->school->name);

            //school address 1
            $pdf->SetY($y + 10);
			if($row == 10){
				 $pdf->SetY($y + 8);
			}
            $pdf->SetX($x);
            $pdf->Cell(0,0, $s->school->shipping_address->address1);

            //city zip
            $pdf->SetY($y + 15);
			if($row == 10){
				 $pdf->SetY($y + 12);
			}
            $pdf->SetX($x);
            $pdf->Cell(0,0, $s->school->shipping_address->city
                .  ", " .   $s->school->shipping_address->state
                . " " . $s->school->shipping_address->zip);

        $pdf->SetFont('Arial','B',12);
        $col++;
        $counter++;
        if($counter % 3 == 1){
            $row++;
            $col = 1;
        }

        if($counter % 30 == 1) {
            $col = 1;
            $row = 1;
            $pdf->AddPage();
        }



    }

    $pdf->Output();

    printNicely($sy);
}catch (Exception $e){

}