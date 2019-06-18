<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 10/3/16
 * Time: 10:06 AM
 */
require_once "templates/layout/header.php"

?>

<div class="container pt2 mt3" id="main" ng-app="edu" ng-controller="eduController">
    <div style="background: #efefef; border: 1px solid #ccc; margin: -24px -11px 30px -11px;">
    <div class="row">
        <div class="col-sm-6 left-side">
            <img class="featured" style="width: 100%;" src="/public/assets/img/educontestpic.jpg">
        </div>
        <div class="col-sm-6 right-side">
        	<div class="cob">
	            <h2 style="margin-bottom: 15px;">Customer Testimonials</h2>
	            <p>"Yours is one of the best run contests in which we participate."</p>
	            <p> "Our students credit these tests to improving their standardized test results."</p>
	            <p> "Thank you so much! We can't wait till next year!"</p>
	            <p> "We have been very pleased with your service and have gotten great results from your contests."</p>
        	</div>
        </div>
    </div>
    </div>
    <p style="font-size: 22px; text-align: center; padding: 0 30px;margin-bottom: 30px;">Educontest is a national math competition that was started in 1997 and currently serves hundreds of schools. Math competitions are offered for each grade from third grade through Advanced Math.</p>
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-2 col-sm-offset-0">
            <div class="p1 bucket">
                <p style="text-align: center; margin-bottom: 30px;"><a class="cta" href="/results">See Your Test Results</a></p>
            </div>
        </div>
        <div class="col-sm-6 col-md-4">
            <div class="p1 bucket">
                <p style="text-align: center; margin-bottom: 30px;"><a class="cta" href="/registration">Register Online</a></p>
            </div>
        </div>
        <br />
<!--        --><?php //printNicely($_SESSION); ?>
    </div>
</div>

