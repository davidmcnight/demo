<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 11/22/16
 * Time: 11:31 AM
 */

require_once "templates/layout/header.php";
?>

<div class="container mt3"  id="main" ng-app="edu" ng-controller="SchoolController" ng-init="getAllInvoices();">
    <div class="col-xs-12 row mt2 mb2">
        <div class="jumbotron" ng-init="getActiveYear()">
            <h2>Invoices for {{activeYear.description}}</span></h2>
        </div>

        <div class="col-md-12 row">
            <h3>Page: {{ currentPage }} <span class="pull-right">Schools signed up: {{schoolYears.length}}</h3>
        </div>
        <div class="row mb1">

            <div class="col-md-2">
                <label for="search">Search:</label>
                <input ng-model="filter" id="search" class="form-control" placeholder="Filter text">
            </div>
            <div class="col-md-1">
                <label for="search"># Items:</label>
                <input type="number" min="1" max="99" class="form-control width-33" ng-model="pageSize">
            </div>
            <div class="col-md-5 pt2">
                <button class="btn-primary btn mr1" ng-click="getAllInvoices()">
                    <span class="glyphicon glyphicon-stats" aria-hidden="true"></span>
                    Show All
                </button>
                <button class="btn-danger btn mr1" ng-click="getUnpaidInvoices()">
                    <span class="glyphicon glyphicon-usd" aria-hidden="true"></span>
                    Show Unpaid
                </button>
                <button class="btn-success btn" ng-click="getPaidInvoices()">
                    <span class="glyphicon glyphicon-piggy-bank" aria-hidden="true"></span>
                    Show Paid
                </button>
            </div>
            <div class="col-md-3 pt2 pull-right mr2" style="text-align: right" >
                 <a class="btn-primary btn " target="_blank" href="/pdf/pdf.php">
                     <span class="glyphicon glyphicon-print" aria-hidden="true"></span>
                     Invoices
                 </a>
                 <a class="btn-danger btn " target="_blank" href="/pdf/labels.php">
                     <span class="glyphicon glyphicon-print" aria-hidden="true"></span>
                      Labels
                 </a>
            </div>

        </div>
        <div class="row col-xs-12">
            <table class="table table-striped">
                <tr >
                    <th>Invoice#</th>
                    <th>Id</th>
                    <th>Name</th>
                    <th>City</th>
                    <th>State</th>
                    <th class="text-primary">Bill</th>
                    <th class="text-success">Paid</th>
                    <th class="text-danger"> Due</th>
                    <th>Payment</th>
                    <th>Actions</th>
                </tr>
                <tr dir-paginate="sy in schoolYears | filter:filter : false| itemsPerPage: pageSize" current-page="currentPage">
                    <td>{{sy.invoice_number}}</td>
                    <td>{{sy.school.id}}</td>
                    <td>{{sy.school.name}}</td>
                    <td>{{sy.school.billing_address.city}}</td>
                    <td>{{sy.school.billing_address.state}}</td>
                    <td class="text-primary" ng-if="sy.school.league.id == 2">${{(sy.school_competitions.length * 80) + (sy.school_tests.length * 25) + 6.00}}</td>
                    <td class="text-primary" ng-if="sy.school.league.id == 1">${{(sy.school_competitions.length * 70) + (sy.school_tests.length * 25) + 6.00}}</td>
                    <td class="text-success">${{sy.amount_paid}}</td>
                    <td class="text-danger" ng-if="sy.school.league.id == 2">${{(sy.school_competitions.length * 80) + (sy.school_tests.length * 25) + 6.00 - sy.amount_paid}} </td>
                    <td class="text-danger" ng-if="sy.school.league.id == 1">${{(sy.school_competitions.length * 70) + (sy.school_tests.length * 25) + 6.00 - sy.amount_paid}} </td>
                    <td>
                        <input type="text" ng-model="invoicePayments[$index]" class="form-control col-xs-6" style="display: inline-block; width: 40%;margin-right: 5px;">
                        <button class="btn btn-success" ng-click="makeAPayment(sy,invoicePayments[$index] )">
                            <span class="glyphicon glyphicon-usd" aria-hidden="true"></span>
                            Pay
                        </button>
                    </td>
                    <td><a target="_blank" href="/pdf/single_pdf.php?syId={{sy.id}}" class="btn btn-primary"><span class="glyphicon glyphicon-print" aria-hidden="true"></span></a></td>
                </tr>
            </table>
            <div ng-controller="OtherController" class="other-controller">
                <div class="text-center">
                    <dir-pagination-controls boundary-links="true" on-page-change="pageChangeHandler(newPageNumber)" template-url="../../dirPagination.tpl.html"></dir-pagination-controls>
                </div>
            </div>
        </div>
    </div>
</div>
