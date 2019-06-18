<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 1/31/17
 * Time: 10:45 AM
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


$container = new Illuminate\Container\Container;
$connFactory = new \Illuminate\Database\Connectors\ConnectionFactory($container);
$conn = $connFactory->make($settings);
$resolver = new \Illuminate\Database\ConnectionResolver();
$resolver->addConnection('default', $conn);
$resolver->setDefaultConnection('default');
\Illuminate\Database\Eloquent\Model::setConnectionResolver($resolver);




try{


    $year = Year::where("open",1)->get()->first();
    $invoices =
        SchoolYear::with("school")
        ->with("school_competitions_pdf")
        ->with("school_tests")
        ->with("school.league")
        ->with("school.shipping_address")
        ->with("school.billing_address")
        ->with("school_competitions_pdf.competition")
        ->with("school_competitions_pdf.competition.grade")
        ->with("school_tests.test")
        ->with("school_tests.test.grade")
        ->where("year_id", $year->id)->get();
		
	
    $pdf = new FPDF();

    $today = getdate();
    $invoiceDate = $today["mon"] . "/" . $today["mday"] . "/" . $today["year"];
    foreach ($invoices as $invoice){

        //start with shipping cost
        $totalCost = 6.00;

        //check league
        if($invoice->school->league->id == 1){
            //70
            $totalCost = $totalCost
                + ($invoice->school_competitions_pdf->count() * 70)
                + ($invoice->school_tests->count() * 25);

        }else{
            $totalCost = $totalCost
            + ($invoice->school_competitions_pdf->count() * 80)
            + ($invoice->school_tests->count() * 25);
        }
       // printNicely($totalCost);

        if($totalCost - $invoice->amount_paid  > 0){

        $pdf->AddPage();

        //TOP LINE
        $pdf->SetFont('Arial','B',12);
        $pdf->SetY(20);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "Invoice# " . $invoice->invoice_number);
        $pdf->SetX(130);
        $pdf->Cell(0,20, "Invoice Date: " . $invoiceDate);

        $pdf->SetY(35);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->Cell(146,1, "", 0,1,'L',true);

        //LEAGUE INFO
        $pdf ->SetX(30);
        $pdf->Cell(50,20, $invoice->school->league->description);
        $pdf->SetX(120);
        $pdf->SetFont('Arial','',10);
        $pdf->Cell(50,20, "Phone: (864) 286-0020");

        $pdf->SetY(42);

        $pdf ->SetX(30);
        $pdf->SetFont('Arial','B',12);
        $pdf->Cell(50,20, "PO Box 769");

        $pdf->SetX(120);
        $pdf->SetFont('Arial','',10);
        $pdf->Cell(50,20, "Fax: (864) 286-0520");


        $pdf->SetY(48);

        $pdf ->SetX(30);
        $pdf->SetFont('Arial','B',12);
        $pdf->Cell(50,20, "Taylors, SC 29687");

        $pdf->SetX(120);
        $pdf->SetFont('Arial','',10);
        $pdf->Cell(50,20, "E-mail: scores@educontest.com");


        //LINE BREAK
        $pdf->SetY(65);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->Cell(146,1, "", 0,1,'L',true);


        //SCHOOL INFO
        $pdf->SetFont('Arial','B',12);

        $pdf->SetY(65);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "Accounts Payable:");
        $pdf->SetX(120);
        $pdf->Cell(50,20, "Ship To: ");

        $pdf->SetY(72);
        $pdf ->SetX(30);
        $pdf->Cell(50,20, $invoice->school->name);
        $pdf->SetX(120);
        $pdf->Cell(50,20, $invoice->school->billing_name);

        $pdf->SetY(79);
        $pdf ->SetX(30);
        $pdf->Cell(50,20, $invoice->school->billing_address["address1"] . " " . $invoice->school->billing_address["address2"]);
        $pdf->SetX(120);
        $pdf->Cell(50,20, $invoice->school->name);

        $pdf->SetY(86);
        $pdf ->SetX(30);
        $pdf->Cell(50,20,
            $invoice->school->billing_address["city"]
            . ", " . $invoice->school->billing_address["state"]
            . " " . $invoice->school->billing_address["zip"]
        );
        $pdf->SetX(120);
        $pdf->Cell(50,20, $invoice->school->shipping_address["address1"] . " " . $invoice->school->billing_address["address2"]);

        $pdf->SetY(93);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "");

        $pdf ->SetX(120);
        $pdf->Cell(50,20,
            $invoice->school->shipping_address["city"]
            . ", " . $invoice->school->shipping_address["state"]
            . " " . $invoice->school->shipping_address["zip"]
        );

        //LINE BREAK
        $pdf->SetY(110);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->Cell(146,1, "", 0,1,'L',true);

        //GRADES AND PRICES
        $pdf->SetY(110);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "Grade Name");
        $pdf->SetX(165);
        $pdf->Cell(50,20, "Price");

        //SET FONT
        $pdf->SetFont('Arial','',10);

        $y = 110;
        $total = 0.00;

        foreach ($invoice->school_competitions_pdf as $sc){
            $pdf->SetY($y + 7);
            $pdf->SetX(30);
            $pdf->Cell(50,20, $sc->competition->grade->name);
            $pdf->SetX(165);
            if($invoice->school->league->id == 1){
                $pdf->Cell(50,20, "$70.00");
                $total += 70.00;
            }else{
                $pdf->Cell(50,20, "$80.00");
                $total += 80.00;
            }

            $pdf->SetY($y + 20);
            $pdf->SetX(31);
            $pdf->SetFillColor(0,0,0);
            $pdf->Cell(146,0.3, "", 0,1,'L',true);

            $y += 7;
        }

        foreach ($invoice->school_tests as $st){
            $pdf->SetY($y + 7);
            $pdf->SetX(30);
            $pdf->Cell(50,20, "Practice Test: " . $st->test->grade->name);
            $pdf->SetX(165);

            $pdf->Cell(50,20, "$25.00");
            $total += 25.00;
            $pdf->SetY($y + 20);
            $pdf->SetX(31);
            $pdf->SetFillColor(0,0,0);
            $pdf->Cell(146,0.3, "", 0,1,'L',true);
            $y += 7;
        }

        //shipping handling
        $pdf->SetY($y + 7);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "Registration Fee");
        $pdf->SetX(165);

        $pdf->Cell(50,20, "$6.00");
        $total += 6.00;
        $pdf->SetY($y + 20);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);

        $y += 7;


        //LINE BREAK
        $y += 15;
        $pdf->SetY($y);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->Cell(146,1, "", 0,1,'L',true);

        //subtotal

        $pdf->SetFont('Arial','',12);
        $pdf->SetY($y - 4);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "Subtotal");
        $pdf->SetX(161);

        $pdf->Cell(50,20, "$" . money_format("%i",$total));
        $pdf->SetY($y + 5);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);

        if($invoice->amount_paid > 0){
            $y += 5;
            $pdf->SetY($y);
            $pdf->SetX(31);
            $pdf->SetFillColor(0,0,0);
            $pdf->SetFont('Arial','',12);
            $pdf->SetY($y);
            $pdf->SetX(30);
            $pdf->Cell(50,20, "Amount Paid");
            $pdf->SetX(158);

            $pdf->Cell(50,20, "- $" . money_format("%i",$invoice->amount_paid));
            $total -= $invoice->amount_paid;
            $pdf->SetY($y);
            $pdf->SetX(31);
            $pdf->SetFillColor(0,0,0);






        }


        $y += 15;
        $pdf->SetY($y);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->Cell(146,1, "", 0,1,'L',true);



        $pdf->SetY($y);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->SetFont('Arial','B',14);
        $pdf->SetY($y);
        $pdf->SetX(30);
        $pdf->Cell(50,20, "Amount Due");
        $pdf->SetX(158);
        $pdf->Cell(50,20, "$" . money_format("%i",$total));

        $y += 15;
        $pdf->SetY($y);
        $pdf->SetX(31);
        $pdf->SetFillColor(0,0,0);
        $pdf->Cell(146,1, "", 0,1,'L',true);





    }//if cost
    }
    $pdf->Output();
    
}catch(Exception $e){
    $e->getMessage();
}






?>


