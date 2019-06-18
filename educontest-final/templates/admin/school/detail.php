<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 9/30/16
 * Time: 1:31 PM
 */

require_once "templates/layout/header.php" ?>

<div class="container" id="main" ng-app="edu" ng-controller="SchoolController"
     ng-init="getSchool(<?php echo $id; ?>)">
    <div class="row col-xs-12">
        <div class="jumbotron">
            <h2>{{school.name}}</h2>
        </div>

        <div class="col-xs-7">
            <div class="panel panel-default">
                <div class="panel-heading">
                   School Info
                </div>
                <table class="table table-striped">
                   <tr>
                       <td>League:</td>
                       <td class="bold">{{school.league.description}}</td>
                   </tr>
                    <tr>
                        <td>Contact:</td>
                        <td class="bold">{{school.contact}}</td>
                    </tr>
                    <tr>
                        <td>Billing Name:</td>
                        <td class="bold">{{school.contact}}</td>
                    </tr>
                    <tr>
                        <td>Purchase Order:</td>
                        <td class="bold">{{school.purchase_order}}</td>
                    </tr>
                    <tr>
                        <td>Email</td>
                        <td class="bold">{{school.school_email}}</td>
                    </tr>
                    <tr>
                        <td>Phone:</td>
                        <td class="bold">{{school.phone}}</td>
                    </tr>
                    <tr>
                        <td>Fax:</td>
                        <td class="bold">{{school.fax}}</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="col-xs-5">
            <div class="panel panel-default">
                <div class="panel-heading">
                   Competitions by Year
                </div>
                <table class="table table-striped">
                    <tr><td>2016</td></tr>
                    <tr><td>2015</td></tr>
                    <tr><td>2014</td></tr>
                    <tr><td>2013</td></tr>
                </table>
            </div>
        </div>
        <div class="col-xs-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Shipping Info
                </div>
                <table class="table table-striped">
                    <tr>
                        <td>Address 1:</td>
                        <td class="bold">{{school.shippingAddress.address1}}</td>
                    </tr>
                    <tr>
                        <td>Address 2:</td>
                        <td class="bold">{{school.shippingAddress.address2}}</td>
                    </tr>
                    <tr>
                        <td>City:</td>
                        <td class="bold">{{school.shippingAddress.city}}</td>
                    </tr>
                    <tr>
                        <td>State:</td>
                        <td class="bold">{{school.shippingAddress.state}}</td>
                    </tr>
                    <tr>
                        <td>Zip</td>
                        <td class="bold">{{school.shippingAddress.zip}}</td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="col-xs-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Billing Info
                </div>
                <table class="table .table-striped">
                    <tr>
                        <td>Address 1:</td>
                        <td class="bold">{{school.billingAddress.address1}}</td>
                    </tr>
                    <tr>
                        <td>Address 2:</td>
                        <td class="bold">{{school.billingAddress.address2}}</td>
                    </tr>
                    <tr>
                        <td>City:</td>
                        <td class="bold">{{school.billingAddress.city}}</td>
                    </tr>
                    <tr>
                        <td>State:</td>
                        <td class="bold">{{school.billingAddress.state}}</td>
                    </tr>
                    <tr>
                        <td>Zip</td>
                        <td class="bold">{{school.billingAddress.zip}}</td>
                    </tr>
                </table>
            </div>
        </div>


    </div>






</div>
</body>
</html>

